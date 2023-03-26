package com.example.weather.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.weather.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class StartActivity : AppCompatActivity() {
    lateinit var btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        btn = findViewById(R.id.button)

        btn.setOnClickListener {
                val intent = Intent(this@StartActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }

    }



