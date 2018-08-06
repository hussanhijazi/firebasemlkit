package com.example.hussan.firebasemlkit

import android.util.Log
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlinx.android.synthetic.main.activity_recognition.txtResult


class LabelRecognitionActivity : BaseActivity() {

    companion object {
        var TAG = this.javaClass.canonicalName
    }
    init{
        items = arrayOf("mug.jpg", "mouse.jpg", "ball.jpg")
        recognitionCallback = this::runLabelRecognition
    }

    private fun runLabelRecognition() {
        val image = FirebaseVisionImage.fromBitmap(selectedImage ?: return)
        val detector = FirebaseVision.getInstance()
            .visionLabelDetector
        detector.detectInImage(image)
            .addOnSuccessListener {
                var resultText = ""
                for (label in it) {
                    val text = label.label
                    val entityId = label.entityId
                    val confidence = label.confidence
                     resultText += " $text - $confidence\n"
                }
                txtResult.text = resultText
            }
            .addOnFailureListener {
                it.printStackTrace()
                Log.d(TAG, it.message)
            }

    }

}
