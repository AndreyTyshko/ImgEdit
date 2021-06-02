package com.example.imgedit

import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class MainActivityViewModel : ViewModel() {

    var changedImage: MutableLiveData<Bitmap> = MutableLiveData()
    var changedImageInverted: MutableLiveData<Drawable> = MutableLiveData()



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
                set(
                    floatArrayOf(
                        -1.0f, 0.0f, 0.0f, 0.0f, 255.0f,
                        0.0f, -1.0f, 0.0f, 0.0f, 255.0f,
                        0.0f, 0.0f, -1.0f, 0.0f, 255.0f,
                        0.0f, 0.0f, 0.0f, 1.0f, 0.0f
                    )
                )
            }
            val filter = ColorMatrixColorFilter(matrixInvert)
            drawable.colorFilter = filter
            changedImageInverted.postValue(drawable)

        }
    }

    fun mirror() {

    }

}