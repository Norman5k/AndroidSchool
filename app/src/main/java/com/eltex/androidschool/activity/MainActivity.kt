package com.eltex.androidschool.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eltex.androidschool.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}