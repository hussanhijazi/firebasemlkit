package com.example.hussan.firebasemlkit.ui

import android.util.Log
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlinx.android.synthetic.main.activity_recognition.txtResult


class BarcodeRecognitionRecognitionActivity : BaseRecognitionActivity() {

    init {
        items = arrayOf("barcode.png", "barcode2.jpg", "barcode3.jpg", "qrcode.png")
        recognitionCallback = this::runBarcodeRecognition
    }

    companion object {
        var TAG = this.javaClass.canonicalName
    }

    fun runBarcodeRecognition() {

        selectedImage?.let {
            val image: FirebaseVisionImage = FirebaseVisionImage.fromBitmap(it)
            val detector = FirebaseVision.getInstance()
                    .visionBarcodeDetector

            detector.detectInImage(image)
                    .addOnSuccessListener {
                        Log.d("h2", it.toString())
                        for (barcode in it) {
                            val bounds = barcode.boundingBox
                            val corners = barcode.cornerPoints
                            val rawValue = barcode.rawValue
                            val valueType = barcode.valueType

                            Log.d(TAG, valueType.toString())
                            txtResult.text = barcode.displayValue

                        }
                    }
                    .addOnFailureListener {
                    }
        }
    }


}
