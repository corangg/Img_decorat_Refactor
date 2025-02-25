package com.app.ui.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.Gravity
import android.widget.FrameLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.app.R
import com.app.databinding.ActivityMainBinding
import com.app.recyclerview.DrawerLayerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.core.ui.BaseActivity
import com.core.ui.custom.EditableImageView
import com.core.util.backgroundTarget
import com.core.util.getScaleParams
import com.core.util.getScreenSize
import com.core.util.openGallery
import com.domain.model.ImageData
import com.domain.model.ViewItemData
import com.presentation.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private val viewModel: MainActivityViewModel by viewModels()
    private val adapter by lazy { DrawerLayerAdapter() }
    private var selectedView = -1
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>

    private val startForResult = registerForActivityResultHandler { result ->
        val uri = result.data?.getStringExtra(getString(R.string.splitBitmap))
            ?: return@registerForActivityResultHandler
        viewModel.updateImageUri(uri)
    }

    override fun setUi() {
        binding.viewModel = viewModel
        bindingOnClick()
        bindingNavigation()
        bindingRecyclerView()
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
        viewModel.imageData.observe(lifecycleOwner, ::setMainView)
    }

    override fun setUpDate() {
        itemTouchHelper()
        setImagePicker()
    }

    private fun bindingOnClick() {
        binding.buttonOpenDrawerlayout.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.buttonAddImg.setOnClickListener {
            openGallery(imagePickerLauncher)
        }
        binding.buttonMenu.setOnClickListener {

        }
    }

    private fun bindingNavigation() {
        val navController =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.findNavController()
                ?: return
        binding.bottomNavigation.setupWithNavController(navController)
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_navi_split -> {
                    val intent = Intent(this, ImageSplitActivity::class.java)
                    val uri = viewModel.imageData.value?.viewDataInfo?.find { it.select }?.img
                        ?: return@setOnItemSelectedListener false
                    intent.apply {
                        putExtra(getString(R.string.image), uri)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    startForResult.launch(intent)
                    true
                }

                R.id.menu_navi_background, R.id.menu_navi_hue, R.id.menu_navi_sticker -> {
                    NavigationUI.onNavDestinationSelected(item, navController)
                    true
                }

                else -> false
            }
        }
    }

    private fun bindingRecyclerView() {
        binding.drawerRecycleLayer.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = this@MainActivity.adapter
        }
        adapter.setOnItemClickListener { item, position ->
            selectedView = position
            viewModel.selectImage(position)
            addView(viewModel.imageData.value?.viewDataInfo ?: listOf())
        }
        adapter.setOnItemCheckListener { position, isChecked ->
            viewModel.checkImageLayer(position, isChecked)
        }
        adapter.setOnItemDeleteListener {
            viewModel.deleteImageLayer(it)
        }
    }

    private fun setMainView(imageData: ImageData?) {
        imageData ?: return
        selectedView = imageData.viewDataInfo.indexOfFirst { it.select }
        setMainViewBackGround(imageData)
        addView(imageData.viewDataInfo)
        adapter.submitList(imageData.viewDataInfo.toMutableList())
    }

    private fun setMainViewBackGround(imageData: ImageData) {
        binding.imgFlameLayout.setBackgroundColor(imageData.backgroundColor)
        binding.imgFlameLayout.layoutParams =
            binding.imgFlameLayout.layoutParams.getScaleParams(
                this.resources.displayMetrics.widthPixels,
                imageData.backgroundScale
            )
        if (imageData.backgroundImage != "") {
            Glide.with(binding.root)
                .load(imageData.backgroundImage)
                .override(getSmallScreenSize(), getSmallScreenSize())
                .centerCrop()
                .into(binding.imgFlameLayout.backgroundTarget())
        }
    }

    private fun itemTouchHelper() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: ViewHolder,
                target: ViewHolder
            ): Boolean {
                val fromPos = viewHolder.adapterPosition
                val toPos = target.adapterPosition
                adapter.moveItem(fromPos, toPos)
                viewModel.swapImageLayer(fromPos, toPos)
                return true
            }

            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {}
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.drawerRecycleLayer)
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
                        contentResolver.takePersistableUriPermission(
                            imageUri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                        imageUris.add(imageUri)
                    }
                } ?: result.data?.data?.let { uri ->
                    imageUris.add(uri)
                }
                viewModel.addImageLayer(imageUris)
            }
        }
    }

    private fun addView(list: List<ViewItemData>) {
        binding.imgView.removeAllViews()
        if (list.isEmpty()) return
        for (i in list.indices) {
            if (list[i].visible) {
                val view = EditableImageView(this).apply {
                    viewId = i
                    if (i == selectedView) {
                        isSelectedValue = true
                    }
                    setMatrixData(list[i].matrixValues, list[i].scale, list[i].rotationDegrees)
                    setImageSaturation(list[i].saturationValue)
                    setImageBrightness(list[i].brightnessValue)
                    setImageTransparency(list[i].transparencyValue)
                    onSelectCallback = {
                        if (selectedView != it) {
                            viewModel.selectImage(it)
                            adapter.selectView(it)
                        }
                    }
                    onMatrixChangeCallback = { matrix, scale, degree, id ->
                        val matrixValues = FloatArray(9)
                        matrix.getValues(matrixValues)
                        val flagData = Pair(
                            id, ViewItemData(
                                type = 0,
                                matrixValues = Array(9) { index -> matrixValues[index] },
                                scale = scale,
                                rotationDegrees = degree
                            )
                        )
                        viewModel.updateViewMatrix(flagData)
                    }
                }
                Glide.with(this).load(list[i].img)
                    .override(getSmallScreenSize(), getSmallScreenSize())
                    .format(DecodeFormat.PREFER_RGB_565).into(view)
                val layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                )

                layoutParams.gravity = Gravity.CENTER
                binding.imgView.addView(view, layoutParams)
            }
        }
    }

    private fun getSmallScreenSize(): Int {
        return minOf(getScreenSize(this).first, getScreenSize(this).second)
    }
}