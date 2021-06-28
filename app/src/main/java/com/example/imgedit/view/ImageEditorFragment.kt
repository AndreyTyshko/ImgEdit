package com.example.imgedit.view

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import com.example.imgedit.viewmodel.MainActivityViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_image_editor_framgnet.*


class ImageEditorFragment : Fragment(R.layout.fragment_image_editor_framgnet) {


    private var currentImage: Uri? = null
    private val storagePermission: Array<String>? = null
    private var imageAdapter: ImageAdapter? = null
    lateinit var viewModel: MainActivityViewModel
    var imageUri: Uri? = null
    private var currentImage1: Bitmap? = null
    private var resultImage: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        currentImage1 = savedInstanceState?.get(KEY_IMG) as Bitmap?
        resultImage = savedInstanceState?.get(KEY_RESULT_IMAGE) as Bitmap?
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iv_input_img.setImageBitmap(currentImage1)
        imageViewResult.setImageBitmap(resultImage)
        viewModel = (activity as MainActivity).viewModel

        bindRV()

        imageAdapter?.setOnItemClickListener {
            val builder = AlertDialog.Builder(activity)
            builder.setTitle(getString(R.string.Select_action))
                .setNegativeButton(
                    getString(R.string.delete_action)
                ) { dialog, _ ->
                    viewModel.deleteOperation(it)
                    viewModel.getAllOperations().invoke().observe(viewLifecycleOwner) {
                        imageAdapter?.differ?.submitList(it)
                    }
                    dialog.cancel()
                }
                .setPositiveButton(
                    getString(R.string.reuse_operation)
                ) { dialog, _ ->
                    iv_input_img.setImageURI(it.image)
                    dialog.cancel()
                }
            builder.create().show()
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
                viewModel.changedImage.observe(viewLifecycleOwner) {
                    imageViewResult.setImageBitmap(it)
                }
            }
        }


        buttonInvColor.setOnClickListener {
            if (!hasImage(iv_input_img)) {
                showImagePickDialog()
            } else {

                val bitmap = (iv_input_img.drawable as BitmapDrawable).bitmap
                viewModel.invertColors(bitmap)
                viewModel.changedImage.observe(viewLifecycleOwner) {
                    imageViewResult.setImageBitmap(it)
                }
            }
        }

        buttonMirrImg.setOnClickListener {
            if (!hasImage(iv_input_img)) {
                showImagePickDialog()
            } else {

                val bitmap = (iv_input_img.drawable as BitmapDrawable).bitmap
                viewModel.imageFlipHorizontal(bitmap, -1.0f, 1.0f)
                viewModel.changedImage.observe(viewLifecycleOwner) {
                    imageViewResult.setImageBitmap(it)
                }
            }
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

    private fun scroll() {
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


    private fun showImagePickDialog() {
        val options = arrayOf(getString(R.string.camera), getString(R.string.gallery))
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(getString(R.string.select_image))
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
                getString(R.string.allow_camera),
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
                getString(R.string.allow_storage),
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


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, getString(R.string.camera_granted), Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(context, getString(R.string.camera_denied), Toast.LENGTH_SHORT)
                    .show()
            }
        } else if (requestCode == STORAGE_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, getString(R.string.storage_granted), Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(context, getString(R.string.storage_denied), Toast.LENGTH_SHORT)
                    .show()
            }
        }
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


    override fun onSaveInstanceState(outState: Bundle) {
        val drawable: BitmapDrawable = iv_input_img.drawable as BitmapDrawable
        val drawableResult: BitmapDrawable = imageViewResult.drawable as BitmapDrawable
        val some = drawable.bitmap
        val someResult = drawableResult.bitmap
        outState.putParcelable(KEY_IMG, some)
        outState.putParcelable(KEY_RESULT_IMAGE, someResult)

        super.onSaveInstanceState(outState)
    }


    companion object {

        private const val CAMERA_REQUEST_CODE = 100
        private const val STORAGE_REQUEST_CODE = 200
        private const val IMAGE_PICK_GALLERY_CODE = 300
        private const val IMAGE_PICK_CAMERA_CODE = 400
        private const val KEY_INDEX = "index"
        private const val KEY_IMG = "index"
        private const val KEY_RESULT_IMAGE = "resultImage"

    }
}
