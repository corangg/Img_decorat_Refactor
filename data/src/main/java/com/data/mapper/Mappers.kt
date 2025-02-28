package com.data.mapper

import android.content.Context
import com.core.util.downloadFont
import com.data.datasource.local.room.LocalEmojiData
import com.data.datasource.local.room.LocalFontData
import com.data.datasource.local.room.LocalImageData
import com.data.datasource.local.room.LocalViewItemData
import com.data.datasource.remote.FontItem
import com.data.datasource.remote.RemoteEmojisData
import com.data.datasource.remote.RemoteUrls
import com.domain.model.EmojiData
import com.domain.model.ImageData
import com.domain.model.UnSplashData
import com.domain.model.ViewItemData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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

fun RemoteUrls.toExternal() = UnSplashData(
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

fun LocalViewItemData.toExternal() = ViewItemData(
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

fun RemoteEmojisData.toLocal() = LocalEmojiData(
    emoji = this.character,
    group = this.group
)

fun LocalEmojiData.toExternal() = EmojiData(
    emoji = this.emoji,
    group = this.group
)

fun Flow<List<LocalEmojiData>>.toExternalList(): Flow<List<EmojiData>> {
    return this.map { list -> list.map { it.toExternal() } }
}

fun FontItem.toLocal(context: Context): LocalFontData? {
    val fontUrl = this.files["regular"] ?: return null
    val fontPath = context.downloadFont(fontUrl, this.family, "ttf") ?: return null
    return LocalFontData(
        fontName = this.family,
        fontPath = fontPath
    )
}
