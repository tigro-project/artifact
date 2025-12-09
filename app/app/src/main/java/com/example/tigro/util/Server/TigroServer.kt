package com.example.tigro.util.Server

import com.google.protobuf.ByteString
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import java.nio.ByteBuffer

// FIXME: Refuses to connect, related to no TLS?
private val channel: ManagedChannel = ManagedChannelBuilder.forAddress("10.0.2.2", 50051)
    .usePlaintext() // For testing only. Use TLS in production.
    .build()


/**
 * Wrapper around different servers for interchangeability. To change, edit the delegation (i.e. ""by ...)
 * Options:
 *  - LocalTigroServerProxy : Completely local implementation, uses a dictionary
 *  - GrpcTigroServerProxy(<channel>) : Uses gRPC protocol. Takes in a channel
 */
object TigroServer : ITigroServerProxy by LocalTigroServerProxy {
}
