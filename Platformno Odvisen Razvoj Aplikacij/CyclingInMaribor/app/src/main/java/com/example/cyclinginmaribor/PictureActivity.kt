package com.example.cyclinginmaribor

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.cyclinginmaribor.databinding.ActivityPictureBinding

class PictureActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPictureBinding
    private lateinit var image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPictureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set picture taken by camera (its path) to ImageView
        image = findViewById(R.id.picture)
        val bitmap = BitmapFactory.decodeFile(intent.getStringExtra("image_path"))
        image.setImageBitmap(bitmap)

        val backButton = binding.backButton
        backButton.setOnClickListener {
            finish()
        }
    }
}