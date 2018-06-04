package com.example.hussan.firebasemlkit

import android.app.Application
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo


class AppAplication : Application() {

    override fun onCreate() {
        super.onCreate()
        RxPaparazzo.register(this)
    }
}
