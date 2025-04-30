package com.app.dolt.ui.profile

import ProfileEditFragment
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.app.dolt.R
import com.app.dolt.api.RetrofitClient
import com.app.dolt.databinding.ActivityProfileBinding
import com.app.dolt.model.FollowRequest
import com.app.dolt.model.LogoutRequest
import com.app.dolt.model.ProfileRequest
import com.app.dolt.model.UserProfile
import com.app.dolt.repository.ProfileRepository
import com.app.dolt.ui.MenuActivity
import com.app.dolt.ui.login.LoginActivity
import com.app.dolt.ui.profile.ProfileFollowFragment
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import timber.log.Timber


/**
 * Actividad encargada de mostrar y gestionar el perfil de un usuario.
 * Permite ver la información del perfil, seguir o dejar de seguir, editar perfil y cerrar sesión.
 */
class ProfileActivity : MenuActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val repository = ProfileRepository()

    private val userProfile: MutableLiveData<UserProfile> by lazy {
        MutableLiveData<UserProfile>()
    }

    /**
     * Observador del perfil que recarga la información cuando se actualiza.
     */
    val profileObserver = Observer<UserProfile> { newName ->
        if (this.userProfile.value != null) {
            loadProfile(this.userProfile.value?.username ?: "NULL")
        }
    }

    /**
     * Carga la información del perfil desde el repositorio.
     *
     * @param username : Nombre de usuario del perfil.
     */
    private fun loadProfile(username: String){
        val ctx = this
        lifecycleScope.launch {
            try {
                val userProfile: UserProfile? = repository.getProfile(username)
                if (userProfile != null) {
                    ctx.userProfile.value = userProfile
                    Timber.i(userProfile.name + " " + userProfile.username + " " + userProfile.profile_image)

                    // Carga la imagen de perfil
                    binding.profileImage.apply{
                        post {
                            val size = width
                            layoutParams.height = size
                            requestLayout()

                            Glide.with(context)
                                .load(userProfile.getProfileImageUrl())
                                .override(size, size)
                                .centerCrop()
                                .into(this)
                        }
                    }

                    // Muestra la información del perfil
                    binding.profileName.text = userProfile.name
                    binding.profileUsername.text = "@" + userProfile.username

                    val followingText = getString(R.string.following_label, userProfile.num_followed.toString())
                    val followersText = getString(R.string.followers_label, userProfile.num_followers.toString())

                    val profileFollowingText = SpannableString(followingText).apply {
                        setSpan(UnderlineSpan(), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }

                    val profileFollowersText = SpannableString(followersText).apply {
                        setSpan(UnderlineSpan(), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }

                    binding.profileFollowing.text = profileFollowingText
                    binding.profileFollowers.text = profileFollowersText

                    // Muestra los botones según el estado de relación
                    if (userProfile.following) {
                        binding.followButton.visibility = View.GONE
                        binding.unfollowButton.visibility = View.VISIBLE
                    }
                    if (userProfile.follower) {
                        binding.followerLabel.visibility = View.VISIBLE
                    }

                    ctx.userProfile.removeObservers(ctx)

                } else {
                    Toast.makeText(ctx, "Error de sesión, inicie de nuevo", Toast.LENGTH_SHORT).show()
                    val intent = Intent(ctx, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Método llamado al crear la actividad.
     * Configura la interfaz y los eventos asociados al perfil.
     *
     * @param savedInstanceState : Estado anterior guardado (si existe).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()

        Timber.e("---------------PROFILE_ACT CREATED")

        binding = ActivityProfileBinding.inflate(layoutInflater)
        val container = findViewById<FrameLayout>(R.id.container) 
        container.addView(binding.root)

        val myself = intent.getStringExtra("MYSELF") == "true"

        // Configura la navegación y botones si el perfil es propio 
        if (myself) {
            val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
            bottomNavigation.selectedItemId = R.id.navigation_profile
            binding.logoutButton.visibility = View.VISIBLE
            binding.editButton.visibility = View.VISIBLE
            binding.followButton.visibility = View.GONE
        }

        val username = intent.getStringExtra("USERNAME")
        val ctx = this
        if (username != null) {
            loadProfile(username)

            val profileFollowingText = SpannableString(binding.profileFollowing.text.toString()).apply {
                setSpan(UnderlineSpan(), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            val profileFollowersText = SpannableString(binding.profileFollowers.text.toString()).apply {
                setSpan(UnderlineSpan(), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            binding.profileFollowing.text = profileFollowingText
            binding.profileFollowers.text = profileFollowersText
            // Seguir usuario
            binding.followButton.setOnClickListener {
                lifecycleScope.launch {
                    RetrofitClient.apiService.follow(FollowRequest(username))
                    loadProfile(username)
                    binding.followButton.visibility = View.GONE
                    binding.unfollowButton.visibility = View.VISIBLE
                }
            }

            // Dejar de seguir usuario
            binding.unfollowButton.setOnClickListener {
                lifecycleScope.launch {
                    RetrofitClient.apiService.unfollow(FollowRequest(username))
                    loadProfile(username)
                    binding.unfollowButton.visibility = View.GONE
                    binding.followButton.visibility = View.VISIBLE
                }
            }

            // Obtener lista de seguidos
            binding.profileFollowing.setOnClickListener {
                lifecycleScope.launch {
                    val fragment = ProfileFollowFragment(username, 1)

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .commit()
                }
            }

            // Obtener lista de seguidores
            binding.profileFollowers.setOnClickListener {
                lifecycleScope.launch {
                    val fragment = ProfileFollowFragment(username, 2)

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .commit()
                }
            }




            // Cerrar sesión
            binding.logoutButton.setOnClickListener {
                val sharedPreferences = getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
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
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }

            // Editar perfil
            binding.editButton.setOnClickListener {
                this.userProfile.observe(this, this.profileObserver)
                this.userProfile.let { userProfile ->
                    val fragment = ProfileEditFragment(userProfile, this)

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .commit()
                }
                Timber.i("AAAAAAAAAAA%s", this.userProfile.value?.name.toString())
            }

        }
    }
}