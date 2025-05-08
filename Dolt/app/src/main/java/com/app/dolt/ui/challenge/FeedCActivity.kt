package com.app.dolt.ui.challenge

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.dolt.databinding.ActivityFeedCBinding
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.app.dolt.R
import com.app.dolt.ui.MenuActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import timber.log.Timber


/**
 * Actividad encargada de mostrar el feed de desafíos.
 * Utiliza un [RecyclerView] para listar los desafíos disponibles.
 */
class FeedCActivity : MenuActivity() {
    private lateinit var binding: ActivityFeedCBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFeedCBinding.inflate(layoutInflater)
        val container = findViewById<FrameLayout>(R.id.container) // No se puede usar binding aqui, ya que accede al elemento de otro layout
        container.addView(binding.root)


        // Obtén el NavHostFragment y el NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Si tienes una barra de navegación o algún controlador, configura la navegación

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}