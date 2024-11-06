package com.app.ui.fragment.background

import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import com.app.config.backGroundScaleList
import com.app.databinding.FragmentBackgroundScaleBinding
import com.app.recyclerview.BackgroundScaleAdapter
import com.core.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BackgroundScaleFragment :
    BaseFragment<FragmentBackgroundScaleBinding>(FragmentBackgroundScaleBinding::inflate) {
    private val adapter by lazy { BackgroundScaleAdapter() }

    override fun setUi() {
        binding.backgroundScaleRecycler.apply {
            layoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false)
            adapter = this@BackgroundScaleFragment.adapter
        }
        adapter.submitList(backGroundScaleList)

        adapter.setOnItemClickListener { item, position ->
            val a = item.scale
            a
        }
    }

    override fun setUpDate() {

    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {

    }
}