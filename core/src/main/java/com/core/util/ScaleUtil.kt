package com.core.util

import android.view.ViewGroup.LayoutParams

fun LayoutParams.getScaleParams(screenWith: Int, scale: Float): LayoutParams {
    return if (scale > 1) {
        this.apply {
            width = screenWith
            height = (screenWith / scale).toInt()
        }
    } else {
        this.apply {
            width = (screenWith * scale).toInt()
            height = screenWith
        }
    }

}