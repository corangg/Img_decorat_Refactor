package com.data.mapper

import com.data.datasource.local.room.LocalImageData
import com.data.datasource.remote.LocalUrls
import com.domain.model.ImageData
import com.domain.model.UnSplashUrls

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
    backgroundImage = this.backgroundImage,
    textColor = this.textColor,
    textSize = this.textSize,
    saturationValue = this.saturationValue,
    brightnessValue = this.brightnessValue,
    transparencyValue = this.transparencyValue
)

fun LocalUrls.toExternal() = UnSplashUrls(
    full = this.full,
    raw = this.raw,
    regular = this.regular,
    small = this.small,
    thumb = this.thumb,
)