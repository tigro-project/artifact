package com.example.tigro.util.Server

import android.util.Log
import com.example.tigro.util.crypto.EdxDriver
import com.example.tigro.util.crypto.keyset.SecretKeySet
import com.google.protobuf.ByteString

/**
 * Local implementation of the Tigro Server - object allows singleton pattern
 * Note: does not persist, since dataDictionary is stored in the object
 */
object LocalTigroServerProxy : ITigroServerProxy {

    private val dataDictionary: MutableMap<Long, MutableMap<ByteString, ByteString>> = mutableMapOf()
    private var edxDriver: EdxDriver = EdxDriver()

    override fun sendHelloRequest(data: String): String {
        return "Local Server reads: $data"
    }

    override fun sendDropMailRequest(label: String, mail: String, symmetricKeySet: SecretKeySet) {
        val poid = 0L
        val tkn = edxDriver.pibaseToken(symmetricKeySet.tokenKey, ByteString.copyFromUtf8(label))

        val innerDictionary = dataDictionary.getOrPut(poid) { mutableMapOf() }
        innerDictionary[tkn] = ByteString.copyFromUtf8(mail)

        Log.d("jdebug","Drop mail request added to data dictionary: Label='$label', Mail='$mail'")
    }

    override fun sendGetMailRequest(label: String, symmetricKeySet: SecretKeySet): String {
        val poid = 0L
        val tkn = edxDriver.pibaseToken(symmetricKeySet.tokenKey, ByteString.copyFromUtf8(label))

        val innerDictionary = dataDictionary[poid] ?: throw IllegalArgumentException("No data found for POID: $poid")
        return innerDictionary[tkn]?.toStringUtf8() ?: throw IllegalArgumentException("No mail found for label: $label")
    }

    override fun sendDeleteMailRequest(label: String, symmetricKeySet: SecretKeySet) {
        val poid = 0L
        val tkn = edxDriver.pibaseToken(symmetricKeySet.tokenKey, ByteString.copyFromUtf8(label))

        val innerDictionary = dataDictionary[poid] ?: throw IllegalArgumentException("No data found for POID: $poid")
        innerDictionary.remove(tkn)
    }
}