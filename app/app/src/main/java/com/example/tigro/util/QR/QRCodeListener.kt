package com.example.tigro.util.QR

interface QRCodeListener {
    fun onQRCodeDetected(qrCodeText: String)
}