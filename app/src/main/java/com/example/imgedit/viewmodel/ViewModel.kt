package com.example.imgedit.viewmodel

import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imgedit.dataBase.dao.OperationDao

import com.example.imgedit.database.entity.EditedImageModel
import kotlinx.coroutines.launch


class MainActivityViewModel : ViewModel() {

    var changedImage: MutableLiveData<Bitmap> = MutableLiveData()
    var changedImageInverted: MutableLiveData<Drawable> = MutableLiveData()
    private var dao: OperationDao?=null


    fun rotate(bitmap: Bitmap, angle: Float) {
        viewModelScope.launch {
            val matrix = Matrix()
            matrix.postRotate(angle)
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

    fun mirror() {

    }

}
