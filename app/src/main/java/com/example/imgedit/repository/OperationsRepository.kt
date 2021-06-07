package com.example.imgedit.repository

import androidx.lifecycle.LiveData
import com.example.imgedit.dataBase.entity.EditedImageModel

interface OperationsRepository {

    suspend fun upsertOperation(editedImageModel: EditedImageModel): Long

    fun getAllOperations(): LiveData<List<EditedImageModel>>

    suspend fun deleteOperation(editedImageModel: EditedImageModel)

}