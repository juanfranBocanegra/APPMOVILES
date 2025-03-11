package com.app.dolt.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.app.dolt.R
import com.app.dolt.api.RetrofitClient
import com.app.dolt.databinding.ActivityProfileBinding
import com.app.dolt.model.FollowRequest
import com.app.dolt.model.LogoutRequest
import com.app.dolt.model.UserProfile
import com.app.dolt.repository.ProfileRepository
import com.app.dolt.ui.login.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class ProfileActivity : MenuActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val repository = ProfileRepository()


    private fun loadProfile(username: String){
        val ctx = this
        lifecycleScope.launch {
            try {
                val userProfile: UserProfile? = repository.getProfile(username)
                if (userProfile != null) {
                    Log.i("PROFILE:", userProfile.name + " " + userProfile.username)
                    binding.profileName.text = userProfile.name
                    binding.profileUsername.text = "@" + userProfile.username
                    binding.profileFollowing.text =
                        getString(R.string.following_label, userProfile.num_followed.toString())
                    binding.profileFollowers.text =
                        getString(R.string.followers_label, userProfile.num_followers.toString())
                    if (userProfile.following) {
                        binding.followButton.visibility = View.GONE
                        binding.unfollowButton.visibility = View.VISIBLE
                    }
                    if (userProfile.follower) {
                        binding.followerLabel.visibility = View.VISIBLE
                    }



                } else {
                    Toast.makeText(ctx, "Error de sesión, inicie de nuevo", Toast.LENGTH_SHORT)
                        .show()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()



        binding = ActivityProfileBinding.inflate(layoutInflater)
        val container =
            findViewById<FrameLayout>(R.id.container) //no se puede usar binding aqui, ya que accede al elemento de otro layout
        container.addView(binding.root)


        val myself = intent.getStringExtra("MYSELF") == "true"

        if (myself) {

            val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
            bottomNavigation.selectedItemId = R.id.navigation_profile
            binding.logoutButton.visibility = View.VISIBLE
            binding.followButton.visibility = View.GONE
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val username = intent.getStringExtra("USERNAME")
        val ctx = this
        if (username != null) {

            loadProfile(username)

            binding.followButton.setOnClickListener {
                lifecycleScope.launch {
                    RetrofitClient.apiService.follow(FollowRequest(username))
                    loadProfile(username)
                    binding.followButton.visibility = View.GONE
                    binding.unfollowButton.visibility = View.VISIBLE

                }

            }

            binding.unfollowButton.setOnClickListener {
                lifecycleScope.launch {
                    RetrofitClient.apiService.unfollow(FollowRequest(username))
                    loadProfile(username)
                    binding.unfollowButton.visibility = View.GONE
                    binding.followButton.visibility = View.VISIBLE


                }
            }
        }

        binding.logoutButton.setOnClickListener {
            val sharedPreferences = getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            val refresh = sharedPreferences.getString("REFRESH_TOKEN", null)
            if (refresh != null) {
                lifecycleScope.launch {
                    RetrofitClient.apiService.logout(LogoutRequest(refresh))
                    editor.apply {
                        remove("ACCESS_TOKEN")
                        remove("USERNAME")
                        editor.remove("REFRESH_TOKEN")
                        apply()
                    }
                }
            }


            val intent = Intent(ctx, LoginActivity::class.java)
            startActivity(intent)
            finish()


        }


    }
}
