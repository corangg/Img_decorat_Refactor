package com.presentation

import androidx.lifecycle.MutableLiveData
import com.core.di.DefaultDispatcher
import com.core.di.IoDispatcher
import com.core.di.MainDispatcher
import com.core.viewmodel.BaseViewModel
import com.domain.usecase.UpdateBrightnessValueUseCase
import com.domain.usecase.UpdateSaturationValueUseCase
import com.domain.usecase.UpdateTransparencyValueUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class HueViewModel @Inject constructor(
    private val updateSaturationValueUseCase: UpdateSaturationValueUseCase,
    private val updateBrightnessValueUseCase: UpdateBrightnessValueUseCase,
    private val updateTransparencyValueUseCase: UpdateTransparencyValueUseCase,
    @MainDispatcher mainDispatcher: MainCoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseViewModel(mainDispatcher, defaultDispatcher, ioDispatcher) {
    val textSaturationValue = MutableLiveData("0")
    val saturationValue = MutableLiveData(100)

    val textBrightnessValue = MutableLiveData("0")
    val brightnessValue = MutableLiveData(100)

    val textTransparencyValue = MutableLiveData("100")
    val transparencyValue = MutableLiveData(100)


    init {
        textSaturationValue.observeForever {
            textToSeek(saturationValue, it) {
                onIoWork { updateSaturationValueUseCase(saturationValue.value?.toFloat() ?: 0F) }
            }
        }

        saturationValue.observeForever {
            seekToText(textSaturationValue, it){
                onIoWork { updateSaturationValueUseCase(saturationValue.value?.toFloat() ?: 0F) }
            }
        }

        textBrightnessValue.observeForever {
            textToSeek(brightnessValue,it){
                onIoWork { updateBrightnessValueUseCase(brightnessValue.value?.toFloat()?: 0F) }
            }
        }

        brightnessValue.observeForever {
            seekToText(textBrightnessValue,it){
                onIoWork { updateBrightnessValueUseCase(brightnessValue.value?.toFloat()?: 0F) }
            }
        }

        textTransparencyValue.observeForever {
            if((transparencyValue.value?: 100) != (it.toIntOrNull()?:100)){
                transparencyValue.value = it.toIntOrNull()?:100
                onIoWork { updateTransparencyValueUseCase(transparencyValue.value?.toFloat()?: 0F) }
            }
        }

        transparencyValue.observeForever {
            if(textTransparencyValue.value?.toIntOrNull() != it){
                textTransparencyValue.value = it.toString()
                onIoWork { updateTransparencyValueUseCase(transparencyValue.value?.toFloat()?: 0F) }
            }
        }
    }

    private fun textToSeek(seekLiveData: MutableLiveData<Int>, text: String, update: () -> Unit) {
        if ((seekLiveData.value ?: 100) - 100 != (text.toIntOrNull() ?: 0)) {
            seekLiveData.value = 100 + (text.toIntOrNull() ?: 0)
            update()
        }
    }

    private fun seekToText(textLiveData: MutableLiveData<String>, seek: Int, update: () -> Unit) {
        if(textLiveData.value?.toIntOrNull() != seek - 100){
            textLiveData.value = (seek -100).toString()
            update()
        }
    }
}