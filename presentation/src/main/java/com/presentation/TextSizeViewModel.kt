package com.presentation

import androidx.lifecycle.MutableLiveData
import com.core.di.DefaultDispatcher
import com.core.di.IoDispatcher
import com.core.di.MainDispatcher
import com.core.viewmodel.BaseViewModel
import com.domain.usecase.UpdateTextSize
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class TextSizeViewModel @Inject constructor(
    private val updateTextSize: UpdateTextSize,
    @MainDispatcher mainDispatcher: MainCoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseViewModel(mainDispatcher, defaultDispatcher, ioDispatcher) {

    val textSizeValue = MutableLiveData("0")
    val sizeValue = MutableLiveData(0)

    init {
        textSizeValue.observeForever {
            textToSeek(sizeValue, it) {
                onIoWork { updateTextSize(textSizeValue.value?.toInt() ?: 0) }
            }
        }

        sizeValue.observeForever {
            seekToText(textSizeValue, it) {
                onIoWork { updateTextSize(sizeValue.value ?: 0) }
            }
        }
    }

    private fun textToSeek(seekLiveData: MutableLiveData<Int>, text: String, update: () -> Unit) {
        if ((seekLiveData.value ?: 0) != (text.toIntOrNull() ?: 0)) {
            seekLiveData.value = (text.toIntOrNull() ?: 0)
            update()
        }
    }

    private fun seekToText(textLiveData: MutableLiveData<String>, seek: Int, update: () -> Unit) {
        if (textLiveData.value?.toIntOrNull() != seek) {
            textLiveData.value = (seek).toString()
            update()
        }
    }

}