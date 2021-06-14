package com.example.imgedit.viewmodel

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imgedit.dataBase.entity.EditedImageModel
import com.example.imgedit.repository.usecase.DeleteOperationUseCase
import com.example.imgedit.repository.usecase.GetAllOperationsUseCase
import com.example.imgedit.repository.usecase.RotateImageUseCase
import com.example.imgedit.repository.usecase.UpsertOperationUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainActivityViewModel @Inject constructor(
    private val deleteOperationUseCase: DeleteOperationUseCase,
    private val upsertOperationUseCase: UpsertOperationUseCase,
    private val getAllOperationsUseCase: GetAllOperationsUseCase,
    // private val flipOperationUseCase: FlipOperationUseCase,
    private val rotateImageUseCase: RotateImageUseCase,
    private val context: Context
) : ViewModel() {

    var changedImage: MutableLiveData<Bitmap> = MutableLiveData()
    var changedImageInverted: MutableLiveData<Bitmap> = MutableLiveData()
    var changedImageMirror: MutableLiveData<Bitmap> = MutableLiveData()


    fun upsertOperation(editedImageModel: EditedImageModel) {
        viewModelScope.launch {
            upsertOperationUseCase.invoke(editedImageModel)
        }
    }

    fun getAllOperations() = getAllOperationsUseCase


    fun rotate(bitmap: Bitmap, angle: Float) {
        viewModelScope.launch {
            val id = System.currentTimeMillis() / 1000
            //upsertOperationUseCase.invoke(EditedImageModel(rnds,"Rotated",))
            changedImage.postValue(rotateImageUseCase.invoke(bitmap, angle))
        }
    }

    /* fun invertColors(drawable: Drawable) {
         viewModelScope.launch {
             val matrixInvert = ColorMatrix().apply {
                 setSaturation(0F)
             }
             val filter = ColorMatrixColorFilter(matrixInvert)
             drawable.colorFilter = filter
             changedImageInverted.postValue(drawable)

         }
     }
 */





    fun invertColors(bitmap: Bitmap) {
        viewModelScope.launch {
         /*  var  bitmap = Bitmap.createBitmap(
                bitmap.width,
                bitmap.height,
                Bitmap.Config.ARGB_8888
            )*/
           var matrix = ColorMatrix().apply {
                setSaturation(0f)
            }
           var  filter = ColorMatrixColorFilter(matrix)
           var  paint = Paint().apply {
                colorFilter = filter
            }
            val mutableBitmap: Bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
            Canvas(mutableBitmap).drawBitmap(bitmap,0f,0f,paint)
            changedImageInverted.postValue(Bitmap.createBitmap(mutableBitmap))
        }

        /*//val filter = ColorMatrixColorFilter(matrixInvert)
        //drawable.colorFilter = filter*/

    }

    /*  private fun getGrayscale(bitmap: Bitmap): Bitmap? {

          //Custom color matrix to convert to GrayScale
          val matrix = floatArrayOf(
              0.3f, 0.59f, 0.11f, 0f, 0f,
              0.3f, 0.59f, 0.11f, 0f, 0f,
              0.3f, 0.59f, 0.11f, 0f, 0f, 0f, 0f, 0f, 1f, 0f
          )
          val dest = Bitmap.createBitmap(
              bitmap.width,
              bitmap.height,
              bitmap.config
          )
          val canvas = Canvas(dest)
          val paint = Paint()
          val filter = ColorMatrixColorFilter(matrix)
          paint.colorFilter = filter
          canvas.drawBitmap(bitmap, 0F, 0F, paint)
          return dest
      }*/
    /*fun Bitmap.toGrayscale():Bitmap?{
       val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

       val matrix = ColorMatrix().apply {
           setSaturation(0f)
       }
       val filter = ColorMatrixColorFilter(matrix)

       val paint = Paint().apply {
           colorFilter = filter
       }

       Canvas(bitmap).drawBitmap(this, 0F, 0F, paint)
       return bitmap
   }*/
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

    companion object {
        private const val COORDINATION_SX = -1.0f
        private const val COORDINATION_SY = 1.0f
    }

}






