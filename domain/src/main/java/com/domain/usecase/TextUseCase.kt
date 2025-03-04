package com.domain.usecase

import com.domain.model.ViewItemData
import com.domain.repository.Repository
import javax.inject.Inject

class AddViewTextItemData @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke() {
        val data = ViewItemData(type = 1)
        repository.addViewItemData(data)
    }
}

class UpdateTextValue @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(data: ViewItemData, id: Int) {
        val imageData = repository.getImageData() ?: return
        val itemDataList = imageData.viewDataInfo.toMutableList()
        if (itemDataList.size > id) {
            val itemData = itemDataList[id].copy(
                matrixValues = data.matrixValues,
                scale = data.scale,
                rotationDegrees = data.rotationDegrees,
                text = data.text
            )
            itemDataList[id] = itemData

            repository.updateImageData(imageData.copy(viewDataInfo = itemDataList))
        }
    }
}

class UpdateTextSize @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(size: Int) = repository.updateTextSize(size)
}

class UpdateTextColorUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(color: Int) = repository.updateTextColor(color)
}

class UpdateTextBackgroundColorUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(color: Int) = repository.updateTextBackgroundColor(color)
}

class DownloadGoogleFontListUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke() = repository.downloadGoogleFontList()
}

class GetFontListUseCase @Inject constructor(private val repository: Repository) {
    operator fun invoke() = repository.getFontPath()
}

class UpdateTextFontUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(fontPath: String) = repository.updateTextFont(fontPath)
}