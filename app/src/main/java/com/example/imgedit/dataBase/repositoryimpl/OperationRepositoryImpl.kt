package com.example.imgedit.dataBase.repositoryimpl

import androidx.lifecycle.LiveData
import com.example.imgedit.dataBase.datasource.LocalHistoryDataSource
import com.example.imgedit.repository.OperationsRepository
import com.example.imgedit.dataBase.entity.EditedImageModel
import javax.inject.Inject

class OperationRepositoryImpl @Inject constructor(private val localHistoryDataSource: LocalHistoryDataSource) :
    OperationsRepository {

    override suspend fun upsertOperation(editedImageModel: EditedImageModel): Long =
        localHistoryDataSource.upsertOperation(editedImageModel)

    override fun getAllOperations(): LiveData<List<EditedImageModel>> =
        localHistoryDataSource.getAllOperations()

    override suspend fun deleteOperation(editedImageModel: EditedImageModel) =
        localHistoryDataSource.deleteOperation(editedImageModel)
}