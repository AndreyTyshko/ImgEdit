package com.example.imgedit.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imgedit.dataBase.entity.EditedImageModel
import com.example.imgedit.repository.usecase.*
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*
import javax.inject.Inject


class MainActivityViewModel @Inject constructor(
    private val deleteOperationUseCase: DeleteOperationUseCase,
    private val upsertOperationUseCase: UpsertOperationUseCase,
    private val getAllOperationsUseCase: GetAllOperationsUseCase,
    // private val flipOperationUseCase: FlipOperationUseCase,
    private val rotateImageUseCase: RotateImageUseCase,
    private val invertImageUseCase: InvertImageUseCase,
    private val imageFlipHorizontalUseCase: ImageFlipHorizontalUseCase,
    private val context: Context
) : ViewModel() {

    var changedImage: MutableLiveData<Bitmap> = MutableLiveData()


    fun getAllOperations() = getAllOperationsUseCase


    fun rotate(bitmap: Bitmap, angle: Float) {
        viewModelScope.launch {
            val id = System.currentTimeMillis() / 1000
            //val resultedImage =
            changedImage.postValue(rotateImageUseCase.invoke(bitmap, angle))
        }
    }


    fun invertColors(bitmap: Bitmap) {
        viewModelScope.launch {
            val id = System.currentTimeMillis() / 1000
            changedImage.postValue(invertImageUseCase.invoke(bitmap))
        }
    }

    fun imageFlipHorizontal(bitmap: Bitmap, sx: Float, sy: Float) {
        viewModelScope.launch {

            /*val id = System.currentTimeMillis() / 1000
            val bitmap: Bitmap = imageFlipHorizontalUseCase.invoke(bitmap, sx, sy)*/
            changedImage.postValue(imageFlipHorizontalUseCase(bitmap, sx, sy))


            /*changedImage.postValue(
                Bitmap.createBitmap(
                    bitmap,
                    0,
                    0,
                    bitmap.width,
                    bitmap.height,
                    matrix,
                    true
                )
            )*/
        }

    }


    companion object {
        private const val COORDINATION_SX = -1.0f
        private const val COORDINATION_SY = 1.0f
    }

}






