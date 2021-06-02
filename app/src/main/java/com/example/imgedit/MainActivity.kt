package com.example.imgedit


import android.Manifest.permission.CAMERA
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Bitmap
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var image_Uri: Uri? = null
    private var theBitmap: Bitmap? = null
    private var storagePermission: Array<String>? = null

    private val viewModel: MainActivityViewModel by lazy {
        MainActivityViewModel()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        iv_input_img.setOnClickListener {
            showImagePickDialog()
        }

        viewModel.changedImage

        buttonRotate.setOnClickListener {
            viewModel.rotate(theBitmap!!, 90f)
            viewModel.changedImage.observe(this, {
                imageViewResult.setImageBitmap(it)
            })
        }

       /* buttonInvColor.setOnClickListener {
            viewModel.invertColors(theBitmap!!)
            viewModel.changedImage.observe(this, {
                imageViewResult.setImageBitmap(it)
            })
        }*/


        buttonInvColor.setOnClickListener {

            imageViewResult.setImageURI(image_Uri)
            val myDrawable = imageViewResult.drawable
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

            myDrawable.colorFilter = filter
            imageViewResult.setImageDrawable(myDrawable)
            imageViewResult.invalidate()


        }

    }


    private fun showImagePickDialog() {
        val options = arrayOf("Camera", "Gallery")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Выберите изображение")
            .setItems(options) { _, which ->
                if (which == 0) {
                    if (checkCameraPermission()) {
                        pickFromCamera()
                    } else {
                        requestCameraPermission()
                    }
                } else {
                    if (checkStoragePermission()) {
                        pickFromGallery()
                    } else {
                        requestStoragePermission()
                    }
                }
            }
            .show()
    }

    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(
            intent,
            IMAGE_PICK_GALLERY_CODE
        )
    }

    private fun pickFromCamera() {
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_Image Title")
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image Description")
        image_Uri =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_Uri)
        startActivityForResult(
            intent,
            IMAGE_PICK_CAMERA_CODE
        )
    }

    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            WRITE_EXTERNAL_STORAGE
        ) ==
                PERMISSION_GRANTED
    }

    private fun requestStoragePermission() {
        storagePermission?.let {
            ActivityCompat.requestPermissions(
                this,
                it,
                STORAGE_REQUEST_CODE
            )
        }
    }

    private fun checkCameraPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            this,
            CAMERA
        ) ==
                PERMISSION_GRANTED
        val result1 = ContextCompat.checkSelfPermission(
            this,
            WRITE_EXTERNAL_STORAGE
        ) ==
                PERMISSION_GRANTED
        return result && result1
    }

    private fun requestCameraPermission() {
        storagePermission?.let {
            ActivityCompat.requestPermissions(
                this,
                it,
                CAMERA_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    val cameraAccepted =
                        grantResults[0] == PERMISSION_GRANTED
                    val storageAccepted =
                        grantResults[1] == PERMISSION_GRANTED
                    if (cameraAccepted && storageAccepted) {
                        pickFromCamera()
                    } else {
                        //TODO
                    }
                }
                if (grantResults.size > 0) {
                    val storageAccepted =
                        grantResults[0] == PERMISSION_GRANTED
                    if (storageAccepted) {
                        pickFromGallery()
                    } else {

                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                image_Uri = data!!.data
                theBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, image_Uri)
                iv_input_img.setImageURI(image_Uri)
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                iv_input_img.setImageURI(image_Uri)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    companion object {

        private val CAMERA_REQUEST_CODE = 100
        private val STORAGE_REQUEST_CODE = 200
        private val IMAGE_PICK_GALLERY_CODE = 300
        private val IMAGE_PICK_CAMERA_CODE = 400

    }

}




