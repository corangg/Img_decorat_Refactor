package com.data.mapper

import com.data.datasource.local.room.LocalImageData
import com.domain.model.ImageData

fun ImageData.toLocal() = LocalImageData(
    name = this.name,
    backgroundScale = this.backgroundScale,
    backgroundColor = this.backgroundColor,
    textColor = this.textColor,
    textSize = this.textSize,
    saturationValue = this.saturationValue,
    brightnessValue = this.brightnessValue,
    transparencyValue = this.transparencyValue
)

fun LocalImageData.toExternal() = ImageData(
    name = this.name,
    backgroundScale = this.backgroundScale,
    backgroundColor = this.backgroundColor,
    textColor = this.textColor,
    textSize = this.textSize,
    saturationValue = this.saturationValue,
    brightnessValue = this.brightnessValue,
    transparencyValue = this.transparencyValue
)