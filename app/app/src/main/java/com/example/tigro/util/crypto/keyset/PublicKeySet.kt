package com.example.tigro.util.crypto.keyset

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import java.io.Serializable


/**
 * Stores keys needed to communicate with tigro server
 *
 */
@kotlinx.serialization.Serializable
data class PublicKeySet(
    val tokenKey: PublicKey,
    val encryptKey: PublicKey
): Serializable {

    /**
     * Serialize the object into JSON. Returns a string
     * NOTE: encodes as KeySetBytes since Keys are not serializable
     */
    fun serialize(): String {
        val tokenKeyBytes = tokenKey.encoded
        val encryptKeyBytes = encryptKey.encoded

        return Json.encodeToString(KeySetBytes(tokenKeyBytes, encryptKeyBytes))
    }

    /**
     * Deserialize on a companion object. For example, KeySet.deserialize("some text")
     * NOTE: must regenerate Public Keys from bytes
     * @returns a new KeySet
     */
    companion object {
        fun deserialize(json: String): PublicKeySet {
            val keySetBytes = Json.decodeFromString<KeySetBytes>(json)

            val keyFactory = KeyFactory.getInstance("EC")
            val tokenKey = keyFactory.generatePublic(X509EncodedKeySpec(keySetBytes.tokenKeyBytes))
            val encryptKey = keyFactory.generatePublic(X509EncodedKeySpec(keySetBytes.encryptKeyBytes))

            return PublicKeySet(tokenKey, encryptKey)
        }
    }
}