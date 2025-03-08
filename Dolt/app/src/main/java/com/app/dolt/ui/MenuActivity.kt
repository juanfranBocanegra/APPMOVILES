package com.app.dolt.ui

import android.app.ActivityOptions
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.app.dolt.R
import com.app.dolt.databinding.ActivityMenuBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

// BaseActivity.kt
open class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu)

        // Configura la barra de navegación
        val bottomNavigation : BottomNavigationView = binding.bottomNavigation
        bottomNavigation.setOnItemSelectedListener  { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    if (this !is FeedCActivity) {
                        startActivity(Intent(this, FeedCActivity::class.java))
                        val options = ActivityOptions.makeCustomAnimation(this, 0, 0)
                    }
                    true
                }
                R.id.navigation_profile -> {
                    if (this !is ProfileActivity) {
                        startActivity(Intent(this, ProfileActivity::class.java))
                        val options = ActivityOptions.makeCustomAnimation(this, 0, 0)
                    }
                    true
                }
                R.id.navigation_settings -> {
                    if (this !is FeedCActivity) {
                        startActivity(Intent(this, FeedCActivity::class.java))
                        val options = ActivityOptions.makeCustomAnimation(this, 0, 0)
                    }
                    true
                }
                else -> false
            }
        }
    }
}