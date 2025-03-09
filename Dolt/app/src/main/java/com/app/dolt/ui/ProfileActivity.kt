package com.app.dolt.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.app.dolt.R
import com.app.dolt.api.RetrofitClient
import com.app.dolt.databinding.ActivityProfileBinding
import com.app.dolt.model.LogoutRequest
import com.app.dolt.model.Profile
import com.app.dolt.repository.ProfileRepository
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class ProfileActivity : MenuActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val repository = ProfileRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityProfileBinding.inflate(layoutInflater)
        val container = findViewById<FrameLayout>(R.id.container) //no se puede usar binding aqui, ya que accede al elemento de otro layout
        container.addView(binding.root)

        val menu = intent.getStringExtra("MENU")

        if (menu == "true") {
            val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
            bottomNavigation.selectedItemId = R.id.navigation_profile
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val username = intent.getStringExtra("USERNAME")
        val ctx = this
        if (username != null) {

            lifecycleScope.launch {
                try {
                    val profile: Profile? = repository.getProfile(username)
                    if (profile != null) {
                        Log.i("PROFILE:", profile.name + " " + profile.username)
                        binding.profileName.text = profile.name
                        binding.profileUsername.text = profile.username
                    } else {
                        Toast.makeText(ctx, "Error de sesión, inicie de nuevo", Toast.LENGTH_SHORT).show()
                        val intent = Intent(ctx, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } catch (e: Exception) {
                    // Manejar errores (red, API, etc.)
                    e.printStackTrace()
                }
            }
        }

        binding.logoutButton.setOnClickListener{
            val sharedPreferences = getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.apply {
                remove("ACCESS_TOKEN")
                remove("USERNAME")
                apply()
            }

            val refresh = sharedPreferences.getString("REFRESH_TOKEN", null)
            if (refresh != null) {
                lifecycleScope.launch {
                    RetrofitClient.apiService.logout(LogoutRequest(refresh))
                }
            }

            editor.remove("REFRESH_TOKEN")
            editor.apply()

            val intent = Intent(ctx, LoginActivity::class.java)
            startActivity(intent)
            finish()


        }
    }
}