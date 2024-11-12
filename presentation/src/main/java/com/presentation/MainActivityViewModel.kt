package com.presentation

import android.net.Uri
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.core.di.DefaultDispatcher
import com.core.di.IoDispatcher
import com.core.di.MainDispatcher
import com.core.viewmodel.BaseViewModel
import com.domain.model.ViewItemData
import com.domain.usecase.AddViewImageItemData
import com.domain.usecase.InitImageDataUseCase
import com.domain.usecase.ObserveImageDataUseCase
import com.domain.usecase.UpdateViewMatrixUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    observeImageDataUseCase: ObserveImageDataUseCase,
    private val initImageDataUseCase: InitImageDataUseCase,
    private val addViewImageItemData: AddViewImageItemData,
    private val updateViewMatrixUseCase: UpdateViewMatrixUseCase,
    @MainDispatcher mainDispatcher: MainCoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseViewModel(mainDispatcher, defaultDispatcher, ioDispatcher) {

    val imageData = observeImageDataUseCase().asLiveData(viewModelScope.coroutineContext)

    init {
        onIoWork { initImageDataUseCase() }
    }

    fun addImageLayer(imageList: List<Uri>) = onIoWork {
        for (i in imageList) {
            addViewImageItemData(i.toString())
        }
    }

    fun updateViewMatrix(data: Pair<Int, ViewItemData>) = onIoWork {
        updateViewMatrixUseCase(data.second, data.first)
    }
}