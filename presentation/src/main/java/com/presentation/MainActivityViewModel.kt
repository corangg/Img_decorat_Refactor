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
import com.domain.usecase.DeleteView
import com.domain.usecase.InitImageDataUseCase
import com.domain.usecase.ObserveImageDataUseCase
import com.domain.usecase.UpdateImageUriUseCase
import com.domain.usecase.UpdateSelectImageUseCase
import com.domain.usecase.UpdateSwapView
import com.domain.usecase.UpdateViewMatrixUseCase
import com.domain.usecase.UpdateViewVisibility
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
    private val updateViewVisibility: UpdateViewVisibility,
    private val updateSwapView: UpdateSwapView,
    private val updateSelectImageUseCase: UpdateSelectImageUseCase,
    private val deleteView: DeleteView,
    private val updateImageUriUseCase: UpdateImageUriUseCase,
    @MainDispatcher mainDispatcher: MainCoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseViewModel(mainDispatcher, defaultDispatcher, ioDispatcher) {
    val imageData = observeImageDataUseCase().asLiveData(viewModelScope.coroutineContext)

    init { onIoWork { initImageDataUseCase() } }

    fun addImageLayer(imageList: List<Uri>) = onIoWork {
        for (i in imageList) {
            addViewImageItemData(i.toString())
        }
    }

    fun updateViewMatrix(data: Pair<Int, ViewItemData>) = onIoWork { updateViewMatrixUseCase(data.second, data.first) }

    fun checkImageLayer(position: Int, checked: Boolean) = onIoWork { updateViewVisibility(position, checked) }

    fun swapImageLayer(fromPos: Int, toPos: Int) = onIoWork { updateSwapView(fromPos, toPos) }

    fun selectImage(position: Int) = onIoWork { updateSelectImageUseCase(position) }

    fun deleteImageLayer(position: Int) = onIoWork { deleteView(position) }

    fun updateImageUri(uri: String) = onIoWork { updateImageUriUseCase(uri) }
}