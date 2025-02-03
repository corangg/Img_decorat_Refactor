package com.presentation

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import androidx.lifecycle.MutableLiveData
import com.core.di.DefaultDispatcher
import com.core.di.IoDispatcher
import com.core.di.MainDispatcher
import com.core.ui.custom.SplitCircleView
import com.core.ui.custom.SplitPolygonView
import com.core.ui.custom.SplitSquareView
import com.core.viewmodel.BaseViewModel
import com.domain.usecase.CutCircleImageView
import com.domain.usecase.CutPolygonImageView
import com.domain.usecase.CutSquareImageView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class ImageSplitActivityViewModel @Inject constructor(
    private val cutSquareImageView: CutSquareImageView,
    private val cutCircleImageView: CutCircleImageView,
    private val cutPolygonImageView: CutPolygonImageView,
    @MainDispatcher mainDispatcher: MainCoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseViewModel(mainDispatcher, defaultDispatcher, ioDispatcher) {
    val polygonPoint = MutableLiveData(3)

    fun splitImage(splitAreaView: SplitSquareView, bitmap: Bitmap): Bitmap? {
        return cutSquareImageView(splitAreaView, bitmap)
    }

    fun cutSquareImage(splitAreaView: SplitSquareView, bitmap: Bitmap?) =
        cutSquareImageView(splitAreaView, bitmap)

    fun cutCircleImage(circleView: SplitCircleView, bitmap: Bitmap?) =
        cutCircleImageView(circleView, bitmap)

    fun cutPolygonImage(splitAreaView: SplitPolygonView, bitmap: Bitmap?) =
        cutPolygonImageView(splitAreaView, bitmap)
}