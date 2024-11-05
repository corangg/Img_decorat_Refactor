package com.app.ui.activity

import android.view.View
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.app.R
import com.app.databinding.ActivityMainBinding
import com.core.ui.BaseActivity
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
        val navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.findNavController()?: return
        binding.bottomNavigation.setupWithNavController(navController)
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
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
}