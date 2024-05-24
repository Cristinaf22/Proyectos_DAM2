package com.example.multimediaapplication.activities

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.example.multimediaapplication.R


class AudioActivity: AppCompatActivity ()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_activity)

        val audioView = findViewById<VideoView>(R.id.audioView)
        val buttonReturn = findViewById<Button>(R.id.buttonReturn)

        val path = intent.getStringExtra("path")

        val uri = Uri.parse(path)
        audioView.setVideoURI(uri)

        val mediaController = MediaController(this)
        audioView.setMediaController(mediaController)

        buttonReturn.setOnClickListener(){

            finish()
        }
    }
}