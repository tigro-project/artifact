package com.example.tigro.util.annotation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import java.io.ByteArrayOutputStream

class AnnotationImageManager {

    fun createGrayPlaceholderImage(): ByteArray {
        val bitmap = Bitmap.createBitmap(500, 400, Bitmap.Config.ARGB_8888)
        bitmap.eraseColor(Color.GRAY) // Fill with gray color
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    fun updateAnnotationBarImage(imageView: ImageView, imageData: ByteArray?) {
        if (imageData != null && imageData.isNotEmpty()) {
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
            imageView.setImageBitmap(bitmap)
            imageView.visibility = View.VISIBLE
        } else {
            imageView.visibility = View.GONE
        }
    }
}