package com.app.dolt.ui.post

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.app.dolt.R
import com.app.dolt.databinding.ActivityFeedPBinding
import com.app.dolt.ui.MenuActivity
import com.app.dolt.ui.profile.ProfileActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch


class FeedPActivity : MenuActivity() {
    private lateinit var binding: ActivityFeedPBinding
    private val viewModel: PostViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()

        binding = ActivityFeedPBinding.inflate(layoutInflater)
        val container = findViewById<FrameLayout>(R.id.container) //no se puede usar binding aqui, ya que accede al elemento de otro layout
        container.addView(binding.root)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.navigation_feed

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }


        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val ctx = this
        lifecycleScope.launch {
            viewModel.posts.collect { posts ->
                if (posts.isNotEmpty()) {
                    val adapter = PostAdapter()
                    adapter.updatePosts(posts)  // Actualiza los datos en el adaptador
                    binding.recyclerView.adapter = adapter

                    adapter.setOnItemClickListener { position ->
                        val post = posts[position]
                        val sharedPreferences = getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
                        val username = sharedPreferences.getString("USERNAME", null)

                        intent = Intent(ctx, ProfileActivity::class.java)
                        intent.putExtra("USERNAME", post.user)
                        if(username == post.user) {
                            intent.putExtra("MYSELF", "true")
                        }else{
                            intent.putExtra("MYSELF", "false")
                        }


                        val options = ActivityOptions.makeCustomAnimation(ctx, 0, 0)
                        startActivity(intent, options.toBundle())


                    }

                    /*val */

                }
            }
        }

        viewModel.loadPosts(0)


    }


}