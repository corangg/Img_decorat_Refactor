package com.app.ui.fragment

import androidx.lifecycle.LifecycleOwner
import com.app.databinding.FragmentSplitBinding
import com.core.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplitFragment : BaseFragment<FragmentSplitBinding>(FragmentSplitBinding::inflate) {
    override fun setUi() {
    }

    override fun setUpDate() {
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
    }
}