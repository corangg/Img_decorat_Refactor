package com.presentation

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.core.di.DefaultDispatcher
import com.core.di.IoDispatcher
import com.core.di.MainDispatcher
import com.core.viewmodel.BaseViewModel
import com.domain.usecase.DownloadGoogleFontListUseCase
import com.domain.usecase.GetFontListUseCase
import com.domain.usecase.UpdateTextFontUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class TextFontViewModel @Inject constructor(
    private val downloadGoogleFontListUseCase: DownloadGoogleFontListUseCase,
    private val updateTextFontUseCase: UpdateTextFontUseCase,
    getFontListUseCase: GetFontListUseCase,
    @MainDispatcher mainDispatcher: MainCoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseViewModel(mainDispatcher, defaultDispatcher, ioDispatcher) {

    val fontList = getFontListUseCase().asLiveData(viewModelScope.coroutineContext)

    fun downloadGoogleFontList() = onIoWork { downloadGoogleFontListUseCase() }

    fun updateTextFont(path: String) = onIoWork { updateTextFontUseCase(path) }
}