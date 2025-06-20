package com.domain.usecase

import com.domain.model.ImageData
import com.domain.repository.Repository
import javax.inject.Inject

class InitImageDataUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke() = repository.getImageData() ?: repository.insertImageData(ImageData())
}

class ObserveImageDataUseCase @Inject constructor(private val repository: Repository) {
    operator fun invoke() = repository.getImageDataFlow()
}

class GetImageDataUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke() = repository.getImageData()
}

class UpdateImageDataBackgroundScaleUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(scale: Float) = repository.updateImageScale(scale)
}

class UpdateImageDataBackgroundColorUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(color: Int) = repository.updateImageBackgroundColor(color)
}

class UpdateImageDataBackgroundImageUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(url: String) = repository.updateBackgroundImage(url)
}

class GetBackGroundImageUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(keyword: String) = repository.getBackgroundImage(keyword)
}

class UpdateImageUriUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(uri: String) = repository.updateImageUri(uri)
}