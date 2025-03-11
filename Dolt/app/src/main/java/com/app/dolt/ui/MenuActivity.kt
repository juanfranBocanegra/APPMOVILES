package com.app.dolt.ui

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.content.Intent
import androidx.appcompat.view.menu.MenuItemImpl
import androidx.databinding.DataBindingUtil
import com.app.dolt.R
import com.app.dolt.databinding.ActivityMenuBinding
import com.app.dolt.ui.challenge.FeedCActivity
import com.app.dolt.ui.post.FeedPActivity
import com.app.dolt.ui.search.SearchActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

// BaseActivity.kt
open class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu)

        // Configura la barra de navegación
        val bottomNavigation : BottomNavigationView = binding.bottomNavigation
        val menu = bottomNavigation.menu

        for(i in 0 until menu.size()){
            (menu.getItem(i) as? MenuItemImpl)?.let{
                it.isExclusiveCheckable = false
                it.isChecked = false
                it.isExclusiveCheckable = true
            }
        }

        bottomNavigation.setOnItemSelectedListener  { item ->
            when (item.itemId) {
                R.id.navigation_challenges -> {
                    if (this !is FeedCActivity) {
                        val intent = Intent(this, FeedCActivity::class.java)
                        val options = ActivityOptions.makeCustomAnimation(this, 0, 0)
                        startActivity(intent, options.toBundle())
                    }
                    true
                }
                R.id.navigation_feed -> {
                    if (this !is FeedPActivity) {
                        val intent = Intent(this, FeedPActivity::class.java)
                        val options = ActivityOptions.makeCustomAnimation(this, 0, 0)
                        startActivity(intent, options.toBundle())
                    }
                    true
                }
                R.id.navigation_profile -> {
                    if (this !is ProfileActivity || intent.getStringExtra("MYSELF") != "true") {
                        val sharedPreferences = getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
                        val username = sharedPreferences.getString("USERNAME", null)
                        val intent = Intent(this, ProfileActivity::class.java)
                        intent.putExtra("USERNAME", username)
                        intent.putExtra("MYSELF", "true")
                        val options = ActivityOptions.makeCustomAnimation(this, 0, 0)
                        startActivity(intent, options.toBundle())
                    }
                    true
                }
                R.id.navigation_search -> {
                    if (this !is SearchActivity) {
                        val intent = Intent(this, SearchActivity::class.java)
                        val options = ActivityOptions.makeCustomAnimation(this, 0, 0)
                        startActivity(intent, options.toBundle())
                    }
                    true
                }

                else -> false
            }
        }
    }
}