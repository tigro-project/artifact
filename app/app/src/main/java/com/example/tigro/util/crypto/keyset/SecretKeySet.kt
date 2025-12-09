package com.example.tigro.util.crypto.keyset

import com.example.tigro.AES_ALG
import com.example.tigro.HMAC_ALG
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.Serializable
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@kotlinx.serialization.Serializable
data class SecretKeySet(
    val tokenKey: SecretKey,
    val encryptKey: SecretKey
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
     * NOTE: must regenerate Secret Keys from bytes
     * @returns a new KeySet
     */
    companion object {
        fun deserialize(json: String): SecretKeySet {
            val keySetBytes = Json.decodeFromString<KeySetBytes>(json)

            //FIXME: is this correct algo (think resolved now)
            val tokenKey = SecretKeySpec(keySetBytes.tokenKeyBytes, HMAC_ALG)
            val encryptKey = SecretKeySpec(keySetBytes.encryptKeyBytes, AES_ALG)

            return SecretKeySet(tokenKey, encryptKey)
        }
    }
}

