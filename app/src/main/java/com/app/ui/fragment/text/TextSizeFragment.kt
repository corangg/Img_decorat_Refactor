package com.app.ui.fragment.text

import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.app.databinding.FragmentTextSizeBinding
import com.core.ui.BaseFragment
import com.presentation.TextSizeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TextSizeFragment : BaseFragment<FragmentTextSizeBinding>(FragmentTextSizeBinding::inflate) {
    private val viewModel: TextSizeViewModel by viewModels()

    override fun setUi() {
        binding.viewModel = viewModel
    }

    override fun setUpDate() {
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
    }
}