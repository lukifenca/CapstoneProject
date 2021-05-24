package com.lukitor.myapplicationC

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lukitor.myapplicationC.databinding.ActivityUserBinding


class UserActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigationView.visibility = View.GONE
        binding.bottomNavigationView.animate().scaleY(0f)
        binding.imagetemp.setOnClickListener { view ->
            binding.imagetemp.animate().scaleY(0f)
            binding.imagetemp.visibility = View.GONE
            binding.bottomNavigationView.visibility = View.VISIBLE
            binding.bottomNavigationView.animate().scaleY(1.0f)
        }
        binding.imgProfile.setOnClickListener { view ->
            startActivity(Intent(this, ProfileActivity::class.java))
            overridePendingTransition(R.transition.bottom_up, R.transition.nothing);}
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            val fragment: Fragment
            when (item.itemId) {
                R.id.menu_Dashboard -> {
                    fragment = DashboardFragment.newInstance()
                    supportFragmentManager.beginTransaction().replace(R.id.layoutAplaahhhh, fragment)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.menu_Scan -> {
//                    fragment = DataFragment.newInstance("Movie")
//                    supportFragmentManager.beginTransaction().replace(R.id.container, fragment)
//                        .commit()
//                    return@OnNavigationItemSelectedListener true
                }
                R.id.menu_Close -> {
                    binding.bottomNavigationView.animate().scaleY(0f)
                    binding.imagetemp.visibility = View.VISIBLE
                    binding.imagetemp.animate().scaleY(1.0f)
                    binding.bottomNavigationView.visibility = View.GONE
                }
            }
            false
        })
        if (savedInstanceState == null) {
            binding.bottomNavigationView.setSelectedItemId(R.id.menu_Dashboard)
        }
    }
}