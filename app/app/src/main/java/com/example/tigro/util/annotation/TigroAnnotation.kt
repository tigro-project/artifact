package com.example.tigro.util.annotation

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.Serializable

@kotlinx.serialization.Serializable
data class TigroAnnotation(
    val label: String,
    val imageByteArray: ByteArray = ByteArray(0),
    val contents: String,
    val expirationDate: String,
): Serializable {
    fun serialize(): String {
        return Json.encodeToString(this)
    }

    companion object {
        fun deserialize(json: String): TigroAnnotation {
            return Json.decodeFromString<TigroAnnotation>(json)
        }
    }
}
