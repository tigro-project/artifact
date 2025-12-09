package com.example.tigro.util.crypto.keyset

import java.security.PrivateKey

data class PrivateKeySet(
    val tokenKey: PrivateKey,
    val encryptKey: PrivateKey
)