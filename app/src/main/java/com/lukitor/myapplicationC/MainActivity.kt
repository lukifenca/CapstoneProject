package com.lukitor.myapplicationC

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.lukitor.myapplicationC.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Glide.with(this).load(R.drawable.capstonelogo).into(binding.imageView2)
        Handler(mainLooper).postDelayed({
            startActivity(Intent(this, GetStartedHomeActivity::class.java))
            finish()}, 8000)
    }
}