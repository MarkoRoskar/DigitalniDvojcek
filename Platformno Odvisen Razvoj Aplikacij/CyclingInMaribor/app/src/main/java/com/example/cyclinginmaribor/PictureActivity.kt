package com.example.cyclinginmaribor

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class PictureActivity : AppCompatActivity() {

    private lateinit var image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)

        image = findViewById(R.id.picture)
        val bitmap = BitmapFactory.decodeFile(intent.getStringExtra("image_path"))
        image.setImageBitmap(bitmap)
    }
}