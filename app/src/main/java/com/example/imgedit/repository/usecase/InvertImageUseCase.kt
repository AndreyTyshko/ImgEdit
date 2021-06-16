package com.example.imgedit.repository.usecase

import android.graphics.Bitmap
import com.example.imgedit.repository.OperationsRepository
import javax.inject.Inject

class InvertImageUseCase @Inject constructor(private val operationsRepository: OperationsRepository) {
    suspend operator fun invoke(bitmap: Bitmap): Bitmap=
        operationsRepository.invertColors(bitmap)
}