package com.core.ui.custom

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Path
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.example.img_decorat.presentation.ui.view.RotateGestureDetector

class EditableImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : AppCompatImageView(context, attrs, defStyle), View.OnTouchListener {
    private val matrix = Matrix()

    var viewId: Int = -1
    var onMatrixChangeCallback: ((Matrix, Float, Float, Int) -> Unit)? = null

    private val scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
    private val rotateGestureDetector = RotateGestureDetector(RotateListener())

    var scaleFactor = 1.0f
    var rotationDegrees = 0f
    private var lastTouchX = 0f
    private var lastTouchY = 0f
    var saturationValue = 1f
    var brightnessValue = 1f
    var transparencyValue = 1f


    init {
        setOnTouchListener(this)
        scaleType = ScaleType.MATRIX
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val transPos = getTransformedPoints()

        if (!judgeTouchableArea(event.x, event.y, transPos)) {
            return false
        }

        scaleGestureDetector.onTouchEvent(event)
        rotateGestureDetector.onTouchEvent(event)

        if (!scaleGestureDetector.isInProgress && !rotateGestureDetector.isInProgress) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastTouchX = event.x
                    lastTouchY = event.y
                    //viewModel?.selectLastImage(this.id)
                }

                MotionEvent.ACTION_MOVE -> {
                    val dx = event.x - lastTouchX
                    val dy = event.y - lastTouchY

                    matrix.postTranslate(dx, dy)
                    imageMatrix = matrix

                    lastTouchX = event.x
                    lastTouchY = event.y


                }

                MotionEvent.ACTION_UP -> {
                    onMatrixChangeCallback?.invoke(matrix, scaleFactor, rotationDegrees, viewId)
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

    private fun judgeTouchableArea(x: Float, y: Float, polygon: FloatArray): Boolean {
        var intersectCount = 0
        for (i in polygon.indices step 2) {
            val x1 = polygon[i]
            val y1 = polygon[i + 1]
            val x2 = polygon[(i + 2) % polygon.size]
            val y2 = polygon[(i + 3) % polygon.size]

            if (((y1 > y) != (y2 > y)) &&
                (x < (x2 - x1) * (y - y1) / (y2 - y1) + x1)
            ) {
                intersectCount++
            }
        }
        return (intersectCount % 2) == 1
    }

    private fun getTransformedPoints(): FloatArray {
        val drawable = drawable ?: return floatArrayOf()
        val width = drawable.intrinsicWidth.toFloat()
        val height = drawable.intrinsicHeight.toFloat()
        val points = floatArrayOf(
            0f, 0f,
            width, 0f,
            width, height,
            0f, height
        )
        matrix.mapPoints(points)
        return points
    }

    private fun drawBorder(canvas: Canvas) {
        val points = getTransformedPoints()
        if (points.isEmpty()) return
        val path = Path().apply {
            moveTo(points[0], points[1])
            lineTo(points[2], points[3])
            lineTo(points[4], points[5])
            lineTo(points[6], points[7])
            close()
        }
        /*if (viewModel?.lastTouchedImageId?.value == this.id) {
            canvas.drawPath(path, viewHelper.borderPaint(Color.WHITE, 4f))
        } else {
            canvas.drawPath(path, viewHelper.borderPaint(Color.TRANSPARENT))
        }*/
    }

    fun setImageTransparency(alpha: Float) {
        val clampedAlpha = Math.max(0f, Math.min(alpha, 100f)) / 100f
        transparencyValue = alpha
        this.alpha = clampedAlpha
    }

    fun setImageSaturation(saturation: Float) {
        saturationValue = (saturation + 100f) / 100f
        //colorFilter = viewHelper.applyColorFilter(saturationValue, brightnessValue)
    }

    fun setImageBrightness(brightness: Float) {
        brightnessValue = 0.008f * brightness + 1f
        //colorFilter = viewHelper.applyColorFilter(saturationValue, brightnessValue)
    }

    fun getImageBitmap(): Bitmap? {
        return (drawable as? BitmapDrawable)?.bitmap
    }

    fun setMatrixData(matrixValue: Array<Float>, scale: Float, degrees: Float) {
       /* matrix.setValues(matrixValue.toFloatArray())
        imageMatrix = matrix

        scaleFactor = scale
        rotationDegrees = degrees*/
        if (matrixValue.size == 9) { // 배열 크기 검증
            val floatArray = matrixValue.toFloatArray()
            matrix.setValues(floatArray)
            imageMatrix = matrix

            scaleFactor = scale
            rotationDegrees = degrees
        }
    }

    fun setImageScale(scale: Float) {
        scaleFactor = scale
        matrix.postScale(scaleFactor, scaleFactor)
        imageMatrix = matrix
    }

    fun getMatrixValues(): FloatArray {
        val matrixValues = FloatArray(9)
        matrix.getValues(matrixValues)
        return matrixValues
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val scale = detector.scaleFactor
            scaleFactor *= scale
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 10.0f))

            matrix.postScale(scale, scale, detector.focusX, detector.focusY)
            imageMatrix = matrix
            invalidate()
            return true
        }
    }

    private inner class RotateListener : RotateGestureDetector.OnRotateGestureListener {
        override fun onRotate(detector: RotateGestureDetector): Boolean {
            val rotation = detector.rotationDegreesDelta
            rotationDegrees += rotation
            matrix.postRotate(rotation, detector.focusX, detector.focusY)
            imageMatrix = matrix
            invalidate()
            return true
        }
    }
}