package com.data.mapper

import com.data.datasource.local.room.LocalImageData
import com.data.datasource.local.room.LocalViewItemData
import com.data.datasource.remote.RemoteUrls
import com.domain.model.ImageData
import com.domain.model.UnSplashUrls
import com.domain.model.ViewItemData

fun ImageData.toLocal() = LocalImageData(
    name = this.name,
    backgroundScale = this.backgroundScale,
    backgroundColor = this.backgroundColor,
    backgroundImage = this.backgroundImage,
    viewDataInfo = this.viewDataInfo.map { it.toLocal() }
)

fun LocalImageData.toExternal() = ImageData(
    name = this.name,
    backgroundScale = this.backgroundScale,
    backgroundColor = this.backgroundColor,
    backgroundImage = this.backgroundImage,
    viewDataInfo = this.viewDataInfo.map { it.toExternal() }
)

fun RemoteUrls.toExternal() = UnSplashUrls(
    full = this.full,
    raw = this.raw,
    regular = this.regular,
    small = this.small,
    thumb = this.thumb,
)

fun ViewItemData.toLocal() = LocalViewItemData(
    type = this.type,
    select = this.select,
    visible = this.visible,
    scale = this.scale,
    rotationDegrees = this.rotationDegrees,
    saturationValue = this.saturationValue,
    brightnessValue = this.brightnessValue,
    transparencyValue = this.transparencyValue,
    matrixValues = this.matrixValues,
    img = this.img,
    text = this.text,
    textSize = this.textSize,
    textColor = this.textColor,
    textBackGroundColor = this.textBackGroundColor,
    font = this.font,
)

fun LocalViewItemData.toExternal(): ViewItemData {
    return ViewItemData(
        type = this.type,
        select = this.select,
        visible = this.visible,
        scale = this.scale,
        rotationDegrees = this.rotationDegrees,
        saturationValue = this.saturationValue,
        brightnessValue = this.brightnessValue,
        transparencyValue = this.transparencyValue,
        matrixValues = this.matrixValues.copyOf(),
        img = this.img,
        text = this.text,
        textSize = this.textSize,
        textColor = this.textColor,
        textBackGroundColor = this.textBackGroundColor,
        font = this.font
    )
}