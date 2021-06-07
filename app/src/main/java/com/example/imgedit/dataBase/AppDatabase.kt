
package com.example.imgedit.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.imgedit.dataBase.converters.UriConverters
import com.example.imgedit.dataBase.dao.OperationDao
import com.example.imgedit.dataBase.entity.EditedImageModel


@Database(
    entities = [EditedImageModel::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(UriConverters::class)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getOperationsDao(): OperationDao

}
