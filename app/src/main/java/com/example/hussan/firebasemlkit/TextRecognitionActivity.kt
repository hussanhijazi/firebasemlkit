package com.example.hussan.firebasemlkit

import com.example.hussan.firebasemlkit.extensions.toast
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import kotlinx.android.synthetic.main.activity_recognition.txtResult


class TextRecognitionActivity : BaseActivity() {

    init{
        items = arrayOf("ignus.png", "helabs.png", "santos.jpg")
        recognitionCallback = this::runTextRecognition
    }

    fun runTextRecognition() {

        selectedImage?.let{
            val image: FirebaseVisionImage = FirebaseVisionImage.fromBitmap(it)
            val detector = FirebaseVision.getInstance()
                .visionTextDetector

            detector.detectInImage(image)
                .addOnSuccessListener {
                    processText(it)
                }
                .addOnFailureListener {
                }
        }
    }

    private fun processText(texts: FirebaseVisionText) {
        val blocks = texts.blocks
        if (blocks.size == 0) {
            toast("No text found")
            return
        }
        var resultText = ""
        for (i in blocks.indices) {
            val lines = blocks[i].lines
            for (j in lines.indices) {
                val elements = lines[j].elements
                for (k in elements.indices) {
                    resultText += " ${elements[k].text}"
                }
            }
        }
        toast(resultText)
        txtResult.text = resultText
    }

}
