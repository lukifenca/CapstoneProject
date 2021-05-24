package com.lukitor.myapplicationC

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.lukitor.myapplicationC.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var fromsmall: Animation
    private lateinit var fromnothing: Animation
    private lateinit var foricon: Animation
    private lateinit var togo: Animation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.setNavigationIcon(R.drawable.iconback)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener{view -> finish()
        overridePendingTransition(R.transition.nothing,R.transition.bottom_down)}
        binding.collapsingToolbar.title = "Your Profile"
        binding.collapsingToolbar.setCollapsedTitleTextColor(
                ContextCompat.getColor(this, R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(Color.argb(0, 255, 0, 0))
        fromsmall = AnimationUtils.loadAnimation(this,R.anim.fromsmall)
        fromnothing = AnimationUtils.loadAnimation(this,R.anim.fromnothing)
        foricon = AnimationUtils.loadAnimation(this,R.anim.foricon)
        togo = AnimationUtils.loadAnimation(this,R.anim.togo)
        binding.mykonten.alpha = 0f
        binding.overbox.alpha = 0f
        binding.imagetemp.visibility = View.GONE
        binding.imgsettingkolestrol.setOnClickListener { view ->
            binding.imagetemp.visibility = View.VISIBLE
            binding.imagetemp.startAnimation(foricon)
            binding.overbox.alpha = 1f
            binding.overbox.startAnimation(fromnothing)
            binding.mykonten.alpha = 1f
            binding.mykonten.startAnimation(fromsmall)
        }
        binding.btnConfirm.setOnClickListener { view ->
            binding.overbox.startAnimation(togo)
            binding.imagetemp.startAnimation(togo)
            binding.mykonten.startAnimation(togo)
            binding.imagetemp.visibility = View.GONE
            ViewCompat.animate(binding.mykonten).setStartDelay(1000).alpha(0f).start()
            ViewCompat.animate(binding.overbox).setStartDelay(1000).alpha(0f).start()
        }
    }
}