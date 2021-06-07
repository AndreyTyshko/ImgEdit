package com.example.imgedit.di

import com.example.imgedit.repository.OperationsRepository
import com.example.imgedit.repository.usecase.DeleteOperationUseCase
import com.example.imgedit.repository.usecase.GetAllOperationsUseCase
import com.example.imgedit.repository.usecase.UpsertOperationUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainModule {

    @Singleton
    @Provides
    fun provideDeleteOperationUseCase(
        operationsRepository: OperationsRepository
    ): DeleteOperationUseCase = DeleteOperationUseCase(operationsRepository)

    @Singleton
    @Provides
    fun provideGetAllOperationsUseCase(
        operationsRepository: OperationsRepository
    ): GetAllOperationsUseCase = GetAllOperationsUseCase(operationsRepository)

    @Singleton
    @Provides
    fun provideUpsertUseCase(
        operationsRepository: OperationsRepository
    ): UpsertOperationUseCase = UpsertOperationUseCase(operationsRepository)


}