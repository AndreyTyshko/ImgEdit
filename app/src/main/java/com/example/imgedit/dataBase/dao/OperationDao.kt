package com.example.imgedit.dataBase.dao

import androidx.lifecycle.LiveData
import androidx.room.*

import com.example.imgedit.dataBase.entity.EditedImageModel

@Dao
interface OperationDao {
    @Query("SELECT * FROM operation")
    fun getAllOperations(): LiveData<List<EditedImageModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(editedImageModel: EditedImageModel): Long

    @Delete
    suspend fun deleteAOperation(editedImageModel: EditedImageModel)

}
