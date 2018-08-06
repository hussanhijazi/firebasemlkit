package com.example.hussan.firebasemlkit

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.example.hussan.firebasemlkit.extensions.getBitmapFromAsset
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo
import com.miguelbcr.ui.rx_paparazzo2.entities.FileData
import com.miguelbcr.ui.rx_paparazzo2.entities.size.CustomMaxSize
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_recognition.btnFind
import kotlinx.android.synthetic.main.activity_recognition.btnTakePhoto
import kotlinx.android.synthetic.main.activity_recognition.imageView
import kotlinx.android.synthetic.main.activity_recognition.spiImage

open class BaseActivity: AppCompatActivity() {

    var selectedImage: Bitmap? = null
    var items = emptyArray<String>()
    lateinit var recognitionCallback: () -> Any

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

                selectedImage = this@BaseActivity.getBitmapFromAsset(items[position])
                Glide.with(this@BaseActivity).load(selectedImage).into(imageView)
                recognitionCallback()
            }

        }

        btnFind.setOnClickListener {
            recognitionCallback()
        }

        btnTakePhoto.setOnClickListener {
            takePhoto()
        }
    }


    fun takePhoto() {
        RxPaparazzo.single(this)
            .size(CustomMaxSize(600))
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
                recognitionCallback()
            }
        }
    }

}
