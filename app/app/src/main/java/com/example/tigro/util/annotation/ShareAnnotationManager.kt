package com.example.tigro.util.annotation

import java.security.MessageDigest

class ShareAnnotationManager {

    fun hashInput(input: String): String {
        // taken from https://gist.github.com/lovubuntu/164b6b9021f5ba54cefc67f60f7a1a25
        val bytes = input.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }
}