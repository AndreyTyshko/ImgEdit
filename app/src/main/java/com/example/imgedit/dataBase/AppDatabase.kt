package com.example.imgedit.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.imgedit.dataBase.UriConverters
import com.example.imgedit.dataBase.dao.OperationDao
import com.example.imgedit.database.entity.EditedImageModel

@TypeConverters (UriConverters::class)

@Database(
    entities = [EditedImageModel::class],
    version = 1,
    exportSchema = false
)

abstract class AppDataBase : RoomDatabase() {

    abstract fun getOperationsDao(): OperationDao

}
