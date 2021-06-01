package com.example.imgedit


import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.rotationMatrix
import com.bumptech.glide.Glide



class MainActivity : AppCompatActivity() {

    private val IMAGE_PICK_GALLERY_CODE = 300
    lateinit var image: ImageView
    lateinit var imageViewResult: ImageView
    private var image_Uri: Uri? = null
    var button: Button?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        image = findViewById(R.id.imageView)
        imageViewResult=findViewById(R.id.imageViewResult)
        Glide.with(this)
            /*.load("https://media.geeksforgeeks.org/wp-content/uploads/20210101144014/gfglogo.png")
            .error(R.drawable.err_foreground)
            .into(image)*/

        image.setOnClickListener(View.OnClickListener {
            picFromGallery()
        })


        button=findViewById(R.id.buttonRotate)
        button?.setOnClickListener {
            Glide.with(applicationContext)
                .asBitmap()
                .load(image_Uri)
                .transform()
                .into(imageViewResult)

        }
    }

    private fun picFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                image_Uri = data!!.data
                image?.setImageURI(image_Uri)
                Log.d(TAG, image_Uri.toString())
            } else if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                image.setImageURI(image_Uri)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }



}




