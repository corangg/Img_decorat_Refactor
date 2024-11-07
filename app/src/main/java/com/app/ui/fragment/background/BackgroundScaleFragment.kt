package com.app.ui.fragment.background

import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import com.app.config.backGroundScaleList
import com.app.databinding.FragmentBackgroundScaleBinding
import com.app.recyclerview.BackgroundScaleAdapter
import com.core.ui.BaseFragment
import com.presentation.BackgroundScaleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BackgroundScaleFragment :
    BaseFragment<FragmentBackgroundScaleBinding>(FragmentBackgroundScaleBinding::inflate) {
    private val adapter by lazy { BackgroundScaleAdapter() }
    private val viewModel: BackgroundScaleViewModel by viewModels()

    override fun setUi() {
        binding.backgroundScaleRecycler.apply {
            layoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
            adapter = this@BackgroundScaleFragment.adapter
        }
        adapter.submitList(backGroundScaleList)

        adapter.setOnItemClickListener { item, position ->
            viewModel.setBackgroundScale(item)
        }
    }

    override fun setUpDate() {

    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {

    }
}