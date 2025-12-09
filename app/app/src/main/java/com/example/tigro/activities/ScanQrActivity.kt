package com.example.tigro.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.tigro.R
import com.example.tigro.SHARED_KEYSET_INTENT
import com.example.tigro.util.crypto.KeyManager
import com.example.tigro.util.crypto.keyset.PublicKeySet
import com.example.tigro.util.crypto.keyset.PrivateKeySet
import com.example.tigro.util.crypto.keyset.SecretKeySet
import com.example.tigro.util.QR.CameraPreview
import com.example.tigro.util.QR.QRCodeListener
import com.example.tigro.util.QR.QrCodeDecoder
import com.example.tigro.util.QR.QrCodeGenerator

/**
 * Comes from: MainActivity
 * Leads to: AddContactActivity (nextBtn)
 *
 * uses zxing to scan for another user's tigro QR code. Simultaneously generates a tigro QR code
 * for the user, which is displayed below the camera preview
 */
class ScanQrActivity : AppCompatActivity(), QRCodeListener {

    private lateinit var cameraPreviewContainer: FrameLayout
    private lateinit var qrCodeImageView: ImageView
    private lateinit var nextBtn: Button
    private lateinit var backBtn: Button

    private lateinit var ourPublicKeySet: PublicKeySet          // Public values
    private lateinit var ourSecretKeySet: PrivateKeySet         // Secret values
    private lateinit var sharedKeySet: SecretKeySet             // Shared secrets from DH key exchange

    private val CAMERA_PERMISSION_REQUEST = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qr)

        // Check camera permission and request if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST)
        } else {
            setupCameraAndPreview()
        }

        // The user's QR code
        qrCodeImageView = findViewById(R.id.qrCodeImageView)
        var qrGenerator = QrCodeGenerator(qrCodeImageView)

        var genKeys = KeyManager().generateEphemeralEcdhKeySets()
        ourPublicKeySet = genKeys.first
        ourSecretKeySet = genKeys.second
        qrGenerator.generateQRCode(ourPublicKeySet.serialize())

        nextBtn = findViewById(R.id.nextBtn)
        nextBtn.isEnabled = false
        nextBtn.setOnClickListener { nextBtnClicked() }

        backBtn = findViewById(R.id.backBtn)
        backBtn.setOnClickListener { backBtnClicked() }

        cameraPreviewContainer = findViewById(R.id.cameraPreviewContainer)

    }

    private fun setupCameraAndPreview() {
        cameraPreviewContainer = findViewById(R.id.cameraPreviewContainer)
        val cameraPreview = CameraPreview(this)
        cameraPreviewContainer.addView(cameraPreview)
        cameraPreview.setQRCodeListener(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupCameraAndPreview()
            } else {
                Toast.makeText(this, "Must enable camera for QR scanning", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }

    override fun onQRCodeDetected(qrCodeText: String) {
        println("CALLBACK QR code: $qrCodeText")
        try {
            sharedKeySet = QrCodeDecoder().decode(ourSecretKeySet, qrCodeText)
            nextBtn.backgroundTintList = resources.getColorStateList(R.color.purple_500)
            nextBtn.isEnabled = true
        } catch (e: Exception) {
            // TODO: not a tigro QR, handle accordingly
        }
    }

    /**
     * Next button will not be enabled until a QR is scanned
     *
     * Switches activity to AddContactActivity, with sharedSecret intent passed in
     */
    private fun nextBtnClicked() {
        println("next btn clicked")
        var intent = Intent(this, AddContactActivity::class.java)
        intent.putExtra(SHARED_KEYSET_INTENT, sharedKeySet)
        startActivity(intent)
    }

    private fun backBtnClicked() {
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}