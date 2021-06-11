package com.example.imgedit.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imgedit.dataBase.entity.EditedImageModel
import com.example.imgedit.repository.usecase.*
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainActivityViewModel @Inject constructor(
    private val deleteOperationUseCase: DeleteOperationUseCase,
    private val upsertOperationUseCase: UpsertOperationUseCase,
    private val getAllOperationsUseCase: GetAllOperationsUseCase,
   // private val flipOperationUseCase: FlipOperationUseCase,
    private val rotateImageUseCase:RotateImageUseCase,
    private val context: Context
) : ViewModel() {

    var changedImage: MutableLiveData<Bitmap> = MutableLiveData()
    var changedImageInverted: MutableLiveData<Drawable> = MutableLiveData()
    var changedImageMirror: MutableLiveData<Bitmap> = MutableLiveData()


    fun upsertOperation(editedImageModel: EditedImageModel) {
        viewModelScope.launch {
            upsertOperationUseCase.invoke(editedImageModel)
        }
    }

    fun getAllOperations() = getAllOperationsUseCase


    fun rotate(bitmap: Bitmap, angle: Float) {
        viewModelScope.launch {
            val id = System.currentTimeMillis()/1000
            //upsertOperationUseCase.invoke(EditedImageModel(rnds,"Rotated",))
            changedImage.postValue(rotateImageUseCase.invoke(bitmap,angle))
        }
    }

    fun invertColors(drawable: Drawable) {
        viewModelScope.launch {
            val matrixInvert = ColorMatrix().apply {
                setSaturation(0F)
            }
            val filter = ColorMatrixColorFilter(matrixInvert)
            drawable.colorFilter = filter
            changedImageInverted.postValue(drawable)

        }
    }

    fun imageFlipHorizontal(bitmap: Bitmap, sx: Float, sy: Float) {
        viewModelScope.launch {
            //flipOperationUseCase.invoke(bitmap, COORDINATION_SX, COORDINATION_SY)
            val matrix = Matrix()
            matrix.preScale(sx, sy)
            changedImageMirror.postValue(
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
    companion object{
        private const val COORDINATION_SX = -1.0f
        private const val COORDINATION_SY = 1.0f
    }

}


