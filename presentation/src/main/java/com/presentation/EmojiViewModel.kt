package com.presentation

import com.core.di.DefaultDispatcher
import com.core.di.IoDispatcher
import com.core.di.MainDispatcher
import com.core.viewmodel.BaseViewModel
import com.domain.usecase.AddViewImageItemData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class EmojiViewModel @Inject constructor(
    private val addViewImageItemData: AddViewImageItemData,
    @MainDispatcher mainDispatcher: MainCoroutineDispatcher,
    @DefaultDispatcher defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseViewModel(mainDispatcher, defaultDispatcher, ioDispatcher) {
    fun addEmojiLayer(emoji: String) = onIoWork { addViewImageItemData(emojiToUrl(emoji)) }

    private fun emojiToUrl(emoji: String): String {
        val hexCode = emoji.codePoints()
            .mapToObj { Integer.toHexString(it) }
            .reduce { a, b -> "$a-$b" }
            .get()

        return "https://abs.twimg.com/emoji/v2/72x72/$hexCode.png"
    }
}