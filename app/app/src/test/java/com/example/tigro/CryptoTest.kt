package com.example.tigro

import com.example.tigro.util.crypto.EdxDriver
import com.google.protobuf.ByteString
import org.junit.Assert
import org.junit.Test
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/**
 * Tests include for:
 *  - EdxDriver.kt
 */
class CryptoTest {

    val key = getSecretKey()
    val key2 = getSecretKey()

    private fun getSecretKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(128)
        return keyGenerator.generateKey()
    }

    @Test
    fun testPibaseTokenizeDeterministic() {
        val x = EdxDriver().pibaseToken(key, ByteString.copyFromUtf8("test"))
        val y = EdxDriver().pibaseToken(key, ByteString.copyFromUtf8("test"))
        Assert.assertEquals(x, y)
    }

    @Test
    fun testEncryptDecryptSimple() {
        val payload = ByteString.copyFromUtf8("payload")
        val ciphertext = EdxDriver().skeEncrypt(key, payload)
        val message = EdxDriver().skeDecrypt(key, ciphertext)

        Assert.assertEquals(payload, message)
        Assert.assertNotEquals(payload, ciphertext)
    }

    @Test
    fun testEncryptDecryptDifferentKeysErrors() {
        val payload = ByteString.copyFromUtf8("payload")
        val ciphertext = EdxDriver().skeEncrypt(key, payload)
        val ciphertext2 = EdxDriver().skeEncrypt(key2, payload)

        Assert.assertNotEquals(key, key2)
        Assert.assertThrows(Exception::class.java) {
            EdxDriver().skeDecrypt(key2, ciphertext)
        }
        Assert.assertThrows(Exception::class.java) {
            EdxDriver().skeDecrypt(key, ciphertext2)
        }
    }
}