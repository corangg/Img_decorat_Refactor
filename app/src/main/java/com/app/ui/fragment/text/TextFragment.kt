package com.app.ui.fragment.text

import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.app.R
import com.app.databinding.FragmentTextBinding
import com.core.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TextFragment : BaseFragment<FragmentTextBinding>(FragmentTextBinding::inflate) {
    override fun setUi() {
        val navController = (childFragmentManager.findFragmentById(R.id.text_item_frame_layout) as NavHostFragment).navController
        binding.textNavigation.setupWithNavController(navController)
    }

    override fun setUpDate() {

    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {

    }
}