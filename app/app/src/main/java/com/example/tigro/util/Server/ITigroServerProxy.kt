package com.example.tigro.util.Server

import com.example.tigro.util.crypto.keyset.SecretKeySet
import com.google.protobuf.ByteString

interface ITigroServerProxy {
    fun sendHelloRequest(data: String): String

    fun sendDropMailRequest(label: String, mail: String, symmetricKeySet: SecretKeySet)

    fun sendGetMailRequest(label: String, symmetricKeySet: SecretKeySet): String

    fun sendDeleteMailRequest(label: String, symmetricKeySet: SecretKeySet)
}