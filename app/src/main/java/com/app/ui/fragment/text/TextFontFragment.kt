package com.app.ui.fragment.text

import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import com.app.databinding.FragmentTextFontBinding
import com.app.recyclerview.TextFontAdapter
import com.core.ui.BaseFragment
import com.presentation.TextFontViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TextFontFragment : BaseFragment<FragmentTextFontBinding>(FragmentTextFontBinding::inflate) {
    private val viewModel: TextFontViewModel by viewModels()
    private val adapter by lazy { TextFontAdapter() }

    override fun setUi() {
        viewModel.downloadGoogleFontList()
        binding.recyclerTextFont.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
            adapter = this@TextFontFragment.adapter
        }
        adapter.setOnItemClickListener { item, position ->
            viewModel.updateTextFont(item)
        }
    }

    override fun setUpDate() {
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
        viewModel.fontList.observe(lifecycleOwner, ::setFont)
    }

    private fun setFont(fontList: List<String>) {
        if (fontList.isEmpty()) {
            viewModel.downloadGoogleFontList()
        } else {
            adapter.submitList(fontList)
        }
    }
}