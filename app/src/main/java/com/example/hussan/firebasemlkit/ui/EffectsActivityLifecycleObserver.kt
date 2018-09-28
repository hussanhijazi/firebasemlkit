package com.example.hussan.firebasemlkit.ui

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.otaliastudios.cameraview.CameraView

class EffectsActivityLifecycleObserver(private val cameraView: CameraView) : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun startCamera() {
        cameraView.start()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pauseCamera() {
        cameraView.stop()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroyCamera() {
        cameraView.destroy()
    }
}
