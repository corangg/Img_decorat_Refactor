package com.app.ui.fragment.text

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.config.colorsList
import com.app.databinding.FragmentTextColorBinding
import com.app.recyclerview.TextColorAdapter
import com.core.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TextColorFragment :
    BaseFragment<FragmentTextColorBinding>(FragmentTextColorBinding::inflate) {
    private val textColorAdapter by lazy { TextColorAdapter() }
    private val textBackgroundColorAdapter by lazy { TextColorAdapter() }
    override fun setUi() {
        setRecyclerView(binding.recyclerTextColor, textColorAdapter)
        setRecyclerView(binding.recyclerTextBackgroundColor, textBackgroundColorAdapter)

        textColorAdapter.setOnItemClickListener { item, position ->
        }
        textBackgroundColorAdapter.setOnItemClickListener { item, position ->
        }
    }

    override fun setUpDate() {

    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {

    }

    private fun setRecyclerView(view: RecyclerView, adapter: TextColorAdapter) {
        view.apply {
            layoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
            this.adapter = adapter
        }
        adapter.submitList(colorsList)
    }
}