package com.example.multimediaapplication.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.multimediaapplication.R
import com.example.multimediaapplication.fragments.ImageFragment

class ImageActivity: AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_activity)


        val imageUri = intent.getStringExtra("imageUri")
        val mode = intent.getStringExtra("mode")

        if (imageUri != null) {
            val fragment = supportFragmentManager.findFragmentById(R.id.imgFragment) as? ImageFragment
            if(mode=="view") {
                fragment?.viewImage(imageUri)
            }
            else if (mode=="edit")
            {
                fragment?.editImage(imageUri)
            }
        }

    }

}