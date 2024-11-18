package com.domain.model

import android.graphics.Bitmap
import android.graphics.Color

data class BackgroundScaleItem(
    val scale: String,
    val width: Int,
    val height: Int
)

data class ImageData(
    val name: String = "temporary storage",
    val backgroundScale: Float = 1.0f,
    val backgroundColor: Int = Color.TRANSPARENT,
    val backgroundImage: String = "",
    val viewDataInfo: List<ViewItemData> = listOf()
)

data class UnsplashData(
    val localUrls: UnSplashUrls,
)

data class UnSplashUrls(
    val full: String,
    val raw: String,
    val regular: String,
    val small: String,
    val thumb: String
)

data class DrawerLayerItemData(
    var check: Boolean = false,
    var select: Boolean = false,
    val id: Int,
    val text: String = "",
    var type: Int = 0,
    var bitMap: Bitmap = Bitmap.createBitmap(1024, 1024, Bitmap.Config.ARGB_8888)
        .apply { (Color.TRANSPARENT) }
)

data class ViewItemData(
    val type: Int,
    val select: Boolean = false,
    val visible: Boolean = true,
    val scale: Float= 1.0f,
    val rotationDegrees: Float =1f,
    val saturationValue: Float= 1f,
    val brightnessValue: Float =1f,
    val transparencyValue: Float = 1f,
    val matrixValues: Array<Float> = arrayOf(),
    val img: String = "",
    val text: String = "",
    val textSize: Int = 24,
    val textColor: Int = Color.TRANSPARENT,
    val textBackGroundColor: Int = Color.TRANSPARENT,
    val font: Int = -1
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ViewItemData) return false

        if (img != other.img) return false
        if (!matrixValues.contentEquals(other.matrixValues)) return false
        if (scale != other.scale) return false
        if (rotationDegrees != other.rotationDegrees) return false

        return true
    }

    override fun hashCode(): Int {
        var result = img.hashCode()
        result = 31 * result + matrixValues.contentHashCode()
        result = 31 * result + scale.hashCode()
        result = 31 * result + rotationDegrees.hashCode()
        return result
    }
}