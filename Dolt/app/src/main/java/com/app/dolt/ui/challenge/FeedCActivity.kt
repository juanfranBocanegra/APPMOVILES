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
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.app.dolt.R
import com.app.dolt.ui.MenuActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import androidx.core.view.isVisible


class FeedCActivity : MenuActivity() {
    private lateinit var binding: ActivityFeedCBinding
    private val viewModel: ChallengeViewModel by viewModels()



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

        lifecycleScope.launch {
            viewModel.challenges.collect { challenges ->
                if (challenges.isNotEmpty()) {
                    val adapter = ChallengeAdapter()
                    adapter.updateChallenges(challenges)  // Actualiza los datos en el adaptador
                    binding.recyclerView.adapter = adapter

                    // Establecer el listener para cada elemento después de que se haya actualizado la lista
                    adapter.setOnItemClickListener { position ->
                        val challenge = challenges[position]  // Usar el valor de challenges (de la API)
                        showChallengeDetail(challenge.name, challenge.detail, challenge.id, challenge.available.toString())

                    }
                }
            }
        }

        viewModel.loadChallenges()


    }

    private fun showChallengeDetail(name: String, detail: String, id: String, available: String) {
        val fragment = ChallengeDetailFragment.newInstance(name, detail, id, available)

        // Asegúrate de usar el FragmentManager correcto
        supportFragmentManager.let {
            fragment.show(it, "ChallengeDetailFragment")
        }

        // Opcional: añade un log para verificar que se llama al método
        Log.d("ChallengeDetail", "Showing dialog for: $name")
    }

}