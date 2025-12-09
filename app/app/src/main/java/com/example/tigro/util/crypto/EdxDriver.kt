package com.example.tigro.util.crypto

import com.google.protobuf.ByteString
import java.nio.ByteBuffer
import java.security.Key
import java.security.SecureRandom
import java.security.spec.AlgorithmParameterSpec
import javax.crypto.Cipher
import javax.crypto.Mac
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec


private const val HMAC_ALGORITHM = "HmacSHA256"
private const val SKE_ALGORITHM = "AES/GCM/NoPadding";
private const val SKE_IV_LENGTH = 12;
private const val GCM_TAG_LENGTH = 128;

class EdxDriver {

    private var secureRandom: SecureRandom = SecureRandom();

    fun pibaseToken(key: Key, label: ByteString): ByteString {
//        val secretKeySpec = SecretKeySpec(key.encoded, HMAC_ALGORITHM)
        val mac = Mac.getInstance(HMAC_ALGORITHM)
        mac.init(key)
        val token = mac.doFinal(label.toByteArray())
        return ByteString.copyFrom(token)
    }

    fun skeEncrypt(key: Key, plaintext: ByteString): ByteString {
        val ivBytes = ByteArray(SKE_IV_LENGTH)
        secureRandom.nextBytes(ivBytes)

        val cipher = Cipher.getInstance(SKE_ALGORITHM)
        val parameterSpec = GCMParameterSpec(GCM_TAG_LENGTH, ivBytes)

        cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec)

        val cipherText = cipher.doFinal(plaintext.toByteArray())

        val byteBuffer = ByteBuffer.allocate(ivBytes.size + cipherText.size)
        byteBuffer.put(ivBytes)
        byteBuffer.put(cipherText)
        return ByteString.copyFrom(byteBuffer.array())
    }

    fun skeDecrypt(key: Key, ciphertext: ByteString): ByteString {
        val cipher = Cipher.getInstance(SKE_ALGORITHM)
        val cipherByte: ByteArray = ciphertext.toByteArray()

        val skeIV: AlgorithmParameterSpec = GCMParameterSpec(GCM_TAG_LENGTH, cipherByte, 0, SKE_IV_LENGTH)
        cipher.init(Cipher.DECRYPT_MODE, key, skeIV)

        // TODO: separate iv+ciphertext
        val sep = cipherByte.copyOfRange(SKE_IV_LENGTH, cipherByte.size)

        val plainText = cipher.doFinal(sep)
        return ByteString.copyFrom(plainText)

    }
}