package com.zakshaker.scannerx


import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage


class QrCodesAnalyzer(
    private val onQrCodesDetected: (qrCodes: List<String>) -> Unit
) : ImageAnalysis.Analyzer {

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        imageProxy.image?.let { img ->
            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()

            val detector = BarcodeScanning.getClient(options)

            val visionImage =
                InputImage.fromMediaImage(
                    img,
                    imageProxy.imageInfo.rotationDegrees
                )

            detector.process(visionImage)
                .addOnSuccessListener { codes ->
                    codes.mapNotNull { it.rawValue }.let(onQrCodesDetected)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }

        }
    }
}