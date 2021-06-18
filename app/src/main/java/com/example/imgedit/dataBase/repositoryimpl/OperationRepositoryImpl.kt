package com.example.imgedit.dataBase.repositoryimpl

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import com.example.imgedit.dataBase.datasource.LocalHistoryDataSource
import com.example.imgedit.dataBase.datasource.OperationDataSource
import com.example.imgedit.repository.OperationsRepository
import com.example.imgedit.dataBase.entity.EditedImageModel
import javax.inject.Inject

class OperationRepositoryImpl @Inject constructor(
    private val localHistoryDataSource: LocalHistoryDataSource,
    private val operationDataSource: OperationDataSource
) : OperationsRepository {

    override suspend fun upsertOperation(editedImageModel: EditedImageModel): Long =
        localHistoryDataSource.upsertOperation(editedImageModel)

    override fun getAllOperations(): LiveData<List<EditedImageModel>> =
        localHistoryDataSource.getAllOperations()

    override suspend fun deleteOperation(editedImageModel: EditedImageModel) =
        localHistoryDataSource.deleteOperation(editedImageModel)

//    override suspend fun flipOperation(editedImageModel: EditedImageModel) {
//        TODO("Not yet implemented")
//    }

    override suspend fun rotate(bitmap: Bitmap, angle: Float): Bitmap =
        operationDataSource.rotate(bitmap,angle)

    override suspend fun invertColors(bitmap: Bitmap): Bitmap =
        operationDataSource.invertColors(bitmap)

    override suspend fun imageFlipHorizontal(bitmap: Bitmap, sx:Float, sy:Float): Bitmap =
        operationDataSource.imageFlipHorizontal(bitmap,sx, sy)

}