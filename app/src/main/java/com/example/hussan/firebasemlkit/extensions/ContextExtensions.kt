package com.example.hussan.firebasemlkit.extensions

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat

inline fun <reified T : Activity> Activity.navigate(
    bundle: Bundle? = null,
    options: ActivityOptionsCompat? = null
) {
    val intent = Intent(this, T::class.java)
    intent.apply {
        bundle?.let {
            putExtras(bundle)
        }
        startActivity(this, options?.toBundle())
    }
}