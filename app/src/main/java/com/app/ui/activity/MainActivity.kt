package com.app.ui.activity

import android.graphics.drawable.Drawable
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.app.R
import com.app.databinding.ActivityMainBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.core.ui.BaseActivity
import com.core.util.getScaleParams
import com.domain.model.ImageData
import com.presentation.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate),
    View.OnClickListener {
    private val viewModel: MainActivityViewModel by viewModels()

    override fun setUi() {
        binding.viewModel = viewModel
        binding.buttonOpenDrawerlayout.setOnClickListener(this)
        binding.buttonAddImg.setOnClickListener(this)
        binding.buttonMenu.setOnClickListener(this)
        val navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.findNavController() ?: return
        binding.bottomNavigation.setupWithNavController(navController)
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
        viewModel.imageData.observe(lifecycleOwner, ::setImage)
    }

    override fun setUpDate() {
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_open_drawerlayout -> binding.drawerLayout.openDrawer(GravityCompat.START)
            R.id.button_add_img -> {}
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
        if(imageData.backgroundImage != ""){
            Glide.with(binding.root)
                .load(imageData.backgroundImage)
                .centerCrop()
                .into(binding.imgFlameLayout.backgroundTarget())
        }
    }

    fun View.backgroundTarget() = object : CustomTarget<Drawable>() {
        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
            this@backgroundTarget.background = resource
        }

        override fun onLoadCleared(placeholder: Drawable?) {
            this@backgroundTarget.background = placeholder
        }
    }
}