#pragma once
#include "LDX.h"
#include <filesystem>

// Include for std::mutex

using namespace std; // This line will allow you to use everything from the standard library without prefixing it with std::

class LAT {
private:

    mutex dataMutex1, dataMutex2, dataMutexGlobal;
  
    map<string, string> DX;
    unordered_map<string, size_t> DXst;

    //This queue incorporate Also the arrival time needed to calculate latency in the Evaluations
    queue<tuple<int, string, bool, std::chrono::time_point<std::chrono::high_resolution_clock>>> Q1;
    //This queue incorporate Also the arrival time needed to calculate latency in the Evaluations
    queue<tuple<int, string, bool, std::chrono::time_point<std::chrono::high_resolution_clock>>> Q2;
    queue<size_t> Q;
    
    bool response_flag;
    LDX ldx;
    const size_t keyLength = 16; // For a 128-bit key
    unsigned char key[16];

    std::atomic<int> queryCounter;
    // Stub function to simulate the ?DX.Setup
    void SigmaDX_Setup(const map<string, string>& DX) {
     
        ldx.LDX::Setup(key,  DX);
    }

public:



    LAT(const string k, const unordered_map<string, vector<string>>* MM) : ldx("127.0.0.1", 8900) {
       
        unsigned char salt[8];

        RAND_bytes(salt, sizeof(salt));

        deriveKeyFromPassword(k, salt, key, keyLength);

        for (const auto& pair : *MM) {
            const string& label = pair.first;
            const vector<string>& values = pair.second;

            for (int i = 0; i < values.size(); i++) {

                DX[label + to_string(i)] = "x-x" + values[i];
            }
            DXst[label] = values.size();    
        }
    
        DX["alpha"] = "x-xDummy";
        DXst["alpha"] = 1;

      
        SigmaDX_Setup(DX);

        queryCounter = 0;
    }
   

   
    void GetC_P(const int& id, const std::string& label) {
      
        int response_length = DXst[label];

      
        queryCounter.fetch_add(1, std::memory_order_relaxed);

       
        for (int i = 0; i < response_length; ++i) {
            // Capture the timestamp outside the lock so you incur minimal work while holding the mutex:
            auto now = std::chrono::high_resolution_clock::now();

            if (id == 0) {
                // Lock dataMutexGlobal AND dataMutex1 together, in a deadlock‐safe way:
                std::scoped_lock lock(dataMutexGlobal, dataMutex1);

                // Now it’s safe to push into Q1 and Q:
                Q1.emplace(
                    response_length,
                    label + std::to_string(i),
                    (i == response_length - 1),
                    now
                );
                Q.emplace(0);
                // lock goes out of scope here → both mutexes are released
            }
            else {
                // Lock dataMutexGlobal AND dataMutex2 together, again deadlock‐safe:
                std::scoped_lock lock(dataMutexGlobal, dataMutex2);

                Q2.emplace(
                    response_length,
                    label + std::to_string(i),
                    (i == response_length - 1),
                    now
                );
                Q.emplace(1);
                // lock is released here
            }
        }
    }
 

public:

    void SQPD_P_S(string data_size, int lambda) {
        //Evaluatio files
        std::string filename1 = "QueueSize1_" + data_size + "_" + std::to_string(lambda) + ".csv";
        std::string filename2 = "QueueSize2_" + data_size + "_" + std::to_string(lambda) + ".csv";
        std::string filename3 = "latency_data1_" + data_size + "_" + std::to_string(lambda) + ".csv";
        std::string filename4 = "latency_data2_" + data_size + "_" + std::to_string(lambda) + ".csv";
        ofstream QSize_file1(filename1, ios_base::app);
        ofstream QSize_file2(filename2, ios_base::app);
        QSize_file1 << "QueueSize,DeltaTime" << endl;
        QSize_file2 << "QueueSize,DeltaTime" << endl;
        ofstream latency_file1(filename3, ios_base::app);
        ofstream latency_file2(filename4, ios_base::app);
        latency_file1 << "ResponseLength,Latency" << endl;
        latency_file2 << "ResponseLength,Latency" << endl;

        size_t counter = 0;
        double latency = 0;
        
        double waitTime;
        int id;
        string label_star;
        bool flag;
        vector<string> result;
        pair<string, nullptr_t> result_pair;
        int dummies = 0;
	
        
        // Warmup phase
        if (queryCounter == 100) {
            dummies = 0;
            // Empty Q, Q1, Q2
            {
                std::scoped_lock l(dataMutexGlobal, dataMutex1, dataMutex2);
                while (!Q.empty()) Q.pop();
                while (!Q1.empty()) Q1.pop();
                while (!Q2.empty()) Q2.pop();
            }
        }

        std::chrono::time_point<std::chrono::high_resolution_clock> start, end, arrival_time, startQueue;
        std::tuple<int, std::string, bool, std::chrono::time_point<std::chrono::high_resolution_clock>> e;

        // Process up to 5100 queries
        while (queryCounter <= 5100) {
            int Cid_local = 2;
            

            {
                std::lock_guard<std::mutex> g(dataMutexGlobal);
                if (!Q.empty()) {
                    Cid_local = Q.front();
                    Q.pop();
                }
            }

            if (Cid_local == 0) {
                std::lock_guard<std::mutex> g(dataMutex1);
                if (!Q1.empty()) {
                    e = Q1.front();
                    Q1.pop();
                    id = std::get<0>(e);
                    label_star = std::get<1>(e);
                    flag = std::get<2>(e);
                    arrival_time = std::get<3>(e);
                }
            }
            else if (Cid_local == 1) {
                std::lock_guard<std::mutex> g(dataMutex2);
                if (!Q2.empty()) {
                    e = Q2.front();
                    Q2.pop();
                    id = std::get<0>(e);
                    label_star = std::get<1>(e);
                    flag = std::get<2>(e);
                    arrival_time = std::get<3>(e);
                }
            }
            else {
             
                label_star = "alpha";
                id = 1;
                flag = true;
                ++dummies;
            }

            // Log queue sizes & latency after warmup
            if (queryCounter > 100) {
                auto now = std::chrono::high_resolution_clock::now();
                {
                    std::lock_guard<std::mutex> g(dataMutex1);
                    QSize_file1 << Q1.size() << ","
                        << std::chrono::duration_cast<std::chrono::microseconds>(now - startQueue).count()
                        << "\n";
                }
                {
                    std::lock_guard<std::mutex> g(dataMutex2);
                    QSize_file2 << Q2.size() << ","
                        << std::chrono::duration_cast<std::chrono::microseconds>(now - startQueue).count()
                        << "\n";
                }
            }

            startQueue = std::chrono::high_resolution_clock::now();

            start = std::chrono::high_resolution_clock::now();
            GetP_S(label_star, result_pair);
            end = std::chrono::high_resolution_clock::now();
            latency += std::chrono::duration_cast<std::chrono::microseconds>(end - start).count();
            ++counter;

            //Warmup phase Done
            if (queryCounter > 1e2) {

                //change nullptr by the latency_file to record latency
                if(Cid_local == 0) {
                    sendToCid(id, label_star, result_pair.first, result, &latency_file1, flag, arrival_time);
                }
                else if (Cid_local == 1) {
                    sendToCid(id, label_star, result_pair.first, result, &latency_file2, flag, arrival_time);
                }
                else {
                    sendToCid(id, label_star, result_pair.first, result, &latency_file1, flag, arrival_time);
                    sendToCid(id, label_star, result_pair.first, result, &latency_file2, flag, arrival_time);
				}
                

                if (counter % 5000 == 0) {
                    QSize_file1.flush();
                    latency_file1.flush();
                    QSize_file2.flush();
                    latency_file2.flush();
                    cout << "query:# " << queryCounter << "--" << latency / counter << "us\n";
                    
                   
                }
                
            }
            
        }
       
        cout << "dummies# "  << dummies << endl;
        GetP_S("S", result_pair);
        QSize_file1.close();
        latency_file1.close();
        QSize_file2.close();
        latency_file2.close();
        cout << "we done\n";

    }

    
    void sendToCid(const size_t& id, const string& label_star, const string& value,  vector<string>& result, std::ofstream* file, const bool& flag, std::chrono::time_point < std::chrono::high_resolution_clock> arrial_time) {
        
        if (value == "") {
            cout <<"subtoken: "<< label_star << "\nSending to: " << id << "\nresponse: " << "!Empty! " << "\n";
            this_thread::sleep_for(std::chrono::milliseconds(3600 * 1000));
        }
        //cout << "subtoken: " << label_star <<"\nSending to: " << id << "\nresponse: " << value << "\n";
        if (flag) {

            //Remove this condition when using the BaseLine Variations for calculating latency
            if (label_star != "alpha") {
                std::chrono::time_point<std::chrono::high_resolution_clock> end;
                end = std::chrono::high_resolution_clock::now();
                *file << id << "," << std::chrono::duration_cast<std::chrono::microseconds>(end - arrial_time).count() << "\n";
            }
           // cout << "Sending to " << id << ": " << "UPT\n"; 
        }
    }

    void  GetP_S(const string& label_star, pair<string, nullptr_t>& result_pair) {      
        ldx.LDX::Get(label_star, result_pair.first);
    }
    void set_responseFlag() {
        response_flag = true;
    }

    std::vector<string> getKeys() {
        std::vector<string> keys;
        keys.reserve(DXst.size());
        for (const auto& pair : DXst) {
            if (pair.first != "alpha") keys.push_back(pair.first);
        }
        return keys;
    }
    
};


