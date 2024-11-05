package com.app.ui.fragment.background

import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.app.R
import com.app.databinding.FragmentBackGroundBinding
import com.core.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BackGroundFragment :
    BaseFragment<FragmentBackGroundBinding>(FragmentBackGroundBinding::inflate) {
    override fun setUi() {
        val navController = (childFragmentManager.findFragmentById(R.id.background_item_frame_layout) as NavHostFragment).navController
        binding.backgroundNavigation.setupWithNavController(navController)
    }

    override fun setUpDate() {
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
    }
}