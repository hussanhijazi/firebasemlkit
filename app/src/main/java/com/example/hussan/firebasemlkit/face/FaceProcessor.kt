package com.example.hussan.firebasemlkit.face

import com.example.hussan.firebasemlkit.views.FaceOverlay
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.otaliastudios.cameraview.CameraView

class FaceProcessor(private val cameraView: CameraView, private val graphicOverlay: FaceOverlay) {
    private val options = FirebaseVisionFaceDetectorOptions.Builder()
            .setLandmarkType(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
            .build()

    private val detector = FirebaseVision.getInstance().getVisionFaceDetector(options)

    fun startProcessing() {

        // Getting frames from camera view
        cameraView.addFrameProcessor { frame ->
            if (frame.size != null) {
                val rotation = frame.rotation / 90
                if (rotation / 2 == 0) {
                    graphicOverlay.previewWidth = cameraView.previewSize?.width
                    graphicOverlay.previewHeight = cameraView.previewSize?.height
                } else {
                    graphicOverlay.previewWidth = cameraView.previewSize?.height
                    graphicOverlay.previewHeight = cameraView.previewSize?.width
                }
                // Build a image meta data object
                val metadata = FirebaseVisionImageMetadata.Builder()
                        .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                        .setWidth(frame.size.width)
                        .setHeight(frame.size.height)
                        .setRotation(rotation)
                        .build()
                val firebaseVisionImage = FirebaseVisionImage.fromByteArray(frame.data, metadata)

                detector.detectInImage(firebaseVisionImage).addOnSuccessListener { faceList ->
                    if (faceList.size > 0) {
                        val face = faceList[0]
                        graphicOverlay.face = face
                    } else {
                        graphicOverlay.face = null
                    }
                }
            } else {
                graphicOverlay.face = null
            }
        }
    }
}
