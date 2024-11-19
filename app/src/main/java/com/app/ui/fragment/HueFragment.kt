package com.app.ui.fragment

import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.app.databinding.FragmentHueBinding
import com.core.ui.BaseFragment
import com.presentation.HueViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HueFragment : BaseFragment<FragmentHueBinding>(FragmentHueBinding::inflate) {
    private val viewModel: HueViewModel by viewModels()

    override fun setUi() {
        binding.viewModel = viewModel
    }

    override fun setUpDate() {
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
    }
}