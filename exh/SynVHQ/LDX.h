#pragma once
#include "PathOram.h"
#include <filesystem>

using namespace std;
using asio::ip::tcp;
// This line will allow you to use everything from the standard library without
// prefixing it with

class LDX {
private:
  unordered_map<string, int> DXst;
  vector<string> RAM;
  unordered_map<int, size_t> stRAM;
  unordered_map<string, string> client_stash;
  // Security parameter
  unsigned char *K;
  size_t max_length = 0;
  PathOram oramInstance;
  std::unique_ptr<char *[]> response_stash;
  std::unique_ptr<unsigned char *[]> encrypted_response_stash;
  mt19937 generator;                               // Mersenne Twister generator
  uniform_int_distribution<uint64_t> distribution; // Uniform distribution
  asio::io_context io_context;
  tcp::socket socket;
  unsigned char *dataBlock;
  size_t response_stash_size, blockSize, n;
  ThreadPool *pool;
  // 2) allocate an array of pointers
  std::unique_ptr<unsigned char *[]> blobs;
  size_t N;
  std::unique_ptr<char *[]> plains;

  std::unique_ptr<unsigned char *[]> ciphers;
  // Allocate once (e.g. as members or in your main function)

public:
  LDX(const std::string &serverAddr, const uint16_t &port)
      : io_context(), socket(io_context), generator(std::random_device{}()),
        distribution(0, std::numeric_limits<uint64_t>::max()) {

    // Resolve the server address and port
    tcp::resolver resolver(io_context);
    auto endpoints = resolver.resolve(serverAddr, std::to_string(port));

    // Try to connect to one of the resolved endpoints
    try {
      asio::connect(socket, endpoints);
      std::cout << "Connected to " << serverAddr << ":" << port << std::endl;
    } catch (std::exception &e) {
      std::cerr << "Failed to connect: " << e.what() << std::endl;
    }
  }

  void getClientStash_size() { cout << client_stash.size() << "\n"; }

  void serializeData(const std::unique_ptr<unsigned char *[]> &data,
                     const size_t &dataSize, const size_t &blockSize) {

    try {
      std::cout << "Saving 'serialized_data.bin' to: "
                << std::filesystem::absolute("serialized_data.bin")
                << std::endl;
    } catch (...) {
      std::cout << "Saving 'serialized_data.bin' to current directory"
                << std::endl;
    }

    std::ofstream os("serialized_data.bin", std::ios::binary);
    cereal::BinaryOutputArchive archive(os);

    // Serialize each string in the array
    for (size_t i = 0; i < dataSize; ++i) {

      // Now serialize the string data
      for (size_t j = 0; j < blockSize; ++j) {
        archive(data[i][j]);
      }
    }
  }

  bool setupCS() {

    // Server has responded to a ping request
    std::cout << "Server Accepted Connection\n";

    vector<size_t> attributes;
    oramInstance = PathOram(RAM, stRAM, max_length, client_stash, K);
    RAM.clear();
    std::vector<string>().swap(RAM);

    attributes.push_back(oramInstance.getL());
    attributes.push_back(oramInstance.getNoOfLeafs());
    attributes.push_back(oramInstance.getsizeOfBlock());
    attributes.push_back(oramInstance.getTreeSize());
    attributes.push_back(oramInstance.getZ());

    n = oramInstance.response_size();
    blockSize = oramInstance.getsizeOfBlock();
    response_stash_size = n * blockSize;

    // Allocate the contiguous block
    dataBlock = new unsigned char[response_stash_size];

    // Create an array of pointers for easy access
    response_stash = std::make_unique<char *[]>(n);
    encrypted_response_stash = std::make_unique<unsigned char *[]>(n);

    // Point each element in 'data' to the correct location in 'dataBlock'
    for (size_t i = 0; i < n; ++i) {
      encrypted_response_stash[i] = &dataBlock[i * blockSize];
      response_stash[i] = new char[blockSize + 1 - EVP_MAX_IV_LENGTH];
      response_stash[i][blockSize - EVP_MAX_IV_LENGTH] = '\0';
    }

    N = stRAM.size();
    // Allocate memory for blobs
    // blobs = std::make_unique<unsigned char* []>(N);
    plains = std::make_unique<char *[]>(N);
    ciphers = std::make_unique<unsigned char *[]>(N);

    size_t i = 0;
    size_t cipherBufSize = sizeof(size_t) + EVP_MAX_IV_LENGTH;
    for (auto &kv : stRAM) {

      plains[i] = reinterpret_cast<char *>(&kv.second);

      ciphers[i] = new unsigned char[cipherBufSize];

      std::memset(ciphers[i], 0, cipherBufSize);

      ++i;
    }

    cout << "Request_Size: " << response_stash_size / 1024 << " KB\n";

    cout << "Block size: " << oramInstance.getsizeOfBlock() << " B\n";

    serializeData(oramInstance.getTree(),
                  oramInstance.getTreeSize() * oramInstance.getZ(),
                  oramInstance.getsizeOfBlock());

    oramInstance.deleteTree();

    // Send serialized attributes
    asio::write(socket, asio::buffer(attributes));

    size_t numOfThreads = std::thread::hardware_concurrency() / 2;
    cout << "Half Number Of Threads: " << numOfThreads << "\n";
    pool = new ThreadPool(numOfThreads);

    cout << "sending Encrypted map" << endl;

    // Send N
    asio::write(socket, asio::buffer(&N, sizeof(N)));

    sendEncryptedMap(socket, stRAM, K, *pool, N, plains, ciphers);
    // sendEncryptedStringMap(socket, client_stash, K);

    char ack[3];                              // extra byte for null-termination
    asio::read(socket, asio::buffer(ack, 3)); // read exactly 2 bytes

    if (std::string(ack, 2) == "ok")
      return true;
    return false;
  }

  void Setup(unsigned char *k, const map<string, string> &DX) {
    int addr = 0;
    K = k;

    for (const auto &pair : DX) {
      if (pair.second.size() + to_string(addr).size() > max_length)
        max_length = to_string(addr).size() + pair.second.size();

      RAM.push_back(to_string(addr) + pair.second);
      DXst[pair.first] = addr;
      ++addr;
    }

    if (!setupCS())
      cout << "Setup Error" << endl;
  }

  void AccessCS(const std::string &label, const size_t leafNode,
                const int &addr, std::string &doc_id) {

    // Send leafNode

    asio::write(socket, asio::buffer(&leafNode, sizeof(size_t)));

    stRAM[addr + 1] = distribution(generator) % oramInstance.getNoOfLeafs();

    asio::read(socket, asio::buffer(dataBlock, response_stash_size));

    decryptAll(*pool, encrypted_response_stash, n, blockSize, K,
               response_stash);

    oramInstance.check(response_stash, client_stash, addr, doc_id);

    oramInstance.reAll(response_stash, stRAM, DXst, leafNode, client_stash);

    encryptAll(*pool, response_stash, n, blockSize, K,
               encrypted_response_stash);

    asio::write(socket, asio::buffer(dataBlock, response_stash_size));

    sendEncryptedMap(socket, stRAM, K, *pool, N, plains, ciphers);

    // sendEncryptedStringMap(socket, client_stash, K);
  }

  void Get(const string &label, string &doc_id) {

    if (label == "S") {
      asio::write(socket, asio::buffer(label));
      return;
    }

    asio::write(socket, asio::buffer("R"));

    receiveDecryptReconstructAndEcho(socket, K, *pool, plains, N, ciphers);

    // receiveDecryptReconstructAndEchoStringMap(socket, K, client_stash);

    AccessCS(label, stRAM[DXst[label] + 1], DXst[label], doc_id);
  }
};