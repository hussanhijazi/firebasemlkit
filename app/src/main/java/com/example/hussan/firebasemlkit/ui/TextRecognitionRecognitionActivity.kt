package com.example.hussan.firebasemlkit.ui

import android.content.Intent
import android.util.Log
import android.view.View
import com.example.hussan.firebasemlkit.extensions.toast
import com.example.hussan.firebasemlkit.views.TextGraphic
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentText
import com.google.firebase.ml.vision.text.FirebaseVisionText
import kotlinx.android.synthetic.main.activity_recognition.graphicOverlay
import kotlinx.android.synthetic.main.activity_recognition.progressBar
import kotlinx.android.synthetic.main.activity_recognition.txtResult

class TextRecognitionRecognitionActivity : BaseRecognitionActivity() {

    init {
        items = arrayOf("pucpr.jpg", "utfpr.jpg", "ufpr.jpg",
                "unioeste.jpg", "unioeste_fies.jpg", "pucpr_bolsas.jpg", "fumec.jpg", "doc.jpg")
        recognitionCallback = this::runTextRecognition
    }

    fun runTextRecognition() {

        selectedImage?.let {
            val image: FirebaseVisionImage = FirebaseVisionImage.fromBitmap(it)
            val detector = FirebaseVision.getInstance()
                    .cloudDocumentTextRecognizer
            progressBar.visibility = View.VISIBLE
            detector.processImage(image)
                    .addOnSuccessListener {
                        processTextDocument(it)
                        progressBar.visibility = View.GONE
                    }
                    .addOnFailureListener {
                        progressBar.visibility = View.GONE
                    }

        }
    }

    private fun processTextDocument(texts: FirebaseVisionDocumentText) {
        val blocks = texts.blocks
        if (blocks.size == 0) {
            toast("No text found")
            return
        }
        var resultText = ""
        graphicOverlay.clear()

        for (i in blocks.indices) {
            val lines = blocks[i].paragraphs
            for (j in lines.indices) {
                val words = lines[j].words
                for (k in words.indices) {
                    resultText += " ${words[k].text}"
                }
                resultText += "\n"
            }
        }
        Log.d("h2", resultText)
        val intent = Intent(this, ShowTextsActivity::class.java)
        intent.putExtra("text", resultText)
        startActivity(intent)
    }


    private fun processText(texts: FirebaseVisionText) {
        val blocks = texts.textBlocks
        if (blocks.size == 0) {
            toast("No text found")
            return
        }
        var resultText = ""
        graphicOverlay.clear()

        for (i in blocks.indices) {
            val lines = blocks[i].lines
            for (j in lines.indices) {
                val words = lines[j].elements
                for (k in words.indices) {
                    resultText += " ${words[k].text}"
                    val cloudDocumentTextGraphic = TextGraphic(graphicOverlay,
                            words[k])
                    graphicOverlay.add(cloudDocumentTextGraphic)
                }
                resultText += "\n"
            }
        }
        txtResult.text = resultText
    }

}
