package com.example.imgedit.di

import android.content.Context
import com.example.imgedit.repository.usecase.*
import com.example.imgedit.viewmodel.MainActivityViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PresentationModule {

    @Singleton
    @Provides
    fun provideOperationsViewModel(
        deleteArticleUseCase: DeleteOperationUseCase,
        upsertOperationUseCase: UpsertOperationUseCase,
        getAllOperationsUseCase: GetAllOperationsUseCase,
        imageFlipHorizontalUseCase: ImageFlipHorizontalUseCase,
        invertImageUseCase: InvertImageUseCase,
        rotateImageUseCase: RotateImageUseCase,
        context: Context
    ): MainActivityViewModel = MainActivityViewModel(
        deleteArticleUseCase,
        upsertOperationUseCase,
        getAllOperationsUseCase,
        rotateImageUseCase,
        invertImageUseCase,
        imageFlipHorizontalUseCase,
        context
    )
}
