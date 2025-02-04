package com.app.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
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
        bindingButton()
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
        viewModel.stackBack.observe(this) {
            onButtonValue(binding.imgBtnBack, it.size)
        }
        viewModel.stackNext.observe(this) {
            onButtonValue(binding.imgBtnNext, it.size)
        }
    }

    override fun setUpDate() {
        imgUri = intent.getStringExtra(getString(R.string.image))?.toUri() ?: "".toUri()
        setImage()
    }

    private fun bindingNavigation() {
        binding.bottomNavigationSplit.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_split_square -> {
                    visibleSplitView(splitSquareView, arrayOf(splitCircleView, splitPolygonView))
                    binding.groupPolygon.visibility = View.GONE
                    true
                }

                R.id.menu_split_circle -> {
                    visibleSplitView(splitCircleView, arrayOf(splitSquareView, splitPolygonView))
                    binding.groupPolygon.visibility = View.GONE
                    true
                }

                R.id.menu_split_polygon -> {
                    visibleSplitView(splitPolygonView, arrayOf(splitSquareView, splitCircleView))
                    binding.groupPolygon.visibility = View.VISIBLE
                    true
                }

                else -> false
            }
        }
    }

    private fun visibleSplitView(visibleView: View, goneViews: Array<View>) {
        visibleView.visibility = View.VISIBLE
        goneViews.forEach {
            it.visibility = View.GONE
        }
    }

    private fun bindingButton() {
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
            setStackImg(bitmap)
        }
        binding.imgBtnNext.setOnClickListener {
            viewModel.pushBackStack(uriToBitmap(imgUri))
            val bitmap = viewModel.getNextLastStack() ?: return@setOnClickListener
            setStackImg(bitmap)
        }
        binding.imgBtnCheck.setOnClickListener {
            val intent = Intent().putExtra(
                getString(R.string.splitBitmap),
                imgUri.toString()
            )
            setResult(RESULT_OK, intent)
            finish()
        }
        binding.imgBtnBackActivity.setOnClickListener {
            finish()
        }
    }

    private fun setStackImg(bitmap: Bitmap) {
        imgUri = bitmapToUri(bitmap) ?: return
        Glide.with(this)
            .load(imgUri)
            .override(bitmap.width, bitmap.height)
            .into(binding.imgViewSplit)
    }

    private fun onButtonValue(button: AppCompatButton, stackSize: Int) {
        if (stackSize > 0) {
            button.backgroundTintList = getColorStateList(R.color.point_color)
        } else {
            button.backgroundTintList = getColorStateList(R.color.background_color)
        }
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

    private fun setSplitView() {
        splitSquareView = createSplitView(SplitSquareView::class.java) {}
        splitCircleView = createSplitView(SplitCircleView::class.java) { it.visibility = View.GONE }
        splitPolygonView = createSplitView(SplitPolygonView::class.java) {
            it.visibility = View.GONE
            viewModel.polygonPoint.observe(this@ImageSplitActivity) { polygonPoints ->
                splitPolygonView.setPolygon(polygonPoints)
            }
        }
    }

    private fun <T : AppCompatImageView> createSplitView(
        viewClass: Class<T>,
        onViewCreated: (T) -> Unit
    ): T {
        val view = viewClass.getConstructor(Context::class.java).newInstance(this).apply {
            createTransparentBitmap {
                setImageBitmap(it)
                binding.frameSplitImage.addView(
                    this, FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    )
                )
                onViewCreated(this)
            }
        }
        return view
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