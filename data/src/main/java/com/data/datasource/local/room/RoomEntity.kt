package com.data.datasource.local.room

import android.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocalImageData(
    @PrimaryKey val name: String = "temporary storage",
    val backgroundScale: Float,
    val backgroundColor: Int = Color.TRANSPARENT,
    val backgroundImage: String = "",
    val textColor: Int = Color.BLACK,
    val textSize: Int = 16,
    val saturationValue: Int? = null,
    val brightnessValue: Int? = null,
    val transparencyValue: Int? = null
)