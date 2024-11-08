package com.app.ui.fragment.background

import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import com.app.config.colorsList
import com.app.databinding.FragmentBackgroundColorBinding
import com.app.recyclerview.BackgroundColorAdapter
import com.core.ui.BaseFragment
import com.presentation.BackgroundColorViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BackgroundColorFragment :
    BaseFragment<FragmentBackgroundColorBinding>(FragmentBackgroundColorBinding::inflate) {
    private val adapter by lazy { BackgroundColorAdapter() }
    private val viewModel: BackgroundColorViewModel by viewModels()

    override fun setUi() {
        binding.backgroundColorRecycler.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
            adapter = this@BackgroundColorFragment.adapter
        }
        adapter.submitList(colorsList)

        adapter.setOnItemClickListener { item, position ->
            viewModel.setBackgroundScale(item)
        }
    }

    override fun setUpDate() {

    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {

    }
}