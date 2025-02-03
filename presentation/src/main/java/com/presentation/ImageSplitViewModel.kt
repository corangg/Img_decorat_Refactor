package com.presentation

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import androidx.lifecycle.MutableLiveData
import com.core.di.DefaultDispatcher
import com.core.di.IoDispatcher
import com.core.di.MainDispatcher
import com.core.ui.custom.SplitCircleView
import com.core.ui.custom.SplitPolygonView
import com.core.ui.custom.SplitSquareView
import com.core.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class ImageSplitActivityViewModel @Inject constructor(
    @MainDispatcher mainDispatcher: MainCoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseViewModel(mainDispatcher, defaultDispatcher, ioDispatcher) {
    val polygonPoint = MutableLiveData(3)

    fun splitImage(splitAreaView: SplitSquareView, bitmap: Bitmap): Bitmap? {
        return cropSquareImage(splitAreaView, bitmap)
    }

    fun cropSquareImage(splitAreaView: SplitSquareView, bitmap: Bitmap?): Bitmap? {
        bitmap?: return null
        val viewSize = splitAreaView.getParentSize()
        val areaPoints = splitAreaView.areaRect
        val path = Path().apply {
            moveTo(areaPoints.left, areaPoints.top)
            lineTo(areaPoints.right, areaPoints.top)
            lineTo(areaPoints.right, areaPoints.bottom)
            lineTo(areaPoints.left, areaPoints.bottom)
            close()
        }
        val (scale, offsetX, offsetY) = calculateScaleAndOffset(viewSize, bitmap)

        return createScaledBitmap(bitmap, viewSize, path, scale, offsetX, offsetY)
    }

    fun cropCircleImage(circleView: SplitCircleView, bitmap: Bitmap?): Bitmap? {
        bitmap?: return null
        val (centerX, centerY) = circleView.getCurrentImagePosition()
        val radius = circleView.radius
        val circlePath = Path().apply {
            addCircle(centerX, centerY, radius, Path.Direction.CW)
        }
        val viewSize = circleView.getParentSize()
        val (scale, offsetX, offsetY) = calculateScaleAndOffset(viewSize, bitmap)

        return createScaledBitmap(bitmap, viewSize, circlePath, scale, offsetX, offsetY)
    }

    fun cropPolygonImage(splitAreaView: SplitPolygonView, bitmap: Bitmap?): Bitmap? {
        bitmap?: return null
        val polygonPath = splitAreaView.getPolygonPath()
        val viewSize = splitAreaView.getParentSize()
        val (scale, offsetX, offsetY) = calculateScaleAndOffset(viewSize, bitmap)

        return createScaledBitmap(bitmap, viewSize, polygonPath, scale, offsetX, offsetY)
    }

    private fun calculateScaleAndOffset(
        viewSize: Pair<Int, Int>,
        bitmap: Bitmap
    ): Triple<Float, Float, Float> {
        val scaleX = viewSize.first.toFloat() / bitmap.width
        val scaleY = viewSize.second.toFloat() / bitmap.height
        val scale: Float
        val offsetX: Float
        val offsetY: Float

        if (scaleX < scaleY) {
            scale = scaleX
            offsetY = (viewSize.second - scale * bitmap.height) / 2
            offsetX = 0f
        } else {
            scale = scaleY
            offsetX = (viewSize.first - scale * bitmap.width) / 2
            offsetY = 0f
        }
        return Triple(scale, offsetX, offsetY)
    }

    private fun createScaledBitmap(
        bitmap: Bitmap,
        viewSize: Pair<Int, Int>,
        path: Path,
        scale: Float,
        offsetX: Float,
        offsetY: Float
    ): Bitmap {
        val scaledBitmap =
            Bitmap.createBitmap(viewSize.first, viewSize.second, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(scaledBitmap)

        val paint = Paint().apply {
            isAntiAlias = true
            isFilterBitmap = true
            isDither = true
        }
        val srcRect = Rect(0, 0, bitmap.width, bitmap.height)
        val destRect = Rect(
            offsetX.toInt(),
            offsetY.toInt(),
            (bitmap.width * scale + offsetX).toInt(),
            (bitmap.height * scale + offsetY).toInt()
        )
        canvas.drawBitmap(bitmap, srcRect, destRect, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        val inversePath = Path()
        inversePath.addRect(
            0f,
            0f,
            viewSize.first.toFloat(),
            viewSize.second.toFloat(),
            Path.Direction.CW
        )
        inversePath.op(path, Path.Op.DIFFERENCE)
        canvas.drawPath(inversePath, paint)

        return scaledBitmap
    }
}