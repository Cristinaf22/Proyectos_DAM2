package com.example.multimediaapplication.activities

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.example.multimediaapplication.R

class VideoActivity: AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_activity)

        val videoView = findViewById<VideoView>(R.id.videoView)
        val buttonReturn = findViewById<Button>(R.id.buttonReturn)

        val path = intent.getStringExtra("path")

        val uri = Uri.parse(path)
        videoView.setVideoURI(uri)

        val mediaController = MediaController(this)
        videoView.setMediaController(mediaController)

        buttonReturn.setOnClickListener(){

            finish()
        }

    }
}