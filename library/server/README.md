# server

This directory contains the source code for the Tigro server.

## Setup

The easiest way to run the server is to use Podman or Docker. First, build the server image using the provided `Containerfile`:
```bash
podman build --tag tigro-server .
```

Then, run the server:

```bash
podman run --detach --publish 5000:5000 tigro-server
```

The server will now be accessible at `localhost:5000`.
