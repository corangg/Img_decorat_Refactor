package com.presentation

import com.core.di.DefaultDispatcher
import com.core.di.IoDispatcher
import com.core.di.MainDispatcher
import com.core.viewmodel.BaseViewModel
import com.domain.model.BackgroundScaleItem
import com.domain.usecase.GetBackGroundImageUseCase
import com.domain.usecase.UpdateImageDataBackgroundScaleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class BackgroundScaleViewModel @Inject constructor(
    private val updateImageDataBackgroundScaleUseCase: UpdateImageDataBackgroundScaleUseCase,
    @MainDispatcher mainDispatcher: MainCoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseViewModel(mainDispatcher, defaultDispatcher, ioDispatcher) {

    fun setBackgroundScale(data: BackgroundScaleItem) = onIoWork {
        val scale = data.width.toFloat() / data.height.toFloat()
        updateImageDataBackgroundScaleUseCase(scale)
    }
}
