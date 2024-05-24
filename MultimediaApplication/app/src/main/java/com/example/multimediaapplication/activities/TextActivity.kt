package com.example.multimediaapplication.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.multimediaapplication.R
import com.example.multimediaapplication.fragments.TextFragment

class TextActivity: AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.text_activity)


        val fileContent = intent.getStringExtra("fileContent")
        val mode = intent.getStringExtra("mode")
        val filePath = intent.getStringExtra("filePath")

        if(fileContent != null)
        {
            val fragment = supportFragmentManager.findFragmentById(R.id.textFragment) as? TextFragment
            if(mode=="view")
            {
                fragment?.updateText(fileContent)
                fragment?.disableText()
            }
            else if(mode=="edit")
            {
                fragment?.updateEditText(fileContent)
                fragment?.setFilePath(filePath)
            }

        }
    }
}

