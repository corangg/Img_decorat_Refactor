package com.domain.model

import android.graphics.Color

data class BackgroundScaleItem(
    val scale: String,
    val width: Int,
    val height: Int
)

data class ImageData(
    val name: String = "temporary storage",
    val backgroundScale: Float = 1.0f,
    val backgroundColor: Int = Color.WHITE,
    val textColor: Int = Color.BLACK,
    val textSize: Int = 16,
    val saturationValue: Int? = null,
    val brightnessValue: Int? = null,
    val transparencyValue: Int? = null
)