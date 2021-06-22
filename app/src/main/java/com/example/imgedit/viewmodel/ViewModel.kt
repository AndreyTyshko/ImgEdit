package com.example.imgedit.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imgedit.dataBase.entity.EditedImageModel
import com.example.imgedit.repository.usecase.*
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


class MainActivityViewModel @Inject constructor(
    private val deleteOperationUseCase: DeleteOperationUseCase,
    private val upsertOperationUseCase: UpsertOperationUseCase,
    private val getAllOperationsUseCase: GetAllOperationsUseCase,
    private val rotateImageUseCase: RotateImageUseCase,
    private val invertImageUseCase: InvertImageUseCase,
    private val imageFlipHorizontalUseCase: ImageFlipHorizontalUseCase,
    private val context: Context
) : ViewModel() {

    var changedImageRotate: MutableLiveData<Bitmap> = MutableLiveData()


    fun getAllOperations() = getAllOperationsUseCase

    fun upsertOperation(bitmap: Bitmap, operationName: String) {
        viewModelScope.launch {
            val id = System.currentTimeMillis() / 1000
            val editedImageModel = EditedImageModel(id.toInt(), operationName, getImageUri(bitmap, id.toInt()))
            upsertOperationUseCase.invoke(editedImageModel)
        }
    }

    fun deleteOperation(editedImageModel: EditedImageModel){
        viewModelScope.launch {
            deleteOperationUseCase.invoke(editedImageModel)
        }
    }


    fun rotate(bitmap: Bitmap, angle: Float) {
        viewModelScope.launch {
            val resultedImage = rotateImageUseCase.invoke(bitmap, angle)
            upsertOperation(resultedImage, "Operation rotate")
            changedImageRotate.postValue(resultedImage)
        }
    }

    fun invertColors(bitmap: Bitmap) {
        viewModelScope.launch {
            val resultedImage = invertImageUseCase.invoke(bitmap)
            upsertOperation(resultedImage, "Operation invert")
            changedImageRotate.postValue(resultedImage)
        }
    }

    fun imageFlipHorizontal(bitmap: Bitmap, sx: Float, sy: Float) {
        viewModelScope.launch {
            val resultedImage = imageFlipHorizontalUseCase(bitmap, sx, sy)
            upsertOperation(resultedImage, "Operation flip")
            changedImageRotate.postValue(resultedImage)

        }
    }

    private fun getImageUri(bitmap: Bitmap, id: Int): Uri {
        val file = getFileFromBitmap(bitmap, id)
        return Uri.parse(file.toURI().toString())
    }


    private fun getFileFromBitmap(bitmap: Bitmap, id: Int): File {
        val file = File(context.cacheDir, "$id.png")
        val fos = FileOutputStream(file)
        fos.use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos) }
        file.setReadable(true, false)
        return file
    }

    companion object {
        private const val COORDINATION_SX = -1.0f
        private const val COORDINATION_SY = 1.0f
    }

}






