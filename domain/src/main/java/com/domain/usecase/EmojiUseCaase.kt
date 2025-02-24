package com.domain.usecase

import com.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetEmojiUseCase @Inject constructor(private val repository: Repository) {
    operator fun invoke(): Flow<List<List<String>>> {
        return repository.getEmoji().map { emojiDataList ->
            emojiDataList.groupBy { it.group }.values.map { list -> list.map { it.emoji } }
        }
    }
}

class DownloadEmojiUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke() = repository.downloadEmoji()
}