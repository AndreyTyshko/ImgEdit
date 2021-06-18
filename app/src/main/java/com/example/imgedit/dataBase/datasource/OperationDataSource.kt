package com.example.imgedit.dataBase.datasource

import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import javax.inject.Inject

interface OperationDataSource {

    suspend fun rotate(bitmap: Bitmap, angle: Float): Bitmap

    //    suspend fun inverse(drawable: Drawable): Drawable
//
//    suspend fun flip(drawable: Drawable): Drawable
    suspend fun invertColors(bitmap: Bitmap): Bitmap

    suspend fun imageFlipHorizontal(bitmap: Bitmap, sx:Float, sy:Float):Bitmap
}

class OperationDataSourceImpl : OperationDataSource {

    override suspend fun rotate(bitmap: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

    }

    override suspend fun invertColors(bitmap: Bitmap): Bitmap {


        var matrix = ColorMatrix().apply {
            setSaturation(0f)
        }
        var filter = ColorMatrixColorFilter(matrix)
        var paint = Paint().apply {
            colorFilter = filter
        }
        val mutableBitmap: Bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Canvas(mutableBitmap).drawBitmap(bitmap, 0f, 0f, paint)
        return mutableBitmap
    }


    override suspend fun imageFlipHorizontal(bitmap: Bitmap, sx: Float, sy: Float): Bitmap {
       // TODO("Not yet implemented")
        val matrix = Matrix()
        matrix.preScale(sx, sy)
         return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

    }

}
