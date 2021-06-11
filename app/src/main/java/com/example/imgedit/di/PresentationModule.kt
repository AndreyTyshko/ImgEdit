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
       // flipOperationUseCase: FlipOperationUseCase,
        rotateImageUseCase: RotateImageUseCase,
        context: Context
    ): MainActivityViewModel = MainActivityViewModel(
        deleteArticleUseCase,
        upsertOperationUseCase,
        getAllOperationsUseCase,
       // flipOperationUseCase,
        rotateImageUseCase,
        context
    )
}
