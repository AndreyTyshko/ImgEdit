package com.example.imgedit.view

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imgedit.MainActivity
import com.example.imgedit.R
import com.example.imgedit.dataBase.entity.EditedImageModel
import com.example.imgedit.viewmodel.MainActivityViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_image_editor_framgnet.*
import java.io.File
import java.io.FileOutputStream


class ImageEditorFragment : Fragment(R.layout.fragment_image_editor_framgnet) {

    private var currentImage: Uri? = null
    private val storagePermission: Array<String>? = null
    private var imageAdapter: ImageAdapter? = null

    lateinit var viewModel: MainActivityViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        bindRV()

        imageAdapter?.setOnItemClickListener {
            imageViewResult.setImageURI(it)
        }

        iv_input_img.setOnClickListener {
            showImagePickDialog()
        }

        buttonRotate.setOnClickListener {
            if (!hasImage(iv_input_img)) {
                showImagePickDialog()
            } else {
                val bitmap = (iv_input_img.drawable as BitmapDrawable).bitmap
                viewModel.rotate(bitmap, 90f)
                viewModel.changedImageRotate.observe(viewLifecycleOwner) {
                    imageViewResult.setImageBitmap(it) }
            }
        }

        buttonInvColor.setOnClickListener {
            if (!hasImage(iv_input_img)) {
                showImagePickDialog()
            } else {

                val bitmap = (iv_input_img.drawable as BitmapDrawable).bitmap
                viewModel.invertColors(bitmap)
                viewModel.changedImageRotate.observe(viewLifecycleOwner) {
                    imageViewResult.setImageBitmap(it) }
            }
        }

        buttonMirrImg.setOnClickListener {
            if (!hasImage(iv_input_img)) {
                showImagePickDialog()
            } else {

                val bitmap = (iv_input_img.drawable as BitmapDrawable).bitmap
                viewModel.imageFlipHorizontal(bitmap, -1.0f, 1.0f)
                viewModel.changedImageRotate.observe(viewLifecycleOwner) {
                    imageViewResult.setImageBitmap(it) }
                }
        }

        viewModel.getAllOperations().invoke().observe(viewLifecycleOwner) {
            imageAdapter?.differ?.submitList(it)
        }

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val operation = imageAdapter?.differ!!.currentList[position]
                viewModel.deleteOperation(operation)
                Snackbar.make(view, "Операция удалена!", Snackbar.LENGTH_LONG).show()
                }
            }

        ItemTouchHelper(itemTouchHelper).apply {
            attachToRecyclerView(recyclerView)
        }

        viewModel.getAllOperations().invoke().observe(viewLifecycleOwner) {
        imageAdapter?.differ?.submitList(it)
    }

    }

    private fun bindRV() {
        imageAdapter = ImageAdapter()
        recyclerView.apply {
            adapter = imageAdapter
            layoutManager = LinearLayoutManager(activity)
            scroll()
        }
    }

    private fun scroll(){
        imageAdapter?.itemCount?.minus(1)?.let { recyclerView.scrollToPosition(it) }
    }

    private fun hasImage(@NonNull view: ImageView): Boolean {
        val drawable: Drawable? = view.drawable
        var hasImage = drawable != null
        if (hasImage && drawable is BitmapDrawable) {
            hasImage = (drawable as BitmapDrawable?)!!.bitmap != null
        }
        return hasImage
    }

/*

    private fun errorMessage() = Snackbar.make(
        requireActivity().findViewById(android.R.id.content),
        "Error",
        Snackbar.LENGTH_LONG
    ).show()*/

    private fun showImagePickDialog() {
        val options = arrayOf("Camera", "Gallery")
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Выберите изображение")
            .setItems(options) { _, which ->
                if (which == 0) {
                    if (checkPermissionCamera()) {
                        pickFromCamera()
                    } else {
                        requestPermissionCamera()
                    }
                } else {
                    if (checkPermissionStorage()) {
                        pickFromGallery()
                    } else {
                        requestPermissionStorage()
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


    val REQUEST_IMAGE_CAPTURE = 1

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
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
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE)
    }

    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }


    private fun checkPermissionCamera(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        )
        return result == PackageManager.PERMISSION_GRANTED
    }


    private fun checkPermissionStorage(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissionCamera() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.CAMERA
            )
        ) {
            Toast.makeText(
                context,
                "Camera permission allows us to access location data. Please allow in App Settings for additional functionality.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE
            )
        }
    }


    private fun requestPermissionStorage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            Toast.makeText(
                context,
                "Camera permission allows us to access location data. Please allow in App Settings for additional functionality.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                STORAGE_REQUEST_CODE
            )
        }
    }


/*    override fun onRequestPermissionsResult(
       requestCode: Int,
       permissions: Array<String>,
       grantResults: IntArray
   ) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(
                    requireView(),
                    "Permission Granted, Now you can access location data.",
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                Snackbar.make(
                    requireView(),
                    "Permission Denied, You cannot access location data.",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }*/


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Camera Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Camera Permission Denied", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == STORAGE_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Storage Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Storage Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun requestStoragePermission() {
        storagePermission?.let {
            ActivityCompat.requestPermissions(
                requireActivity(), it, STORAGE_REQUEST_CODE
            )
        }
    }

    private fun checkCameraPermission(): Boolean {
        val result =
            ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) ==
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

    /*  override fun onRequestPermissionsResult(
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
      }*/


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

        private const val CAMERA_REQUEST_CODE = 100
        private const val STORAGE_REQUEST_CODE = 200
        private const val IMAGE_PICK_GALLERY_CODE = 300
        private const val IMAGE_PICK_CAMERA_CODE = 400

    }


}
