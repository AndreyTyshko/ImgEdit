package com.example.imgedit.di

import android.content.Context
import com.example.imgedit.repository.usecase.DeleteOperationUseCase
import com.example.imgedit.repository.usecase.GetAllOperationsUseCase
import com.example.imgedit.repository.usecase.UpsertOperationUseCase
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
        context: Context
    ): MainActivityViewModel = MainActivityViewModel(
        deleteArticleUseCase,
        upsertOperationUseCase,
        getAllOperationsUseCase,
        context
    )
}
