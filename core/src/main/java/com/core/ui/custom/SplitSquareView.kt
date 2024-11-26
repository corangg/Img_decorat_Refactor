package com.core.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.appcompat.widget.AppCompatImageView

class SplitSquareView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : AppCompatImageView(context, attrs, defStyle), View.OnTouchListener {
    private val matrix = Matrix()
    private var scaleFactor = 1.0f
    private val scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private var parentWidth = 0
    private var parentHeight = 0

    init {
        setOnTouchListener(this)
        scaleType = ScaleType.MATRIX
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        parentWidth = (parent as View).width
        parentHeight = (parent as View).height
    }

    private fun testPos(){

    }

    fun getParentSize(): Pair<Int, Int> {
        return Pair(parentWidth, parentHeight)
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        val point = areaPoint()
        val touchInsideImage = event.x >= point[0] && event.x <= point[2] &&
                event.y >= point[1] && event.y <= point[3]

        if (!touchInsideImage) {
            return false
        }

        scaleGestureDetector.onTouchEvent(event)

        if (!scaleGestureDetector.isInProgress) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastTouchX = event.x
                    lastTouchY = event.y
                    invalidate()
                }

                MotionEvent.ACTION_MOVE -> {
                    matrix.postTranslate( event.x - lastTouchX, event.y - lastTouchY)
                    imageMatrix = matrix

                    lastTouchX = event.x
                    lastTouchY = event.y
                }
            }
        }
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBorder(canvas)
    }

    fun strokePaint(strokeColor: Int, thickness: Float): Paint {
        return Paint().apply {
            color = strokeColor
            style = Paint.Style.STROKE
            strokeWidth = thickness
        }
    }

    fun areaPoint(): FloatArray {
        val pos = getCurrentImagePosition()
        val viewSize = getCurrentImageSize()
        val posX = if (pos.first > 0) {
            pos.first
        } else {
            0f
        }
        val posY = if (pos.second > 0) {
            pos.second
        } else {
            0f
        }
        val posRight = if (pos.first + viewSize.first < parentWidth) {
            pos.first + viewSize.first
        } else {
            parentWidth.toFloat()
        }
        val posBottom = if (pos.second + viewSize.second < parentHeight) {
            pos.second + viewSize.second
        } else {
            parentHeight.toFloat()
        }

        return floatArrayOf(posX, posY, posRight, posBottom)
    }

    private fun drawBorder(canvas: Canvas) {
        val point = areaPoint()
        val borderRect = RectF(
            point[0],
            point[1],
            point[2],
            point[3]
        )
        canvas.drawRect(borderRect, strokePaint(Color.RED, 4f))
    }

    fun getCurrentImageSize(): Pair<Float, Float> {
        val values = FloatArray(9)
        matrix.getValues(values)
        val scaleX = values[Matrix.MSCALE_X]
        val scaleY = values[Matrix.MSCALE_Y]

        val drawable = drawable ?: return Pair(0f, 0f)
        val originalWidth = drawable.intrinsicWidth
        val originalHeight = drawable.intrinsicHeight

        return Pair(originalWidth * scaleX, originalHeight * scaleY)
    }

    fun getCurrentImagePosition(): Pair<Float, Float> {
        val values = FloatArray(9)
        matrix.getValues(values)
        val transX = values[Matrix.MTRANS_X]
        val transY = values[Matrix.MTRANS_Y]
        return Pair(transX, transY)
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            var scale = detector.scaleFactor
            val currentScaleFactor = scaleFactor * scale
            val maxScale = Math.min(
                parentWidth / drawable.intrinsicWidth.toFloat(),
                parentHeight / drawable.intrinsicHeight.toFloat()
            )
            val minScale = 0.1f

            if (currentScaleFactor > maxScale) {
                scale = maxScale / scaleFactor
            } else if (currentScaleFactor < minScale) {
                scale = minScale / scaleFactor
            }

            scaleFactor *= scale
            matrix.postScale(scale, scale, detector.focusX, detector.focusY)
            imageMatrix = matrix
            invalidate()
            return true
        }
    }
}
