package com.lukitor.myapplicationC

import android.content.Intent
import android.os.Bundle
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
        binding.imgProfile.setOnClickListener { view ->
            startActivity(Intent(this, ProfileActivity::class.java))}
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
                R.id.menu_Log -> {
                    //fragment = DataFragment.newInstance("TV Show")
//                    supportFragmentManager.beginTransaction().replace(R.id.container, fragment)
//                        .commit()
//                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })
        if (savedInstanceState == null) {
            binding.bottomNavigationView.setSelectedItemId(R.id.menu_Dashboard)
        }
    }
}