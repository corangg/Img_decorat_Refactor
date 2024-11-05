package com.app.ui.fragment.background

import androidx.lifecycle.LifecycleOwner
import com.app.databinding.FragmentBackgroundImageBinding
import com.core.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BackgroundImageFragment :
    BaseFragment<FragmentBackgroundImageBinding>(FragmentBackgroundImageBinding::inflate) {
    override fun setUi() {
    }

    override fun setUpDate() {

    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {

    }
}