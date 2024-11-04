package com.app.ui

import androidx.activity.viewModels
import androidx.lifecycle.LifecycleOwner
import com.app.databinding.ActivityMainBinding
import com.core.ui.BaseActivity
import com.presentation.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private val viewModel: MainActivityViewModel by viewModels()

    override fun setUi() {
    }

    override fun setObserve(lifecycleOwner: LifecycleOwner) {
    }

    override fun setUpDate() {
    }
}