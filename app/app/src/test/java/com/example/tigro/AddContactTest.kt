package com.example.tigro

import com.example.tigro.util.QR.QrCodeDecoder
import com.example.tigro.util.crypto.EdxDriver
import com.example.tigro.util.crypto.KeyManager
import com.example.tigro.util.crypto.keyset.PrivateKeySet
import com.example.tigro.util.crypto.keyset.PublicKeySet
import com.example.tigro.util.crypto.keyset.SecretKeySet
import com.google.protobuf.ByteString
import org.junit.Assert
import org.junit.Test


/**
 * Tests include for:
 *  - PublicKetSet.kt   serialize, deserialize
 *  - SecretKeySet.kt   serialize, deserialize
 *  - KeyManager.kt     public-private keypair generation, shared secret generation
 *  - QrCodeDecoder.kt  decode
 */
class AddContactTest {

    private val kp1 = KeyManager().generateEphemeralEcdhKeySets()
    private val kp2 = KeyManager().generateEphemeralEcdhKeySets()
    private val shared1: SecretKeySet = KeyManager().generateAllSharedSecret(kp1.second, kp2.first)



    @Test
    fun testSerializeDeserializePublicKeySet() {
        val pubSet: PublicKeySet = kp1.first
        val out = pubSet.serialize()
        Assert.assertEquals(pubSet, PublicKeySet.deserialize(out))
    }

    @Test
    fun testSerializeDeserializeSecretKeySet() {
        val out = shared1.serialize()
        Assert.assertEquals(shared1, SecretKeySet.deserialize(out))
    }

    @Test
    fun testQrDecode() {
        val ourPrivate: PrivateKeySet = kp1.second
        val otherQrCode: String = kp2.first.serialize()
        val out = QrCodeDecoder().decode(ourPrivate, otherQrCode)
        Assert.assertEquals(shared1, out)
    }

    @Test
    fun keyManagerTest() {
        val km1 = KeyManager()
        val km2 = KeyManager()
        val kp1 = km1.generateEphemeralEcdhKeySets()
        val kp2 = km2.generateEphemeralEcdhKeySets()

        // Sanity check - public private keys are not equal for kp1 and kp2
        Assert.assertNotEquals(kp1.first, kp2.first)
        Assert.assertNotEquals(kp1.second, kp2.second)

        val shared1 = km1.generateAllSharedSecret(kp1.second, kp2.first)
        val shared2 = km2.generateAllSharedSecret(kp2.second, kp1.first)

        // Assert shared secrets are the same
        Assert.assertEquals(shared1, shared2)

        // Basic encrypt decrypt
        val payload = ByteString.copyFromUtf8("payload")
        val ciphertext = EdxDriver().skeEncrypt(shared1.encryptKey, payload)
        val message = EdxDriver().skeDecrypt(shared2.encryptKey, ciphertext)

        Assert.assertEquals(payload, message)
        Assert.assertNotEquals(payload, ciphertext)
    }
}