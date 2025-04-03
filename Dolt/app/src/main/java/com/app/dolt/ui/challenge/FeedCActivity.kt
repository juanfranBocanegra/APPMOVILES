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


/**
 * Actividad encargada de mostrar el feed de desafíos.
 * Utiliza un [RecyclerView] para listar los desafíos disponibles.
 */
class FeedCActivity : MenuActivity() {
    private lateinit var binding: ActivityFeedCBinding

    /**
     * ViewModel que gestiona los datos de los desafíos.
     */
    private val viewModel: ChallengeViewModel by viewModels()

    /**
     * Método llamado al crear la actividad.
     * Configura el layout, el RecyclerView y observa los datos del ViewModel.
     *
     * @param savedInstanceState : Estado anterior guardado (si existe).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()

        binding = ActivityFeedCBinding.inflate(layoutInflater)
        val container = findViewById<FrameLayout>(R.id.container) //no se puede usar binding aqui, ya que accede al elemento de otro layout
        container.addView(binding.root)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.navigation_challenges

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // Observa los cambios en la lista de desafíos y actualiza el RecyclerView
        lifecycleScope.launch {
            viewModel.challenges.collect { challenges ->
                if (challenges.isNotEmpty()) {
                    val adapter = ChallengeAdapter()
                    adapter.updateChallenges(challenges)  
                    binding.recyclerView.adapter = adapter

                    // Establecer el listener para cada elemento
                    adapter.setOnItemClickListener { position ->
                        val challenge = challenges[position]  
                        showChallengeDetail(challenge.name, challenge.detail, challenge.id, challenge.available.toString())
                    }
                }
            }
        }

        // Solicita la carga de desafíos al ViewModel
        viewModel.loadChallenges()
    }

    /**
     * Muestra el fragmento con los detalles del desafío seleccionado.
     *
     * @param name : Nombre del desafío.
     * @param detail : Detalle del desafío.
     * @param id : Identificador del desafío.
     * @param available : Disponibilidad del desafío.
     */
    private fun showChallengeDetail(name: String, detail: String, id: String, available: String) {
        val fragment = ChallengeDetailFragment.newInstance(name, detail, id, available)

        supportFragmentManager.let {
            fragment.show(it, "ChallengeDetailFragment")
        }

        Log.d("ChallengeDetail", "Showing dialog for: $name")
    }

}