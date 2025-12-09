package com.example.tigro.util.Server
import com.example.tigro.util.crypto.EdxDriver
import com.example.tigro.util.crypto.keyset.SecretKeySet
import com.google.protobuf.ByteString
import io.grpc.ManagedChannel
import tigro.Tigro
import tigro.TigroServiceGrpc

class GrpcTigroServerProxy(chnl: ManagedChannel) : ITigroServerProxy {

    private var channel: ManagedChannel = chnl
    private var stub: TigroServiceGrpc.TigroServiceBlockingStub = TigroServiceGrpc.newBlockingStub(channel)
    private var edxDriver: EdxDriver = EdxDriver()

    override fun sendHelloRequest(data: String): String {
        // TODO: figure out logging with tests
//        return try {
            val request = Tigro.HelloRequest.newBuilder()
                .setData(data)
                .build()

            println("hello request")
            val response: Tigro.HelloReply = stub.hello(request)
            println("hello request finished")
//            Log.d("gRPC", "Received gRPC response: $response")
            return response.data
//        } catch (e: Exception) {
//            // Log.e("gRPC", "Error in gRPC call", e)
//            // Handle the exception or return a default value
//            "Error in gRPC call, $e"
//        }
    }

    override fun sendDropMailRequest(label: String, mail: String, symmetricKeySet: SecretKeySet) {
        val poid = 0L
        val tkn = edxDriver.pibaseToken(symmetricKeySet.tokenKey, ByteString.copyFromUtf8(label))
        val encMail = edxDriver.skeEncrypt(symmetricKeySet.encryptKey, ByteString.copyFromUtf8(mail))

        val request = Tigro.DropMailRequest.newBuilder()
            .setPoid(poid)
            .setLabel(tkn)
            .setMail(encMail)
            .build()

        val response: Tigro.DropMailReply = stub.dropMail(request)
        // Don't return
    }

    override fun sendGetMailRequest(label: String, symmetricKeySet: SecretKeySet): String {
        val poid = 0L
        val tkn = edxDriver.pibaseToken(symmetricKeySet.tokenKey, ByteString.copyFromUtf8(label))

        val request = Tigro.GetMailRequest.newBuilder()
            .setPoid(poid)
            .setLabel(tkn)
            .build()

        val response: Tigro.GetMailReply = stub.getMail(request)
        val rawMail = response.mail
        // FIXME: is this expected behavior?
        if (rawMail == null) {
            throw IllegalStateException("Received empty mail")
        }
        val decMail: ByteString = edxDriver.skeDecrypt(symmetricKeySet.encryptKey, rawMail)
        return decMail.toStringUtf8()
    }

    override fun sendDeleteMailRequest(label: String, symmetricKeySet: SecretKeySet) {
        val poid = 0L
        val tkn = edxDriver.pibaseToken(symmetricKeySet.tokenKey, ByteString.copyFromUtf8(label))

        val request = Tigro.DeleteMailRequest.newBuilder()
            .setPoid(poid)
            .setLabel(tkn)
            .build()

        val response: Tigro.DeleteMailReply = stub.deleteMail(request)
        // Don't return
    }

}