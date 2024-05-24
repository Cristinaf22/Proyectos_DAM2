package com.example.espaciocultural

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity


class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val that = this

        val timer: CountDownTimer = object : CountDownTimer((10 * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                val intent = Intent(that, ListActivity::class.java)
                startActivity(intent)
            }
        }
        timer.start()

        val pantallaInicio = findViewById<LinearLayout>(R.id.pantallaInicio)

        pantallaInicio.setOnClickListener(){
            timer.cancel()
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }
    }
}