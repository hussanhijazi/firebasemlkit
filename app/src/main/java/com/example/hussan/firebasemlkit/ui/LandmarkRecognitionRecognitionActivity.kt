package com.example.hussan.firebasemlkit.ui

import com.example.hussan.firebasemlkit.extensions.toast
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlinx.android.synthetic.main.activity_recognition.txtResult

class LandmarkRecognitionRecognitionActivity : BaseRecognitionActivity() {

    companion object {
        var TAG = this.javaClass.canonicalName
    }

    init {
        items = arrayOf("baalbeck.jpg", "iguazu_falls.jpg", "friendship_bridge.png")
        recognitionCallback = this::runLabelRecognition
    }

    private fun runLabelRecognition() {
        val image = FirebaseVisionImage.fromBitmap(selectedImage ?: return)
        val detector = FirebaseVision.getInstance()
                .visionCloudLandmarkDetector


        detector.detectInImage(image)
                .addOnCompleteListener {
                    //The completion listener returns a list of detected landmarks
                    var landmark = ""
                    for (firebaseVisionLandmarks in it.result) {
                        //get the name of the landmark
                        landmark = firebaseVisionLandmarks.landmark
                        // Multiple locations are possible so loop through them,
                        // e.g., the location of the depicted
                        // landmark and the location the picture was taken.
                        for (location in firebaseVisionLandmarks.locations) {
                            //get the latitude and the longitude of the location
                            val lat = location.latitude
                            val long = location.longitude
                        }
                        //Also extract the accuracy of the detected landmark
                        val confidence = firebaseVisionLandmarks.confidence
                    }

                    txtResult.text = landmark
                }
                .addOnFailureListener {
                    //Failed to retrieve an image
                    toast("Something went wrong")
                }
    }
}
