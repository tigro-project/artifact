package com.example.tigro.util.overwrite

// This string is the (practical) max length string to overwrite any data stored in
// any editText components
const val MAX_LENGTH_STRING = "OVERWRITTEN"


interface OverwriteListener {
    fun onRequestOverwrite()
}