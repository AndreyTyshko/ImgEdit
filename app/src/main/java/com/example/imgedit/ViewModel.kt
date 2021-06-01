package com.example.imgedit

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    var changedImage: MutableLiveData<Bitmap> = MutableLiveData()


    fun rotate(bitmap: Bitmap, angle: Float){
       viewModelScope.launch {
           val matrix = Matrix()
           matrix.postRotate(angle)
           changedImage.postValue(Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true))
       }
    }

    fun mirror(bitmap: Bitmap){
        viewModelScope.launch {

        }
    }

    fun invertColors(){

    }

}