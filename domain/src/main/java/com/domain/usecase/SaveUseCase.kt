package com.domain.usecase

import android.view.View
import com.domain.repository.Repository
import javax.inject.Inject

class ImageExtractUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(view: View) = repository.extractFile(view)
}