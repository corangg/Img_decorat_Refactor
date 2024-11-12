package com.app.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.R
import com.app.databinding.ActivityMainBinding
import com.app.recyclerview.DrawerLayerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.core.ui.BaseActivity
import com.core.ui.custom.EditableImageView
import com.core.util.getScaleParams
import com.domain.model.ImageData
import com.domain.model.ViewItemData
import com.presentation.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate),
    View.OnClickListener {
    private val viewModel: MainActivityViewModel by viewModels()
    private val adapter by lazy { DrawerLayerAdapter() }

    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>

    override fun setUi() {
        binding.viewModel = viewModel
        binding.buttonOpenDrawerlayout.setOnClickListener(this)
        binding.buttonAddImg.setOnClickListener(this)
        binding.buttonMenu.setOnClickListener(this)
        val navController =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.findNavController()
                ?: return
        binding.bottomNavigation.setupWithNavController(navController)

        binding.drawerRecycleLayer.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = this@MainActivity.adapter
        }
        adapter.submitList(listOf())

        adapter.setOnItemClickListener { tem, position ->

        }
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
        viewModel.imageData.observe(lifecycleOwner, ::setImage)
    }

    override fun setUpDate() {
        itemTouchHelper()
        setImagePicker()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_open_drawerlayout -> binding.drawerLayout.openDrawer(GravityCompat.START)
            R.id.button_add_img -> openGallery()
            R.id.button_menu -> {}
        }
    }

    private fun setImage(imageData: ImageData?) {
        imageData ?: return
        binding.imgFlameLayout.setBackgroundColor(imageData.backgroundColor)
        binding.imgFlameLayout.layoutParams = binding.imgFlameLayout.layoutParams.getScaleParams(
            this.resources.displayMetrics.widthPixels,
            imageData.backgroundScale
        )
        binding.imgFlameLayout.setBackgroundColor(imageData.backgroundColor)
        if (imageData.backgroundImage != "") {
            Glide.with(binding.root)
                .load(imageData.backgroundImage)
                .centerCrop()
                .into(binding.imgFlameLayout.backgroundTarget())
        }
        addView(imageData.viewDataInfo)
    }

    fun View.backgroundTarget() = object : CustomTarget<Drawable>() {
        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
            this@backgroundTarget.background = resource
        }

        override fun onLoadCleared(placeholder: Drawable?) {
            this@backgroundTarget.background = placeholder
        }
    }

    private fun itemTouchHelper() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPos = viewHolder.adapterPosition
                val toPos = target.adapterPosition
                adapter.moveItem(fromPos, toPos)

                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.drawerRecycleLayer)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = getString(R.string.intentImageType)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        imagePickerLauncher.launch(Intent.createChooser(intent, "Select Pictures"))
    }

    private fun setImagePicker() {
        imagePickerLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUris = mutableListOf<Uri>()

                result.data?.clipData?.let { clipData ->
                    for (i in 0 until clipData.itemCount) {
                        val imageUri = clipData.getItemAt(i).uri
                        imageUris.add(imageUri)
                    }
                } ?: result.data?.data?.let { uri ->
                    imageUris.add(uri)
                }
                viewModel.addImageLayer(imageUris)
            }
        }
    }

    private var flagData = Pair(0, ViewItemData(0))

    private fun addView(list: List<ViewItemData>) {
        if (list[flagData.first] != flagData.second) {
            binding.imgView.removeAllViews()
            for (i in list.indices) {
                val view = EditableImageView(this).apply {
                    viewId = i
                    setMatrixData(list[i].matrixValues, list[i].scale, list[i].rotationDegrees)
                }
                view.onMatrixChangeCallback = { matrix, scale, degree, id ->
                    val matrixValues = FloatArray(9)
                    matrix.getValues(matrixValues)
                    flagData = Pair(
                        id, ViewItemData(
                            type = 0,
                            matrixValues = Array(9) { index -> matrixValues[index] },
                            scale = scale,
                            rotationDegrees = degree
                        )
                    )
                    viewModel.updateViewMatrix(flagData)
                }

                Glide.with(this).load(list[i].img).into(view)
                val layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.gravity = Gravity.CENTER
                binding.imgView.addView(view, layoutParams)
            }
        }
    }
}