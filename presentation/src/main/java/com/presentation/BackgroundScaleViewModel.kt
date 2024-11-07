package com.presentation

import com.core.di.DefaultDispatcher
import com.core.di.IoDispatcher
import com.core.di.MainDispatcher
import com.core.viewmodel.BaseViewModel
import com.domain.model.BackgroundScaleItem
import com.domain.usecase.UpdateImageDataScaleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class BackgroundScaleViewModel @Inject constructor(
    private val updateImageDataScaleUseCase: UpdateImageDataScaleUseCase,
    @MainDispatcher mainDispatcher: MainCoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseViewModel(mainDispatcher, defaultDispatcher, ioDispatcher) {

    fun setBackgroundScale(data: BackgroundScaleItem) = onIoWork {
        val scale = data.width.toFloat() / data.height.toFloat()
        updateImageDataScaleUseCase(scale)
    }
}
