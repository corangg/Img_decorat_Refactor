package com.app.ui.fragment

import androidx.lifecycle.LifecycleOwner
import com.app.databinding.FragmentBackGroundBinding
import com.core.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BackGroundFragment :
    BaseFragment<FragmentBackGroundBinding>(FragmentBackGroundBinding::inflate) {
    override fun setUi() {
    }

    override fun setUpDate() {
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
    }
}