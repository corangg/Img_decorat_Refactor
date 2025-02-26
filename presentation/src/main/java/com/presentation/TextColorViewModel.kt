package com.presentation

import com.core.di.DefaultDispatcher
import com.core.di.IoDispatcher
import com.core.di.MainDispatcher
import com.core.viewmodel.BaseViewModel
import com.domain.usecase.UpdateTextBackgroundColorUseCase
import com.domain.usecase.UpdateTextColorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class TextColorViewModel @Inject constructor(
    private val updateTextColorUseCase: UpdateTextColorUseCase,
    private val updateTextBackgroundColorUseCase: UpdateTextBackgroundColorUseCase,
    @MainDispatcher mainDispatcher: MainCoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseViewModel(mainDispatcher, defaultDispatcher, ioDispatcher) {
    fun setTextColor(color: Int) = onIoWork { updateTextColorUseCase(color) }
    fun setTextBackgroundColor(color: Int) = onIoWork { updateTextBackgroundColorUseCase(color) }
}