package com.img_decorat_refactor.ui

import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import com.core.ui.BaseActivity
import com.img_decorat_refactor.databinding.ActivityMainBinding
import com.presentation.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private val viewModel: MainActivityViewModel by viewModels()

    override fun setUi() {
        TODO("Not yet implemented")
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
        TODO("Not yet implemented")
    }

    override fun setUpDate() {
        TODO("Not yet implemented")
    }
}