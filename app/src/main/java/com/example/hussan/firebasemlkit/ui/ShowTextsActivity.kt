package com.example.hussan.firebasemlkit.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.hussan.firebasemlkit.R
import kotlinx.android.synthetic.main.activity_texts.txtText

class ShowTextsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_texts)

        val text = intent.getStringExtra("text")

        txtText.text = text

    }
}
