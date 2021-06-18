package com.example.imgedit.di

import com.example.imgedit.repository.OperationsRepository
import com.example.imgedit.repository.usecase.*
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

//
//    @Singleton
//    @Provides
//    fun provideFlipOperationUseCase(
//        operationsRepository: OperationsRepository
//    ): UpsertOperationUseCase = UpsertOperationUseCase(operationsRepository)

    @Singleton
    @Provides
    fun provideRotateImageUseCase(
        operationsRepository: OperationsRepository
    ): RotateImageUseCase = RotateImageUseCase(operationsRepository)


    @Singleton
    @Provides
    fun provideInvertImageUseCase(
        operationsRepository: OperationsRepository
    ): InvertImageUseCase = InvertImageUseCase(operationsRepository)

    @Singleton
    @Provides
    fun provideImageFlipHorizontalUseCase(
        operationsRepository: OperationsRepository
    ): ImageFlipHorizontalUseCase = ImageFlipHorizontalUseCase(operationsRepository)

}