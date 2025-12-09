package com.example.tigro.util.annotation.SendAnnotation

import com.example.tigro.data.Contact

interface ContactBarFragmentCallback {
    fun onContactSelected(contact: Contact)
    fun onContactDeselected(contact: Contact)
}