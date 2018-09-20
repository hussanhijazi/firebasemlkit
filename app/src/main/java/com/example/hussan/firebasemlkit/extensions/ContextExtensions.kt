package com.example.hussan.firebasemlkit.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.widget.Toast
import java.io.IOException
import java.io.InputStream

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

fun Context.getBitmapFromAsset(filePath: String): Bitmap? {
    val assetManager = this.assets

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

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

}
