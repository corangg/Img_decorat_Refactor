package com.presentation

import androidx.lifecycle.MutableLiveData
import com.core.di.DefaultDispatcher
import com.core.di.IoDispatcher
import com.core.di.MainDispatcher
import com.core.viewmodel.BaseViewModel
import com.domain.model.UnSplashUrls
import com.domain.usecase.GetBackGroundImageUseCase
import com.domain.usecase.UpdateImageDataBackgroundImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class BackgroundImageViewmodel @Inject constructor(
    private val getBackGroundImageUseCase: GetBackGroundImageUseCase,
    private val updateImageDataBackgroundImageUseCase: UpdateImageDataBackgroundImageUseCase,
    @MainDispatcher mainDispatcher: MainCoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseViewModel(mainDispatcher, defaultDispatcher, ioDispatcher) {
    val searchKeyword = MutableLiveData("")
    val imageList = MutableLiveData((listOf<UnSplashUrls>()))

    fun searchImage() = onUiWork {
        val keyword = searchKeyword.value ?: return@onUiWork
        imageList.value = getBackGroundImageUseCase(keyword)
    }

    fun deleteImage() = onIoWork {
        updateImageDataBackgroundImageUseCase("")
    }

    fun setBackgroundImage(url: String) = onUiWork {
        updateImageDataBackgroundImageUseCase(url)
    }
}