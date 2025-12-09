package com.example.tigro.util.annotation.SetAnnotation

import com.example.tigro.data.Contact
import com.example.tigro.util.annotation.TigroAnnotation
import java.time.LocalDateTime

interface TigroAnnotationListener {
    fun setAnnotation(annotation: TigroAnnotation)
    fun setAnnotationExpiration(expiration: LocalDateTime)
    fun setContactTargetList(targets: List<Contact>)
}