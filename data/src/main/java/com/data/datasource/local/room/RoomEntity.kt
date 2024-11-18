package com.data.datasource.local.room

import android.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.data.datasource.local.room.converter.ImageDataConverter
import com.domain.model.ViewItemData

@Entity
@TypeConverters(ImageDataConverter::class)
data class LocalImageData(
    @PrimaryKey val name: String = "temporary storage",
    val backgroundScale: Float,
    val backgroundColor: Int = Color.TRANSPARENT,
    val backgroundImage: String = "",
    val viewDataInfo: List<LocalViewItemData> = listOf()
)

data class LocalViewItemData(
    val type: Int,
    val select: Boolean = false,
    val visible: Boolean,
    val scale: Float,
    val rotationDegrees: Float,
    val saturationValue: Float,
    val brightnessValue: Float,
    val transparencyValue: Float,
    val matrixValues: Array<Float>,
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

        if (!matrixValues.contentEquals(other.matrixValues)) return false
        if (scale != other.scale) return false
        if (rotationDegrees != other.rotationDegrees) return false

        return true
    }

    override fun hashCode(): Int {
        var result = matrixValues.contentHashCode()
        result = 31 * result + scale.hashCode()
        result = 31 * result + rotationDegrees.hashCode()
        return result
    }
}