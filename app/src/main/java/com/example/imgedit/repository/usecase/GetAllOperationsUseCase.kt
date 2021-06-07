package com.example.imgedit.repository.usecase

import androidx.lifecycle.LiveData
import com.example.imgedit.dataBase.entity.EditedImageModel
import com.example.imgedit.repository.OperationsRepository
import javax.inject.Inject

class GetAllOperationsUseCase @Inject constructor(private val operationsRepository: OperationsRepository) {
     operator fun invoke(): LiveData<List<EditedImageModel>> =
        operationsRepository.getAllOperations()
}