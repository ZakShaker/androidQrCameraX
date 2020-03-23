package com.zakshaker.scannerx


import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata


class QrCodesAnalyzer(
    private val onQrCodesDetected: (qrCodes: List<String>) -> Unit
) : ImageAnalysis.Analyzer {

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        imageProxy.image?.let { img ->
            val options = FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE)
                .build()

            val detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options)

            val visionImage =
                FirebaseVisionImage.fromMediaImage(
                    img,
                    degreesToFirebaseRotation(imageProxy.imageInfo.rotationDegrees)
                )

            detector.detectInImage(visionImage)
                .addOnSuccessListener { codes ->
                    codes.mapNotNull { it.rawValue }.let(onQrCodesDetected)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }

        }
    }

    private fun degreesToFirebaseRotation(degrees: Int): Int = when (degrees) {
        0 -> FirebaseVisionImageMetadata.ROTATION_0
        90 -> FirebaseVisionImageMetadata.ROTATION_90
        180 -> FirebaseVisionImageMetadata.ROTATION_180
        270 -> FirebaseVisionImageMetadata.ROTATION_270
        else -> throw Exception("Rotation must be 0, 90, 180, or 270.")
    }

}