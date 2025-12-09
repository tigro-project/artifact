package com.example.tigro.util.QR
import android.content.Context
import android.hardware.Camera
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.tigro.activities.ScanQrActivity
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer

class CameraPreview(context: Context) : SurfaceView(context), SurfaceHolder.Callback, Camera.PreviewCallback {

    private var camera: Camera? = null
    private var multiFormatReader: MultiFormatReader? = MultiFormatReader()
    private var qrCodeListener: QRCodeListener? = null


    init {
        holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {

      try {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT)
            camera?.setPreviewDisplay(holder)
            camera?.setPreviewCallback(this)
            camera?.startPreview()
            camera?.setDisplayOrientation(90)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // Handle surface changes, if needed
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        camera?.setPreviewCallback(null)
        camera?.stopPreview()
        camera?.release()
        camera = null
    }

    override fun onPreviewFrame(data: ByteArray, camera: Camera) {
        // Convert preview data to BinaryBitmap for QR code scanning
        val size = camera.parameters.previewSize
        val source = PlanarYUVLuminanceSource(data, size.width, size.height, 0, 0, size.width, size.height, false)
        val bitmap = BinaryBitmap(HybridBinarizer(source))

        // Perform QR code scanning using ZXing MultiFormatReader
        try {
            val result = multiFormatReader?.decodeWithState(bitmap)
            if (result != null) {
                // QR code detected, handle the result
                var qrCodeText = result.text.toString()
                qrCodeListener?.onQRCodeDetected(qrCodeText)
            }
        } catch (e: Exception) {
            println("qr error")
            e.printStackTrace()
        }
    }

    fun setQRCodeListener(listener: ScanQrActivity) {
        qrCodeListener = listener
    }
}

