package com.example.imgedit.di

import android.content.Context
import androidx.room.Room
import com.example.imgedit.dataBase.AppDataBase
import com.example.imgedit.dataBase.dao.OperationDao
import com.example.imgedit.dataBase.datasource.LocalHistoryDataSource
import com.example.imgedit.dataBase.datasource.LocalHistoryDataSourceImpl
import com.example.imgedit.dataBase.repositoryimpl.OperationRepositoryImpl
import com.example.imgedit.repository.OperationsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DataModule {

    @Singleton
    @Provides
    fun provideOperationRepository(
        localNewsDataSource: LocalHistoryDataSource
    ): OperationsRepository =
        OperationRepositoryImpl(localNewsDataSource)

    @Singleton
    @Provides
    fun provideLocalNewsDataSource(operationDao: OperationDao): LocalHistoryDataSource =
        LocalHistoryDataSourceImpl(operationDao)

    @Singleton
    @Provides
    fun provideOperationsDao(context: Context): OperationDao =
        Room.databaseBuilder(
            context.applicationContext,
            AppDataBase::class.java,
            "operations_db.db"
        ).build().getOperationsDao()

}
