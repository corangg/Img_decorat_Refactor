package com.app.ui.fragment

import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.app.databinding.FragmentMainBinding
import com.core.ui.BaseFragment
import com.presentation.MainFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {
    private val viewModel: MainFragmentViewModel by viewModels()

    override fun setUi() {
    }

    override fun setUpDate() {
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
    }
}