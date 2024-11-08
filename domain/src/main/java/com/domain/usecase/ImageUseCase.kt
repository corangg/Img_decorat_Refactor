package com.domain.usecase

import com.domain.model.ImageData
import com.domain.repository.Repository
import javax.inject.Inject

class InsertImageDataUseCase {
}

class InitImageDataUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke() =
        repository.getImageData() ?: repository.insertImageData(ImageData())
}

class ObserveImageDataUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke() = repository.getTemporaryStorageImageDataFlow()
}

class UpdateImageDataBackgroundScaleUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(scale: Float) = repository.updateImageScale(scale)
}

class UpdateImageDataBackgroundColorUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(color: Int) = repository.updateImageBackgroundColor(color)
}