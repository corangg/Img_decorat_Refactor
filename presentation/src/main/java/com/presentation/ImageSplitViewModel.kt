package com.presentation

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.core.di.DefaultDispatcher
import com.core.di.IoDispatcher
import com.core.di.MainDispatcher
import com.core.ui.custom.SplitCircleView
import com.core.ui.custom.SplitPolygonView
import com.core.ui.custom.SplitSquareView
import com.core.viewmodel.BaseViewModel
import com.domain.usecase.CutCircleImageView
import com.domain.usecase.CutPolygonImageView
import com.domain.usecase.CutSquareImageView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import java.util.Stack
import javax.inject.Inject

@HiltViewModel
class ImageSplitActivityViewModel @Inject constructor(
    private val cutSquareImageView: CutSquareImageView,
    private val cutCircleImageView: CutCircleImageView,
    private val cutPolygonImageView: CutPolygonImageView,
    @MainDispatcher mainDispatcher: MainCoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseViewModel(mainDispatcher, defaultDispatcher, ioDispatcher) {
    val polygonPoint = MutableLiveData(3)

    private val _stackBack = MutableLiveData<Stack<Bitmap>>(Stack())
    val stackBack: LiveData<Stack<Bitmap>> get() = _stackBack

    private val _stackNext = MutableLiveData<Stack<Bitmap>>(Stack())
    val stackNext: LiveData<Stack<Bitmap>> get() = _stackNext

    fun closeSplitActivity() {

    }

    fun clearNextStack(){
        _stackNext.value = Stack()
    }

    fun getBackLastStack():Bitmap?{
        val stack = _stackBack.value ?: Stack()
        if(stack.isEmpty()){
            return null
        }
        val popBitmap = stack.pop()
        _stackBack.value = stack
        return popBitmap
    }

    fun getNextLastStack():Bitmap? {
        val stack = _stackNext.value ?: Stack()
        if(stack.isEmpty()){
            return null
        }
        val popBitmap = stack.pop()
        _stackNext.value = stack
        return popBitmap
    }

    fun pushBackStack(bitmap: Bitmap?) {
        bitmap ?: return
        val stack = _stackBack.value ?: Stack()
        val copiedBitmap = bitmap.copy(bitmap.config, true)
        stack.push(copiedBitmap)
        _stackBack.value = stack
    }

    fun pushNextStack(bitmap: Bitmap?){
        bitmap ?: return
        val stack = _stackNext.value ?: Stack()
        val copiedBitmap = bitmap.copy(bitmap.config, true)
        stack.push(copiedBitmap)
        _stackNext.value = stack
    }

    fun cutSquareImage(splitAreaView: SplitSquareView, bitmap: Bitmap?) =
        cutSquareImageView(splitAreaView, bitmap)


    fun cutCircleImage(circleView: SplitCircleView, bitmap: Bitmap?) =
        cutCircleImageView(circleView, bitmap)

    fun cutPolygonImage(splitAreaView: SplitPolygonView, bitmap: Bitmap?) =
        cutPolygonImageView(splitAreaView, bitmap)
}