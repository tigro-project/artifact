# exh

This directory contains the EXH experiments. EXH implements a secure data access simulation using Path ORAM and latency measurements.

## Directory Structure

- **client/**: Contains the client-side code (`ClientPerf.cpp`, etc.).
- **sserver/**: Contains the server-side code (`Server.cpp`).
- **SynVHQ/**: Shared logic and ORAM implementation.
- **MM/MM/**: **[REQUIRED]** Directory containing the input `.json` data files.
   - The system will look for `.json` files here to populate the ORAM.

## How to Run

1.  **Build**: Compile the `sserver` and `client` projects using Visual Studio.
2.  **Environment**: 
    - Both Client and Server **MUST** be run from the **SAME** working directory.
    - This is because they exchange ORAM state via a shared file named `serialized_data.bin`.
3.  **Execution Order**:
    1.  **Run Client (`client.exe`)**: The client reads the data from `MM/MM`, initializes the ORAM tree, and writes the encrypted tree to `serialized_data.bin`.
    2.  **Run Server (`sserver.exe`)**: The server starts, reads `serialized_data.bin` (created by the client), and listens on port `8900`.
    3.  **Client Connection**: The client connects to the server and begins the experiment (latency measurements).

## Data Paths & Debugging

- **Input Data**: stored in `MM/MM` relative to the execution directory.
- **Shared State**: `serialized_data.bin`.
    - **Client** writes this file.
    - **Server** reads this file.
    - If the server crashes with "Cannot open file", verify that the Client finished its initialization step and that both executables are running in the same folder (or have access to the same `serialized_data.bin`).

## Understanding the Queues (Q, Q1, Q2)

The system uses a multi-queue approach to handle concurrent requests from different threads/clients while maintaining order and thread safety.

- **Q (Global Ordering Queue)**: 
    - **Type**: `queue<size_t>`
    - **Purpose**: Acts as a scheduler/synchronizer. It stores the *ID* of the queue (0 or 1) that has the next request to be processed. 
    - **Why?**: It ensures that the main processing loop knows exactly which data queue to check next, preserving the global arrival order of requests from different threads.

- **Q1 (Data Queue 1)**:
    - **Type**: `queue<tuple<...>>`
    - **Purpose**: Stores the actual request data (Label, Response Length, Timestamp etc.) for **Thread/Client ID 0**.
    - When Thread 0 generates a request, it pushes the data to `Q1` and pushes `0` to `Q`.

- **Q2 (Data Queue 2)**:
    - **Type**: `queue<tuple<...>>`
    - **Purpose**: Stores the actual request data for **Thread/Client ID 1** (and any other non-zero IDs).
    - When Thread 1 generates a request, it pushes the data to `Q2` and pushes `1` to `Q`.

### Difference
- **Q** contains only metadata (queue IDs).
- **Q1 & Q2** contain the actual payload/request details.
- This design allows separate threads to lock their own specific data queues (`dataMutex1` for Q1, `dataMutex2` for Q2) while synchronizing on the global order via `dataMutexGlobal`.

## Experiment Logic & Configuration

The `client/ClientPerf.cpp` file contains the main experiment loop which automatically sweeps through specific parameters:

1.  **Lambda (Arrival Rate)**: The `j` loop iterates through `4` and `8`.
    *   `for (int j = 4; j < 16; j *= 2)`
    *   This controls the request arrival rate (simulated load).
2.  **Dataset Size**: The `i` loop sets the dataset size to $2^{14}$ (16,384 keys).
    *   `for (int i = 14; i < 16; i += 2)`
    *   This determines the amount of data populated in the ORAM.
3.  **CSV Output Generation**:
    *   For each combination (2 runs total: Lambda=4, Lambda=8), the client spawns threads and measures performance.
    *   This generates **4 CSV files** in total (2 runs × 2 log types):
        - `QueueSize...csv`: Tracks the internal queue sizes over time.
        - `latency_data...csv`: Records the latency for each request.

To adjust these parameters (e.g., to test larger datasets or different lambdas), modify the loops in the `main()` function of `client/ClientPerf.cpp`.

## Executing the Experiment

1.  Ensure `MM/MM` has adequate data (json files).
2.  **Order Matters**:
    - Start `sserver.exe` (Release build).
    - Start `client.exe` (Release build).
3.  The client will automatically execute the loops described above.
4.  After completion, look for the generated `.csv` files in your working directory.

## Analysis Scripts

The project includes Python scripts to analyze and plot the experiment results:

### 1. `SynVHQ/x64/Release/QueStat.py` (Queue Statistics)
- **Purpose**: processing `QueueSize` CSV files to calculate weighted queue metrics.
- **Input**: Looks for files matching `*Queue*.csv` in the scanned directory.
- **Output**: Prints weighted mean, median, 75th percentile, max, and standard deviation of the queue size. It weights the queue size by the time (`DeltaTime`) spent in that state.

### 2. `SynVHQ/x64/Release/LatencyStat.py` (Latency Statistics)
- **Purpose**: Processing `latency_data` CSV files to calculate weighted latency metrics.
- **Input**: Looks for files matching `latency_data*.csv`.
- **Output**: Prints weighted mean, median, 75th percentile, max, and standard deviation of the latency. It weights the latency by the `ResponseLength` (size of the response).



