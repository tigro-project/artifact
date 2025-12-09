# client

This directory contains the source code for the Tigro client and the benchmark harness used in the experimental evaluation.

## Setup

Update `TIGRO_SERVER_URI` in `benchmark.py` to the URL of the Tigro server, then set up the client using Podman or Docker:

```bash
podman build --tag tigro-client .
```

Then, run the image to run the benchmarks:

```bash
podman run tigro-client
```
