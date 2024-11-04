package com.app.ui.fragment

import android.view.Gravity
import android.view.View
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.app.R
import com.app.databinding.FragmentMainBinding
import com.core.ui.BaseFragment
import com.presentation.MainFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate),
    View.OnClickListener {
    private val viewModel: MainFragmentViewModel by viewModels()

    override fun setUi() {
        binding.viewModel = viewModel
        binding.buttonOpenDrawerlayout.setOnClickListener(this)
        binding.buttonAddImg.setOnClickListener(this)
        binding.buttonMenu.setOnClickListener(this)
    }

    override fun setUpDate() {
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_open_drawerlayout -> binding.drawerLayout.openDrawer(GravityCompat.START)
            R.id.button_add_img ->{}
            R.id.button_menu ->{}
        }
    }
}