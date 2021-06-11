package com.example.imgedit.dataBase.datasource

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import javax.inject.Inject

interface OperationDataSource {

    suspend fun rotate(bitmap: Bitmap, angle: Float): Bitmap

//    suspend fun inverse(drawable: Drawable): Drawable
//
//    suspend fun flip(drawable: Drawable): Drawable
}

class OperationDataSourceImpl: OperationDataSource {

    override suspend fun rotate(bitmap: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        )

    }

//   // override suspend fun inverse(drawable: Drawable): Drawable {
//       // TODO("Not yet implemented")
//    }
//
//    override suspend fun flip(drawable: Drawable): Drawable {
//        TODO("Not yet implemented")
//    }

}
