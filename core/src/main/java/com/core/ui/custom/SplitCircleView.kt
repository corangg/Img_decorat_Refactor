package com.core.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import com.core.util.borderPaint


class SplitCircleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : BaseCutView(context, attrs, defStyle) {
    private val scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
    var radius: Float = 200f
        set(value) {
            field = value
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBorder(canvas)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (!isTouchableArea(event)) {
            return false
        }

        scaleGestureDetector.onTouchEvent(event)

        if (!scaleGestureDetector.isInProgress) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastTouchX = event.x
                    lastTouchY = event.y
                }

                MotionEvent.ACTION_MOVE -> {
                    val dx = event.x - lastTouchX
                    val dy = event.y - lastTouchY

                    transformationMatrix.postTranslate(dx, dy)
                    imageMatrix = transformationMatrix
                    lastTouchX = event.x
                    lastTouchY = event.y
                }
            }
        }
        invalidate()
        return true
    }

    override fun drawBorder(canvas: Canvas) {
        val pos = getCurrentImagePosition()
        canvas.drawCircle(pos.first, pos.second, radius, borderPaint(Color.RED, 4f))
    }

    override fun isTouchableArea(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        val pos = getCurrentImagePosition()
        val centerX = pos.first
        val centerY = pos.second
        val distance = Math.sqrt(
            Math.pow((x - centerX).toDouble(), 2.0) + Math.pow(
                (y - centerY).toDouble(),
                2.0
            )
        )
        return distance <= radius
    }

    fun getCurrentImagePosition(): Pair<Float, Float> {
        val values = FloatArray(9)
        transformationMatrix.getValues(values)
        val transX = values[Matrix.MTRANS_X]
        val transY = values[Matrix.MTRANS_Y]
        return Pair(transX, transY)
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            var factor = detector.scaleFactor
            // 스케일 범위 제한
            val newScale = (scaleFactor * factor).coerceIn(0.1f, 6f)
            factor = newScale / scaleFactor
            radius *= factor
            scaleFactor = newScale
            invalidate()
            return true
        }
    }
}