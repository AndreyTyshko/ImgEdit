package com.example.imgedit.dataBase.datasource

import androidx.lifecycle.LiveData
import com.example.imgedit.dataBase.dao.OperationDao
import com.example.imgedit.dataBase.entity.EditedImageModel
import javax.inject.Inject

interface LocalHistoryDataSource {

    suspend fun upsertOperation(editedImageModel: EditedImageModel): Long

    fun getAllOperations(): LiveData<List<EditedImageModel>>

    suspend fun deleteOperation(editedImageModel: EditedImageModel)
}

class LocalHistoryDataSourceImpl @Inject constructor(private val operationDao: OperationDao) :
    LocalHistoryDataSource {
    override suspend fun upsertOperation(editedImageModel: EditedImageModel): Long =
        operationDao.upsert(editedImageModel)

    override fun getAllOperations(): LiveData<List<EditedImageModel>> =
        operationDao.getAllOperations()

    override suspend fun deleteOperation(editedImageModel: EditedImageModel) =
        operationDao.deleteAOperation(editedImageModel)

}