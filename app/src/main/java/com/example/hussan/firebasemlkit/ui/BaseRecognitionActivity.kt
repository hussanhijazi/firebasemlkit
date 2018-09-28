package com.example.hussan.firebasemlkit.ui

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Pair
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.example.hussan.firebasemlkit.R
import com.example.hussan.firebasemlkit.extensions.getBitmapFromAsset
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo
import com.miguelbcr.ui.rx_paparazzo2.entities.FileData
import com.miguelbcr.ui.rx_paparazzo2.entities.size.CustomMaxSize
import com.miguelbcr.ui.rx_paparazzo2.entities.size.OriginalSize
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_recognition.btnFind
import kotlinx.android.synthetic.main.activity_recognition.btnTakePhoto
import kotlinx.android.synthetic.main.activity_recognition.imageView
import kotlinx.android.synthetic.main.activity_recognition.spiImage

open class BaseRecognitionActivity : AppCompatActivity() {

    var selectedImage: Bitmap? = null
    var items = emptyArray<String>()
    lateinit var recognitionCallback: () -> Any
    private var mImageMaxWidth: Int? = null
    private var mImageMaxHeight: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_recognition)

        val adapter = ArrayAdapter(
                this, android.R.layout
                .simple_spinner_dropdown_item, items
        )
        spiImage.adapter = adapter
        spiImage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

                selectedImage = this@BaseRecognitionActivity.getBitmapFromAsset(items[position])

                selectedImage?.let {
                    Glide.with(this@BaseRecognitionActivity).load(selectedImage).into(imageView)
                    selectedImage = resizeBitmap(it)
                }
            }

        }

        btnFind.setOnClickListener {
            recognitionCallback()
        }

        btnTakePhoto.setOnClickListener {
            takePhoto()
        }
    }

    private fun resizeBitmap(it: Bitmap): Bitmap {
        val targetedSize = getTargetedWidthHeight()

        val targetWidth = targetedSize.first
        val maxHeight = targetedSize.second

        val scaleFactor = Math.max(
                it.width.toFloat() / targetWidth.toFloat(),
                it.height.toFloat() / maxHeight.toFloat())

        return Bitmap.createScaledBitmap(
                selectedImage,
                (it.width / scaleFactor).toInt(),
                (it.height / scaleFactor).toInt(),
                true)
    }


    private fun getTargetedWidthHeight(): Pair<Int, Int> {
        val targetWidth: Int
        val targetHeight: Int
        val maxWidthForPortraitMode = getImageMaxWidth()
        val maxHeightForPortraitMode = getImageMaxHeight()
        targetWidth = maxWidthForPortraitMode!!
        targetHeight = maxHeightForPortraitMode!!
        return Pair(targetWidth, targetHeight)
    }

    private fun getImageMaxWidth(): Int? {
        if (mImageMaxWidth == null) {
            mImageMaxWidth = imageView.width
        }

        return mImageMaxWidth
    }

    private fun getImageMaxHeight(): Int? {
        if (mImageMaxHeight == null) {
            mImageMaxHeight = imageView.height
        }

        return mImageMaxHeight
    }

    fun takePhoto() {
        RxPaparazzo.single(this)
                .size(CustomMaxSize(1024))
                .usingCamera()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it.resultCode() == Activity.RESULT_OK) {
                        it.targetUI().loadImage(it.data())
                    }
                }
    }

    private fun loadImage(fileData: FileData) {
        fileData.file?.let {
            if (it.exists()) {
                selectedImage = BitmapFactory.decodeFile(it.path)
                Glide.with(this).load(it).into(imageView)
            }
        }
    }

}
