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


/**
 * Actividad encargada de mostrar el feed de publicaciones.
 * Utiliza un [RecyclerView] para listar las publicaciones disponibles.
 */
class FeedPActivity : MenuActivity() {

    private lateinit var binding: ActivityFeedPBinding
    
    /**
     * ViewModel que gestiona los datos de las publicaciones.
     */    
    private val viewModel: PostViewModel by viewModels()

    /**
     * Método llamado al crear la actividad.
     * Configura el layout, el RecyclerView y observa los datos del ViewModel.
     *
     * @param savedInstanceState : Estado anterior guardado (si existe).
     */    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()

        binding = ActivityFeedPBinding.inflate(layoutInflater)
        val container = findViewById<FrameLayout>(R.id.container) // No se puede usar binding aqui, ya que accede al elemento de otro layout
        container.addView(binding.root)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.navigation_feed

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.swipeRefresh.setOnRefreshListener {
            lifecycleScope.launch {
                viewModel.refreshPosts()
                viewModel.loadPosts(0)
            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val ctx = this

        // Observa los cambios en la lista de publicaciones y actualiza RecyclerView
        lifecycleScope.launch {
            viewModel.posts.collect { posts ->
                if (posts.isNotEmpty()) {
                    val adapter = PostAdapter()
                    adapter.updatePosts(posts)
                    binding.recyclerView.adapter = adapter

                    // Establecer el listener para cada elemento
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
                }
                binding.swipeRefresh.isRefreshing = false
            }
        }
        
        // Solicita la carga de publicaciones al ViewModel
        viewModel.loadPosts(0)
    }
}