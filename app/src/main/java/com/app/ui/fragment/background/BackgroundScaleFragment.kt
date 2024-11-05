package com.app.ui.fragment.background

import androidx.lifecycle.LifecycleOwner
import com.app.config.backGroundScaleList
import com.app.databinding.FragmentBackgroundScaleBinding
import com.app.recyclerview.BackgroundScaleAdapter
import com.core.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BackgroundScaleFragment : BaseFragment<FragmentBackgroundScaleBinding>(FragmentBackgroundScaleBinding::inflate) {
    private var backgroundScaleAdapter: BackgroundScaleAdapter? = null

    override fun setUi() {
        binding.backgroundScaleRecycler.run {
            backgroundScaleAdapter = BackgroundScaleAdapter()
            adapter = backgroundScaleAdapter
            backgroundScaleAdapter?.submitList(backGroundScaleList)
        }
    }

    override fun setUpDate() {

    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {

    }
}