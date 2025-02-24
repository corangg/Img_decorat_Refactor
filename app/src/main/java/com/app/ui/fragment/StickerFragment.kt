package com.app.ui.fragment

import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.app.databinding.FragmentStickerBinding
import com.app.recyclerview.BackgroundImageAdapter
import com.core.ui.BaseFragment
import com.presentation.StickerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StickerFragment : BaseFragment<FragmentStickerBinding>(FragmentStickerBinding::inflate) {
    private val viewModel: StickerViewModel by viewModels()
    override fun setUi() {
        viewModel.test()

    }

    override fun setUpDate() {

    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {

    }


}