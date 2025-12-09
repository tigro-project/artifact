package com.example.tigro.util.crypto.keyset

/**
 * Helper Class, stores ByteArray of Key Set encodings since Keys are not serializable
 * Used by both PublicKeySet and SecretKeySet
 */
@kotlinx.serialization.Serializable
data class KeySetBytes(
    val tokenKeyBytes: ByteArray,
    val encryptKeyBytes: ByteArray
)