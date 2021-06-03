package com.lukitor.myapplicationC

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.lukitor.myapplicationC.databinding.ActivityUserBinding


class UserActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imgProfile.setOnClickListener { view ->
            startActivity(Intent(this, ProfileActivity::class.java))
            overridePendingTransition(R.transition.bottom_up, R.transition.nothing);}

        val fragment: Fragment
        fragment = DashboardFragment.newInstance()
        supportFragmentManager.beginTransaction().replace(R.id.layoutAplaahhhh, fragment).commit()
    }
}