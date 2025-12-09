package com.example.tigro

import com.example.tigro.util.Server.GrpcTigroServerProxy
import com.example.tigro.util.Server.TigroServer
import com.example.tigro.util.crypto.KeyManager
import com.example.tigro.util.crypto.keyset.SecretKeySet
import com.google.protobuf.ByteString
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tigro.Tigro
import tigro.TigroServiceGrpc



class NetworkingTest {

    private lateinit var channel: ManagedChannel
    private lateinit var stub: TigroServiceGrpc.TigroServiceBlockingStub
    private val proxy = TigroServer

    private val kp1 = KeyManager().generateEphemeralEcdhKeySets()
    private val kp2 = KeyManager().generateEphemeralEcdhKeySets()
    private val shared1: SecretKeySet = KeyManager().generateAllSharedSecret(kp1.second, kp2.first)


    @Before
    fun setUp() {
        channel = ManagedChannelBuilder.forAddress("localhost", 50051)
            .usePlaintext() // For testing only. Use TLS in production.
            .build()

        stub = TigroServiceGrpc.newBlockingStub(channel)
    }

    @After
    fun tearDown() {
        channel.shutdownNow()
    }

//    /**
//     * Tests gRPC server hello request with both string and ByteString payloads
//     */
//    @Test
//    fun testHelloRequestSimple() {
//        val expectedMessageString: String = "Hello from John 11/9/23!"
//        val expectedMessageBytes: ByteString = ByteString.copyFrom(byteArrayOf(65, 66, 67))
//        // Create a HelloRequest message
//        val request = Tigro.HelloRequest.newBuilder()
//            .setData(expectedMessageString)
//            .build()
//
//        val requestBytes = Tigro.HelloRequest.newBuilder()
//            .setDataBytes(expectedMessageBytes)
//            .build()
//
//        // Send the request and get the response
//        val response: Tigro.HelloReply = stub.hello(request)
//        val responseBytes: Tigro.HelloReply = stub.hello(requestBytes)
//
//        Assert.assertEquals(expectedMessageString, response.data)
//        Assert.assertEquals(expectedMessageBytes, responseBytes.dataBytes)
//    }
//
//    @Test
//    fun testGetMailRequestSimple() {
//        val request = Tigro.GetMailRequest.newBuilder()
//            .setPoid(1234)
//            .build()
//
//        val response: Tigro.GetMailReply = stub.getMail(request)
//        println(response.mail)
//    }
//
//    @Test
//    fun testDropMailRequestSimple() {
//        val labelBytes: ByteString = ByteString.copyFrom(byteArrayOf(65, 66, 67))
//        val mailBytes: ByteString = ByteString.copyFrom(byteArrayOf(65, 66, 67))
//
//        val request = Tigro.DropMailRequest.newBuilder()
//            .setPoid(12)
//            .setLabel(labelBytes)
//            .setMail(mailBytes)
//            .build()
//
//        val response: Tigro.DropMailReply = stub.dropMail(request)
//        println(response)
//    }
//
//    @Test
//    fun testHelloProxy() = runBlocking {
//        println(proxy.sendHelloRequest("hi"))
//    }
//
//    // Note: Only works if gRPC server is running, maybe use TigroServer instead for testing?
//    @Test
//    fun testDropAndGetMail() {
//        val label = ByteString.copyFromUtf8("test")
//        val message = ByteString.copyFromUtf8("dropmail test")
//        proxy.sendDropMailRequest(label, message, shared1)
//
//        var gr = proxy.sendGetMailRequest(label, shared1)
//        println("getMail contents: '${gr.toStringUtf8()}'")
//        Assert.assertEquals(gr, message)
//
//        proxy.sendDeleteMailRequest(label, shared1)
//        Assert.assertThrows(Exception::class.java) {
//            gr = proxy.sendGetMailRequest(label, shared1)
//        }
//    }
//
//    @Test
//    fun testGetOnly() {
//        // This test fails on purpose, remove later
//        val label = ByteString.copyFromUtf8("test")
//
//        var gr = proxy.sendGetMailRequest(label, shared1)
//        println("getMail contents: '${gr.toStringUtf8()}'")
//    }

    // FIXME: Refuses to connect, related to no TLS?
    @Test
    fun testHelloRemoteUnit() {
        val local = "10.0.2.2"
        val remote2 = "128.148.36.56"

        val channel2: ManagedChannel = ManagedChannelBuilder.forAddress(remote2, 50051)
            .usePlaintext() // For testing only. Use TLS in production.
            .build()

        val proxy2: GrpcTigroServerProxy = GrpcTigroServerProxy(channel2)
        val payload = "hello world - grpc"
        println("running!")
        val response = proxy2.sendHelloRequest(payload)
        Assert.assertEquals(payload, response)
    }
}