package com.app.ui.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import com.app.R
import com.app.databinding.ActivityImgaeSplitBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.core.ui.BaseActivity
import com.core.ui.custom.SplitCircleView
import com.core.ui.custom.SplitPolygonView
import com.core.ui.custom.SplitSquareView
import com.core.util.bitmapToUri
import com.core.util.uriToBitmap
import com.presentation.ImageSplitActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageSplitActivity :
    BaseActivity<ActivityImgaeSplitBinding>(ActivityImgaeSplitBinding::inflate) {
    private val viewModel: ImageSplitActivityViewModel by viewModels()
    private lateinit var imgUri: Uri

    private lateinit var splitSquareView: SplitSquareView
    private lateinit var splitCircleView: SplitCircleView
    private lateinit var splitPolygonView: SplitPolygonView

    override fun setUi() {
        binding.viewModel = viewModel
        bindingNavigation()
        binding.imgBtnSplit.setOnClickListener {
            viewModel.clearNextStack()
            imgUri = bitmapToUri(selectSplitView(imgUri)) ?: return@setOnClickListener
            Glide.with(this)
                .load(imgUri)
                .into(binding.imgViewSplit)
        }
        binding.imgBtnBack.setOnClickListener {
            viewModel.pushNextStack(uriToBitmap(imgUri))
            val bitmap = viewModel.getBackLastStack() ?: return@setOnClickListener
            imgUri = bitmapToUri(bitmap) ?: return@setOnClickListener
            Glide.with(this)
                .load(imgUri)
                .override(bitmap.width, bitmap.height)
                .into(binding.imgViewSplit)
        }
        binding.imgBtnNext.setOnClickListener {
            viewModel.pushBackStack(uriToBitmap(imgUri))
            val bitmap = viewModel.getNextLastStack() ?: return@setOnClickListener
            imgUri = bitmapToUri(bitmap) ?: return@setOnClickListener
            Glide.with(this)
                .load(imgUri)
                .override(bitmap.width, bitmap.height)
                .into(binding.imgViewSplit)
        }
        binding.imgBtnCheck.setOnClickListener {
            val intent = Intent().putExtra(
                getString(R.string.splitBitmap),
                imgUri.toString()
            )
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
        viewModel.stackBack.observe(this) {
            if (it.size > 0) {
                binding.imgBtnBack.backgroundTintList = getColorStateList(R.color.point_color)
            } else {
                binding.imgBtnBack.backgroundTintList = getColorStateList(R.color.background_color)
            }
        }
        viewModel.stackNext.observe(this) {
            if (it.size > 0) {
                binding.imgBtnNext.backgroundTintList = getColorStateList(R.color.point_color)
            } else {
                binding.imgBtnNext.backgroundTintList = getColorStateList(R.color.background_color)
            }
        }
    }

    override fun setUpDate() {
        imgUri = intent.getStringExtra(getString(R.string.image))?.toUri() ?: "".toUri()
        setImage()
    }

    private fun setImage() {
        val uri = intent.getStringExtra(getString(R.string.image))?.toUri() ?: return
        Glide.with(this)
            .load(uri)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    runOnUiThread {
                        setSplitView()
                    }
                    return false
                }
            })
            .into(binding.imgViewSplit)
    }

    private fun bindingNavigation() {
        binding.bottomNavigationSplit.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_split_square -> {
                    splitSquareView.visibility = View.VISIBLE
                    splitCircleView.visibility = View.GONE
                    splitPolygonView.visibility = View.GONE
                    binding.groupPolygon.visibility = View.GONE
                    true
                }

                R.id.menu_split_circle -> {
                    splitCircleView.visibility = View.VISIBLE
                    splitPolygonView.visibility = View.GONE
                    splitSquareView.visibility = View.GONE
                    binding.groupPolygon.visibility = View.GONE
                    true
                }

                R.id.menu_split_polygon -> {
                    binding.groupPolygon.visibility = View.VISIBLE
                    splitPolygonView.visibility = View.VISIBLE
                    splitCircleView.visibility = View.GONE
                    splitSquareView.visibility = View.GONE
                    true
                }

                else -> false
            }
        }
    }

    private fun setSplitView() {
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        splitSquareView = SplitSquareView(this).apply {
            createTransparentBitmap {
                setImageBitmap(it)
                binding.frameSplitImage.addView(splitSquareView, layoutParams)
                splitSquareView.visibility = View.GONE
            }
        }
        splitCircleView = SplitCircleView(this).apply {
            createTransparentBitmap {
                setImageBitmap(it)
                binding.frameSplitImage.addView(splitCircleView, layoutParams)
                splitCircleView.visibility = View.GONE
            }
        }
        splitPolygonView = SplitPolygonView(this).apply {
            createTransparentBitmap {
                setImageBitmap(it)
                binding.frameSplitImage.addView(splitPolygonView, layoutParams)
                splitPolygonView.visibility = View.GONE
                viewModel.polygonPoint.observe(this@ImageSplitActivity) { polygonPoints ->
                    splitPolygonView.setPolygon(polygonPoints)
                }
            }
        }
    }

    private fun createTransparentBitmap(onBitmap: (Bitmap) -> Unit) {
        binding.frameSplitImage.post {
            val frameWidth = binding.frameSplitImage.width
            val frameHeight = binding.frameSplitImage.height
            val bitmap =
                Bitmap.createBitmap(frameWidth, frameHeight, Bitmap.Config.ARGB_8888).apply {
                    eraseColor(Color.TRANSPARENT)
                }
            onBitmap(bitmap)
        }
    }

    private fun selectSplitView(imgUri: Uri?): Bitmap? {
        viewModel.pushBackStack(uriToBitmap(imgUri))
        return when (binding.bottomNavigationSplit.selectedItemId) {
            R.id.menu_split_square -> {
                return viewModel.cutSquareImage(splitSquareView, uriToBitmap(imgUri))
            }

            R.id.menu_split_circle -> {
                return viewModel.cutCircleImage(splitCircleView, uriToBitmap(imgUri))
            }

            R.id.menu_split_polygon -> {
                return viewModel.cutPolygonImage(splitPolygonView, uriToBitmap(imgUri))
            }

            else -> null
        }
    }
}