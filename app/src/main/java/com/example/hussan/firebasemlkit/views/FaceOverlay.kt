package com.example.hussan.firebasemlkit.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.example.hussan.firebasemlkit.R
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark
import com.yalantis.ucrop.util.BitmapLoadUtils.transformBitmap

class FaceOverlay(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var face: FirebaseVisionFace? = null
        set(value) {
            field = value
            postInvalidate()
        }

    var previewWidth: Int? = null

    var previewHeight: Int? = null

    private var widthScaleFactor = 1.0f
    private var heightScaleFactor = 1.0f
    private val glassesBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.glasses)
    private val eyeBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.eye)
    private val hairBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.hair)
    private val cheekBitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.star)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val face = face
        val previewWidth = previewWidth
        val previewHeight = previewHeight

        if (face != null && canvas != null && previewWidth != null && previewHeight != null) {
            widthScaleFactor = canvas.width.toFloat() / previewWidth.toFloat()
            heightScaleFactor = canvas.height.toFloat() / previewHeight.toFloat()

            drawGlasses(canvas, face)
            drawHair(canvas, face)
//            drawEye(canvas, face)
//            drawStarCheek(canvas, face)
        }
    }

    private fun drawStarCheek(canvas: Canvas, face: FirebaseVisionFace) {
        val leftCheek = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_CHEEK)
        val rightCheek = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_CHEEK)

        if (leftCheek != null) {
            val rotateCheek = rotateBitmap(face, cheekBitmap)
            val leftEyeDraw = Rect(
                    translateX(leftCheek.position.x).toInt() - 50,
                    translateY(leftCheek.position.y).toInt() - 50,
                    translateX(leftCheek.position.x).toInt() + 50,
                    translateY(leftCheek.position.y).toInt() + 50)
            canvas.drawBitmap(rotateCheek, null, leftEyeDraw, null)
        }

        if (rightCheek != null) {
            val rotateCheek = rotateBitmap(face, cheekBitmap)

            val leftEyeDraw = Rect(
                    translateX(rightCheek.position.x).toInt() - 50,
                    translateY(rightCheek.position.y).toInt() - 50,
                    translateX(rightCheek.position.x).toInt() + 50,
                    translateY(rightCheek.position.y).toInt() + 50)
            canvas.drawBitmap(rotateCheek, null, leftEyeDraw, null)
        }

    }

    private fun drawHair(canvas: Canvas, face: FirebaseVisionFace) {
        val rotatedHair = rotateBitmap(face, hairBitmap)
        val hairRect = translateBoundingBox(face.boundingBox, 100 + Math.abs(face.headEulerAngleZ) * 8)
        canvas.drawBitmap(rotatedHair, null, hairRect, null)
    }

    private fun drawEye(canvas: Canvas, face: FirebaseVisionFace) {
        val leftEye = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EYE)
        val rightEye = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EYE)

        if (leftEye != null && rightEye != null) {
            val rotatedEye = rotateBitmap(face, eyeBitmap)
            val eyeDistance = leftEye.position.x - rightEye.position.x
            val delta = (widthScaleFactor * eyeDistance / 2).toInt()
            val leftEyeDraw = Rect(
                    translateX(leftEye.position.x).toInt() - delta,
                    translateY(leftEye.position.y).toInt() - delta,
                    translateX(leftEye.position.x).toInt(),
                    translateY(leftEye.position.y).toInt())
            canvas.drawBitmap(rotatedEye, null, leftEyeDraw, null)


            val rightEyeDraw = Rect(
                    translateX(rightEye.position.x).toInt() - delta,
                    translateY(rightEye.position.y).toInt() - delta,
                    translateX(rightEye.position.x).toInt(),
                    translateY(rightEye.position.y).toInt())
            canvas.drawBitmap(rotatedEye, null, rightEyeDraw, null)

            drawBox(canvas)
        }
    }

    private fun drawGlasses(canvas: Canvas, face: FirebaseVisionFace) {
        val leftEye = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EYE)
        val rightEye = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EYE)

        if (leftEye != null && rightEye != null) {
            val rotatedGlasses = rotateBitmap(face, glassesBitmap)

            val eyeDistance = leftEye.position.x - rightEye.position.x
            val delta = (widthScaleFactor * eyeDistance / 2).toInt()
            val glassesRect = Rect(
                    translateX(leftEye.position.x).toInt() - delta,
                    translateY(leftEye.position.y).toInt() - delta,
                    translateX(rightEye.position.x).toInt() + delta,
                    translateY(rightEye.position.y).toInt() + delta)
            canvas.drawBitmap(rotatedGlasses, null, glassesRect, null)
//            drawBox(canvas)
        }
    }

    private fun rotateBitmap(face: FirebaseVisionFace, bitmap: Bitmap): Bitmap? {
        val headTilt = face.headEulerAngleZ
        val rotationMatrix = Matrix()
        rotationMatrix.postRotate(headTilt * 1)
        val rotatedGlasses = transformBitmap(bitmap, rotationMatrix)
        return rotatedGlasses
    }

    private fun drawBox(canvas: Canvas) {
        val bounds = face?.boundingBox
        bounds?.let {
            val outlinePaint = Paint().apply {
                color = -0x10000
                strokeWidth = 5f
                style = Paint.Style.STROKE
            }

            canvas.drawRect(translateBoundingBox(bounds), outlinePaint)
        }
    }

    private fun translateBoundingBox(rect: Rect): Rect {
        return translateBoundingBox(rect, 0F)
    }

    private fun translateBoundingBox(rect: Rect, padding: Float): Rect {
        val translated = Rect()
        translated.top = (translateY(rect.top.toFloat()) - padding).toInt()
        translated.bottom = (translateY(rect.bottom.toFloat()) + padding).toInt()
        // swap left and right because of mirroring
        translated.left = (translateX(rect.right.toFloat()) - padding).toInt()
        translated.right = (translateX(rect.left.toFloat()) + padding).toInt()

        return translated
    }

    /**
     * Adjusts the x coordinate from the preview's coordinate system to the view coordinate system.
     */
    private fun translateX(x: Float): Float {
        return width - scaleX(x)
    }

    /**
     * Adjusts the y coordinate from the preview's coordinate system to the view coordinate system.
     */
    private fun translateY(y: Float): Float {
        return scaleY(y)
    }

    /** Adjusts a vertical value of the supplied value from the preview scale to the view scale. */
    private fun scaleX(x: Float): Float {
        return x * widthScaleFactor
    }


    /** Adjusts a vertical value of the supplied value from the preview scale to the view scale. */
    private fun scaleY(y: Float): Float {
        return y * heightScaleFactor
    }
}
