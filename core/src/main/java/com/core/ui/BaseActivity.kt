package com.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner

typealias ActivityInflate<T> = (LayoutInflater) -> T

abstract class BaseActivity<VDB : ViewDataBinding>(private val inflater: ActivityInflate<VDB>) :
    AppCompatActivity() {
    protected lateinit var binding: VDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflater(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)

        setUi()
        setUpDate()
        setObserve(this)
    }

    abstract fun setUi()
    abstract fun setUpDate()
    abstract fun setObserve(lifecycleOwner: LifecycleOwner)
}