package com.example.imgedit.view

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.example.imgedit.R
import com.example.imgedit.viewmodel.MainActivityViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_image_editor_framgnet.*

class ImageEditorFragment : Fragment(R.layout.fragment_image_editor_framgnet) {

    private var currentImage: Uri? = null
    private val storagePermission: Array<String>? = null
    private var adapter:ImageAdapter?=null

    private val viewModel: MainActivityViewModel by lazy {
        MainActivityViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter= ImageAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter?.setOnItemClickListener {
            imageViewResult.setImageURI(it)
        }

        iv_input_img.setOnClickListener {
            showImagePickDialog()
        }


        buttonRotate.setOnClickListener {
            if (currentImage != null) {
                errorMessage()
            } else {
                val bitmap = (iv_input_img.drawable as BitmapDrawable).bitmap
                viewModel.rotate(bitmap, 90f)
                viewModel.changedImage.observe(requireActivity()) {
                    imageViewResult.setImageBitmap(it)
                }
            }
        }

        buttonMirrImg.setOnClickListener {
            if (currentImage != null) {
                errorMessage()
            } else {
                imageViewResult.setImageDrawable(iv_input_img.drawable)
                imageViewResult.rotationY = 180f
            }
        }

        buttonInvColor.setOnClickListener {
            if (currentImage != null) {
                errorMessage()
            } else {
                viewModel.invertColors(iv_input_img.drawable)
                viewModel.changedImageInverted.observe(requireActivity()) {
                    imageViewResult.setImageDrawable(it)
                }
            }

        }

    }

    private fun errorMessage() = Snackbar.make(
        requireActivity().findViewById(android.R.id.content),
        "Error",
        Snackbar.LENGTH_LONG
    ).show()

    private fun showImagePickDialog() {
        val options = arrayOf("Camera", "Gallery")
        val builder = AlertDialog.Builder(requireActivity())
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
        currentImage = requireActivity().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, currentImage)
        startActivityForResult(
            intent,
            IMAGE_PICK_CAMERA_CODE
        )
    }

    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission() {
        storagePermission?.let {
            ActivityCompat.requestPermissions(
                requireActivity(),
                it,
                STORAGE_REQUEST_CODE
            )
        }
    }

    private fun checkCameraPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.CAMERA
        ) ==
                PackageManager.PERMISSION_GRANTED
        val result1 = ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) ==
                PackageManager.PERMISSION_GRANTED
        return result && result1
    }

    private fun requestCameraPermission() {
        storagePermission.let {
            if (it != null) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    it,
                    CAMERA_REQUEST_CODE
                )
            }
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
                currentImage = data!!.data

                iv_input_img.setImageURI(currentImage)
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                iv_input_img.setImageURI(currentImage)
                currentImage = data!!.data
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
