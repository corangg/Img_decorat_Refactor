package com.presentation

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.core.di.DefaultDispatcher
import com.core.di.IoDispatcher
import com.core.di.MainDispatcher
import com.core.viewmodel.BaseViewModel
import com.domain.usecase.DownloadEmojiUseCase
import com.domain.usecase.GetEmojiUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class StickerViewModel @Inject constructor(
    getEmojiUseCase: GetEmojiUseCase,
    private val downloadEmojiUseCase: DownloadEmojiUseCase,
    @MainDispatcher mainDispatcher: MainCoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseViewModel(mainDispatcher, defaultDispatcher, ioDispatcher) {
    val emojiData = getEmojiUseCase().asLiveData(viewModelScope.coroutineContext)

    fun downloadEmojiList() = onIoWork {
        downloadEmojiUseCase()
    }
}