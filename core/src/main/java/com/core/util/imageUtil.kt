package com.core.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.io.FileNotFoundException
import java.io.InputStream

fun Context.uriToBitmap(imageUri: Uri): Bitmap? {
    var inputStream: InputStream? = null
    return try {
        inputStream = contentResolver.openInputStream(imageUri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
        null
    } finally {
        inputStream?.close()
    }
}

fun View.backgroundTarget() = object : CustomTarget<Drawable>() {
    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
        this@backgroundTarget.background = resource
    }

    override fun onLoadCleared(placeholder: Drawable?) {
        this@backgroundTarget.background = placeholder
    }
}