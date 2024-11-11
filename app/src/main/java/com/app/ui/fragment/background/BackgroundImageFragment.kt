package com.app.ui.fragment.background

import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import com.app.databinding.FragmentBackgroundImageBinding
import com.app.recyclerview.BackgroundImageAdapter
import com.core.ui.BaseFragment
import com.core.util.hideKeyboard
import com.presentation.BackgroundImageViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BackgroundImageFragment :
    BaseFragment<FragmentBackgroundImageBinding>(FragmentBackgroundImageBinding::inflate) {
    private val adapter by lazy { BackgroundImageAdapter() }
    private val viewModel: BackgroundImageViewmodel by viewModels()

    override fun setUi() {
        binding.viewModel = viewModel
        binding.backgroundImageRecycler.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 5, GridLayoutManager.VERTICAL, false)
            adapter = this@BackgroundImageFragment.adapter
        }

        adapter.setOnItemClickListener { item, position ->
            viewModel.setBackgroundImage(item.full)
        }
    }

    override fun setUpDate() {

    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
        viewModel.imageList.observe(lifecycleOwner) {
            adapter.submitList(it)
            context?.hideKeyboard(binding.editImageKeyword)
        }
    }
}