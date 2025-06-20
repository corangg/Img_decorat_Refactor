package com.domain.usecase

import com.domain.model.ViewItemData
import com.domain.repository.Repository
import javax.inject.Inject

class AddViewImageItemData @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(uri: String) {
        val data = ViewItemData(type = 0, img = uri)
        repository.addViewItemData(data)
    }
}

class UpdateViewMatrixUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(data: ViewItemData, id: Int) {
        val imageData = repository.getImageData() ?: return
        val itemDataList = imageData.viewDataInfo.toMutableList()
        if (itemDataList.size > id) {
            val itemData = itemDataList[id].copy(
                matrixValues = data.matrixValues,
                scale = data.scale,
                rotationDegrees = data.rotationDegrees
            )
            itemDataList[id] = itemData

            repository.updateImageData(imageData.copy(viewDataInfo = itemDataList))
        }
    }
}

class UpdateViewVisibilityUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(position: Int, visibility: Boolean) =
        repository.updateCheckImage(position, visibility)
}

class UpdateSwapViewUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(fromPos: Int, toPos: Int) =
        repository.updateSwapImage(fromPos, toPos)
}

class DeleteViewUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(position: Int) = repository.deleteImage(position)
}

class UpdateSelectImageUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(position: Int) = repository.updateSelectImage(position)
}

class ObserveSelectImageDataValueUseCase @Inject constructor(private val repository: Repository) {
    operator fun invoke() =repository.selectImageData()
}

class UpdateSaturationValueUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(value: Float) = repository.updateImageSaturation(value)
}

class UpdateBrightnessValueUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(value: Float) = repository.updateImageBrightness(value)
}

class UpdateTransparencyValueUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(value: Float) = repository.updateImageTransparency(value)
}