package com.app.ui.activity

import android.graphics.Bitmap
import android.graphics.Color
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import com.app.R
import com.app.databinding.ActivityImgaeSplitBinding
import com.bumptech.glide.Glide
import com.core.ui.BaseActivity
import com.core.ui.custom.SplitSquareView
import com.core.util.uriToBitmap
import com.presentation.ImageSplitActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageSplitActivity :
    BaseActivity<ActivityImgaeSplitBinding>(ActivityImgaeSplitBinding::inflate) {
    private val viewModel: ImageSplitActivityViewModel by viewModels()

    private lateinit var splitSquareView: SplitSquareView

    override fun setUi() {
        binding.viewModel = viewModel
        binding.imgBtnSplit.setOnClickListener {
            val testBit = viewModel.cropSquareImage(splitSquareView,uriToBitmap(intent.getStringExtra(getString(R.string.image))?.toUri()!!)!!)

            Glide.with(this)
                .load(testBit)
                .into(binding.imgViewSplit)
        }
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {

    }

    override fun setUpDate() {
        setImage()
        setSquareSplitView()
    }

    private fun setImage() {
        val uri = intent.getStringExtra(getString(R.string.image))?.toUri() ?: return
        Glide.with(this)
            .load(uri)
            .into(binding.imgViewSplit)
    }

    private fun setSquareSplitView() {
        splitSquareView =SplitSquareView(this).apply {
            setImageBitmap(createTransparentBitmap())
        }
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        val a = this.resources.displayMetrics.widthPixels
        binding.frameSplitImage.addView(splitSquareView, layoutParams)
    }


    private fun createTransparentBitmap(): Bitmap {
        return Bitmap.createBitmap(2048, 1800, Bitmap.Config.ARGB_8888).apply {
            eraseColor(Color.TRANSPARENT)
        }
    }
}