package com.example.imgedit.viewmodel

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.os.Environment.DIRECTORY_PICTURES
import android.os.Environment.getExternalStoragePublicDirectory
import android.provider.MediaStore
import android.util.Log
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
    private val context: Context
) : ViewModel() {

    var changedImage: MutableLiveData<Bitmap> = MutableLiveData()


    fun getAllOperations() = getAllOperationsUseCase


    fun rotate(bitmap: Bitmap, angle: Float) {
        viewModelScope.launch {
            val id = System.currentTimeMillis() / 1000
            val resultedImage = rotateImageUseCase.invoke(bitmap, angle)
            changedImage.postValue(resultedImage)
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
            //flipOperationUseCase.invoke(bitmap, COORDINATION_SX, COORDINATION_SY)
            val matrix = Matrix()
            matrix.preScale(sx, sy)
            changedImage.postValue(
                Bitmap.createBitmap(
                    bitmap,
                    0,
                    0,
                    bitmap.width,
                    bitmap.height,
                    matrix,
                    true
                )
            )
        }

    }


    companion object {
        private const val COORDINATION_SX = -1.0f
        private const val COORDINATION_SY = 1.0f
    }

}






