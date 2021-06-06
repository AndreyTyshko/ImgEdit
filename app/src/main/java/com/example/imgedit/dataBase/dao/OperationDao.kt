package com.example.imgedit.dataBase.dao

import androidx.lifecycle.LiveData
import androidx.room.*

import com.example.imgedit.database.entity.EditedImageModel

@Dao
interface OperationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(editedImageModel: EditedImageModel): Long

    @Query("SELECT * FROM operation")
    fun getAllOperations(): LiveData<List<EditedImageModel>>

    @Delete
    suspend fun deleteAOperation(editedImageModel: EditedImageModel)

}
