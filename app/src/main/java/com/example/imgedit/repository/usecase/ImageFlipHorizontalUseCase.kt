package com.example.imgedit.repository.usecase

import android.graphics.Bitmap
import com.example.imgedit.repository.OperationsRepository
import javax.inject.Inject



class ImageFlipHorizontalUseCase @Inject constructor(private val operationsRepository: OperationsRepository){
    suspend operator fun invoke (bitmap: Bitmap, sx:Float, sy:Float):Bitmap= operationsRepository.imageFlipHorizontal(bitmap, sx, sy)
}