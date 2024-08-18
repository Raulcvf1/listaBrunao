package com.example.listabrunao

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class ImageViewerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_viewer)

        val imageView = findViewById<ImageView>(R.id.imageView)
        val imageUrl = intent.getStringExtra("imageUrl")

        Glide.with(this).load(imageUrl).into(imageView)
    }
}
