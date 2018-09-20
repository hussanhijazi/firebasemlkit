package com.example.hussan.firebasemlkit

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.hussan.firebasemlkit.extensions.navigate
import kotlinx.android.synthetic.main.activity_main.btnBarcode
import kotlinx.android.synthetic.main.activity_main.btnLabel
import kotlinx.android.synthetic.main.activity_main.btnLandmark
import kotlinx.android.synthetic.main.activity_main.btnText

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLabel.setOnClickListener {
            navigate<LabelRecognitionActivity>()
        }
        btnText.setOnClickListener {
            navigate<TextRecognitionActivity>()
        }

        btnBarcode.setOnClickListener {
            navigate<BarcodeRecognitionActivity>()
        }

        btnLandmark.setOnClickListener {
            navigate<LandmarkRecognitionActivity>()
        }
    }

}
