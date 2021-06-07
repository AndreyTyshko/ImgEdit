package com.example.imgedit.repository.usecase

import com.example.imgedit.repository.OperationsRepository
import com.example.imgedit.dataBase.entity.EditedImageModel
import javax.inject.Inject

class DeleteOperationUseCase @Inject constructor(private val operationsRepository: OperationsRepository) {
    suspend operator fun invoke(editedImageModel: EditedImageModel){
        operationsRepository.deleteOperation(editedImageModel)
    }
}