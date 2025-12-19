#include "PathOram.h"
#include <filesystem>

static void deserializeData(std::unique_ptr<unsigned char *[]> &data,
                            const size_t &dataSize, const size_t &blockSize) {
  // Check current directory
  try {
    std::cout << "Attempting to read serialized_data.bin from: "
              << std::filesystem::absolute("serialized_data.bin") << std::endl;
  } catch (...) {
    std::cout << "Attempting to read serialized_data.bin from current directory"
              << std::endl;
  }

  std::ifstream is("serialized_data.bin", std::ios::binary);

  if (!is) {
    std::cerr << "FATAL ERROR: Cannot open 'serialized_data.bin' for reading."
              << std::endl;
    std::cerr << "Make sure the Client has run first and generated this file "
                 "in the SAME directory as the Server."
              << std::endl;
    exit(1);
  }

  cereal::BinaryInputArchive archive(is);

  // Deserialize each string
  for (size_t i = 0; i < dataSize; ++i) {
    // Now deserialize the string data
    for (size_t j = 0; j < blockSize; ++j) {
      archive(data[i][j]);
    }
  }
}

using asio::ip::tcp;

int main() {

  asio::io_context io_context;
  size_t leafNode = 0;
  std::unique_ptr<unsigned char *[]> storage;

  // Listen for new connections
  tcp::acceptor acceptor(io_context, tcp::endpoint(tcp::v4(), 8900));

  std::cout << "Server listening on port 8900...\n";
  tcp::socket socket(io_context);
  acceptor.accept(socket);
  std::cout << "Client connected: " << socket.remote_endpoint() << "\n";

  PathOram instanceOram;
  vector<size_t> attributes(5);
  while (true) {

    cout << "Waiting for attributes...\n";
    asio::read(socket, asio::buffer(attributes.data(),
                                    attributes.size() * sizeof(size_t)));

    instanceOram.setL(attributes[0]);
    instanceOram.setNoOfLeafs(attributes[1]);
    instanceOram.setSizeOfBlock(attributes[2]);
    instanceOram.setTreeSize(attributes[3]);
    instanceOram.setZ(attributes[4]);
    instanceOram.setPathLength(attributes[0] + 1);

    instanceOram.init_arrays();
    attributes.clear();

    size_t blockSize = instanceOram.getsizeOfBlock();

    std::unique_ptr<unsigned char *[]> data(
        new unsigned char *[instanceOram.getZ() * instanceOram.getTreeSize()]);
    for (size_t i = 0; i < instanceOram.getZ() * instanceOram.getTreeSize();
         ++i) {
      data[i] = new unsigned char[blockSize];
    }
    deserializeData(data, instanceOram.getZ() * instanceOram.getTreeSize(),
                    instanceOram.getsizeOfBlock());

    instanceOram.setTree(data);

    size_t n = instanceOram.response_size();
    size_t response_stash_size = n * blockSize;

    // Allocate the contiguous block
    unsigned char *dataBlock = new unsigned char[response_stash_size];

    // Create an array of pointers for easy access
    std::unique_ptr<unsigned char *[]> encrypted_response_stash =
        std::make_unique<unsigned char *[]>(n);

    // Point each element in 'data' to the correct location in 'dataBlock'
    for (size_t i = 0; i < n; ++i) {
      encrypted_response_stash[i] = &dataBlock[i * blockSize];
    }
    /* 1. log the first payload */
    log_value("response_stash", response_stash_size);
    cout << response_stash_size / 1024 << " KB received !\n";
    size_t N;

    asio::read(socket, asio::buffer(&N, sizeof(N)));

    storage = std::make_unique<unsigned char *[]>(N);

    size_t cipherBufSize = sizeof(size_t) + EVP_MAX_IV_LENGTH;

    for (size_t i = 0; i < N; ++i) {
      storage[i] = new unsigned char[cipherBufSize];
      std::memset(storage[i], 0, cipherBufSize);
    }

    receiveEncryptedMap(socket, storage, N);

    // log_value("storage_size", total_bytes(storage));

    // receiveEncryptedStringMap(socket, stash);

    cout << "received Encrypted map" << endl;
    ;
    asio::write(socket, asio::buffer("ok"));

    char ack[2];
    while (true) {

      asio::read(socket, asio::buffer(ack, 2)); // read exactly 8 bytes

      if (std::string(ack, 1) == "R")
        ;
      else {
        cout << "Unknown request: " << ack << endl;
        break;
      } // Ignore other requests

      sendStoredEncryptedMap(socket, storage, N);
      // sendStoredEncryptedStringMap(socket, stash);

      asio::read(socket, asio::buffer(&leafNode, sizeof(size_t)));

      // Process the leaf node with ORAM instance
      instanceOram.access(leafNode, encrypted_response_stash);

      // Send the serialized path
      asio::write(socket, asio::buffer(dataBlock, response_stash_size));

      asio::read(socket, asio::buffer(dataBlock, response_stash_size));

      // Process the response with ORAM instance
      instanceOram.reWrite(encrypted_response_stash);

      receiveEncryptedMap(socket, storage, N);
      // receiveEncryptedStringMap(socket, stash);
    }
  }
};