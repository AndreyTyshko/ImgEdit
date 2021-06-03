package com.example.imgedit.view

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import com.example.imgedit.R
import kotlinx.android.synthetic.main.fragment_image_editor_framgnet.*

class ImageEditorFragment : Fragment(R.layout.fragment_image_editor_framgnet) {

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
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) ==
                PackageManager.PERMISSION_GRANTED
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
            Manifest.permission.CAMERA
        ) ==
                PackageManager.PERMISSION_GRANTED
        val result1 = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) ==
                PackageManager.PERMISSION_GRANTED
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
                        grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val storageAccepted =
                        grantResults[1] == PackageManager.PERMISSION_GRANTED
                    if (cameraAccepted && storageAccepted) {
                        pickFromCamera()
                    } else {
                        //TODO
                    }
                }
                if (grantResults.size > 0) {
                    val storageAccepted =
                        grantResults[0] == PackageManager.PERMISSION_GRANTED
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
                drawable = iv_input_img.drawable
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                iv_input_img.setImageURI(image_Uri)
                image_Uri = data!!.data
                theBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, image_Uri)
                drawable = iv_input_img.drawable
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iv_input_img.setOnClickListener {
            showImagePickDialog()
        }


        buttonRotate.setOnClickListener {


            viewModel.rotate(theBitmap!!, 90f)
            viewModel.changedImage.observe(this) {
                imageViewResult.setImageBitmap(it)
            }


        buttonMirrImg.setOnClickListener {
            iv_input_img.rotationY = 180f
        }

        buttonInvColor.setOnClickListener {
            viewModel.invertColors(drawable!!)
            viewModel.changedImageInverted.observe(this) {
                imageViewResult.setImageDrawable(it)
            }
        }

    }

    iv_input_img.setOnClickListener{
        showImagePickDialog()
    }


    buttonRotate.setOnClickListener{


        viewModel.rotate(theBitmap!!, 90f)
        viewModel.changedImage.observe(this) {
            imageViewResult.setImageBitmap(it)
        }
    }

    buttonMirrImg.setOnClickListener
    {
        iv_input_img.rotationY = 180f
    }

    buttonInvColor.setOnClickListener{
        viewModel.invertColors(drawable!!)
        viewModel.changedImageInverted.observe(this) {
            imageViewResult.setImageDrawable(it)
        }
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
        image_Uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
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
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) ==
                PackageManager.PERMISSION_GRANTED
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
            Manifest.permission.CAMERA
        ) ==
                PackageManager.PERMISSION_GRANTED
        val result1 = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) ==
                PackageManager.PERMISSION_GRANTED
        return result && result1
    }

    private fun requestCameraPermission() {
        .storagePermission.let {
            ActivityCompat.requestPermissions(
                requireActivity(),
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
                        grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val storageAccepted =
                        grantResults[1] == PackageManager.PERMISSION_GRANTED
                    if (cameraAccepted && storageAccepted) {
                        pickFromCamera()
                    } else {
                        //TODO
                    }
                }
                if (grantResults.size > 0) {
                    val storageAccepted =
                        grantResults[0] == PackageManager.PERMISSION_GRANTED
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
                drawable = iv_input_img.drawable
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                iv_input_img.setImageURI(image_Uri)
                image_Uri = data!!.data
                theBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, image_Uri)
                drawable = iv_input_img.drawable
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