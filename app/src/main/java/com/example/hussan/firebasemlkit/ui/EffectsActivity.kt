package com.example.hussan.firebasemlkit.ui

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.hussan.firebasemlkit.R
import com.example.hussan.firebasemlkit.face.FaceProcessor
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_effect.cameraView
import kotlinx.android.synthetic.main.activity_effect.graphicOverlay

class EffectsActivity : AppCompatActivity() {

    private val faceProcessor by lazy {
        FaceProcessor(cameraView, graphicOverlay)
    }
    private val rxPermissions by lazy {
        RxPermissions(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_effect)

        rxPermissions
                .request(Manifest.permission.CAMERA)
                .subscribe { granted ->
                    if (granted) {
                        faceProcessor.startProcessing()
                    } else {

                    }
                }
        lifecycle.addObserver(EffectsActivityLifecycleObserver(cameraView))
    }
}
