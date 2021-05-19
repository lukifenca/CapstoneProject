package com.lukitor.myapplicationC

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.lukitor.myapplicationC.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.setNavigationIcon(R.drawable.iconback)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener{view -> finish()}
        binding.collapsingToolbar.title = "Your Profile"
        binding.collapsingToolbar.setCollapsedTitleTextColor(
                ContextCompat.getColor(this, R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(Color.argb(0, 255, 0, 0))
    }
}