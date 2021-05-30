package com.example.imgedit


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Gallery
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    val GALLERY_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val image:ImageView = findViewById(R.id.imageView)
        /*Picasso.with(this)
                .load("https://media.geeksforgeeks.org/wp-content/uploads/20210101144014/gfglogo.png")
                .error(R.drawable.err_foreground)
                .into(imageView)*/

       image.setOnClickListener(View.OnClickListener {
           val photoPickerIntent = Intent(Intent.ACTION_PICK)
           photoPickerIntent.type = "image/*"
           startActivityForResult(photoPickerIntent, GALLERY_REQUEST)

       })




    }


}


