package com.example.imgedit.repository

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import com.example.imgedit.dataBase.entity.EditedImageModel

interface OperationsRepository {

    suspend fun upsertOperation(editedImageModel: EditedImageModel): Long

    fun getAllOperations(): LiveData<List<EditedImageModel>>

    suspend fun deleteOperation(editedImageModel: EditedImageModel)

    //suspend fun flipOperation(editedImageModel: EditedImageModel)//исправить

    suspend fun rotate(bitmap: Bitmap, angel: Float): Bitmap

    suspend fun invertColors (bitmap: Bitmap):Bitmap

}