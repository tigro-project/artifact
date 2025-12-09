package com.example.tigro.util.QR

import android.graphics.Bitmap
import android.graphics.Color
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter

/**
 * Generates a QR code from a string - does not do any cryptographic operations
 */
class QrCodeGenerator(imageView: ImageView) {
    private var qrCodeImageView: ImageView = imageView;

    // TODO: add header which can identify user, unique ID
    fun generateQRCode(content: String) {
        val size = 500 // Specify the desired size of the QR code

        val qrCodeWriter = QRCodeWriter()
        try {

            val bitMatrix: BitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, size, size)
            val qrCodeBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)

            for (x in 0 until size) {
                for (y in 0 until size) {
                    qrCodeBitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.GREEN else Color.WHITE)
                }
            }

            this.qrCodeImageView.setImageBitmap(qrCodeBitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }
}