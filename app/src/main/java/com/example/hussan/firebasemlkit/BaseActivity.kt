package com.example.hussan.firebasemlkit

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bumptech.glide.Glide
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo
import com.miguelbcr.ui.rx_paparazzo2.entities.FileData
import com.miguelbcr.ui.rx_paparazzo2.entities.size.CustomMaxSize
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_recognition.btnFind
import kotlinx.android.synthetic.main.activity_recognition.btnTakePhoto
import kotlinx.android.synthetic.main.activity_recognition.imageView
import kotlinx.android.synthetic.main.activity_recognition.spiImage
import java.io.IOException
import java.io.InputStream

open class BaseActivity: AppCompatActivity() {

    var selectedImahge: Bitmap? = null
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

                selectedImahge = getBitmapFromAsset(this@BaseActivity, items[position])
                Glide.with(this@BaseActivity).load(selectedImahge).into(imageView)
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
            if (it != null && it.exists()) {
                selectedImahge = BitmapFactory.decodeFile(it.path)
                Glide.with(this).load(it).into(imageView)
                recognitionCallback()
            }
        }
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun getBitmapFromAsset(context: Context, filePath: String): Bitmap? {
        val assetManager = context.assets

        val `is`: InputStream
        var bitmap: Bitmap? = null
        try {
            `is` = assetManager.open(filePath)
            bitmap = BitmapFactory.decodeStream(`is`)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return bitmap
    }

}