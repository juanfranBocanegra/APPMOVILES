package com.app.dolt.ui.search

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.dolt.R
import com.app.dolt.api.RetrofitClient
import com.app.dolt.databinding.ActivitySearchBinding
import com.app.dolt.ui.MainActivity
import com.app.dolt.ui.MenuActivity
import com.app.dolt.ui.ProfileActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.internal.ContextUtils.getActivity
import kotlinx.coroutines.launch

class SearchActivity : MenuActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val handler = Handler(Looper.getMainLooper())
    private var workRunnable: Runnable? = null
    private val delayMillis = 500L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySearchBinding.inflate((layoutInflater))

        val container =
            findViewById<FrameLayout>(R.id.container) //no se puede usar binding aqui, ya que accede al elemento de otro layout
        container.addView(binding.root)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.selectedItemId = R.id.navigation_search

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Antes de que cambie el texto
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Se ejecuta cada vez que cambia el texto
                workRunnable?.let { handler.removeCallbacks(it) }

                // Programa un nuevo Runnable después del retraso
                workRunnable = Runnable {


                    if (s.toString().isNotEmpty()) {

                        lifecycleScope.launch {
                            try {

                                val searchAdapter = SearchAdapter()
                                binding.recyclerView.adapter = searchAdapter
                                binding.recyclerView.layoutManager = LinearLayoutManager(baseContext)

                                val response = RetrofitClient.apiService.search(s.toString())
                                Log.i("AA:", "AAAAAAAAA: $response")
                                searchAdapter.updateSearch(response)


                                searchAdapter.setOnItemClickListener { position ->
                                    val user = response[position]
                                    val sharedPreferences =
                                        getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
                                    val myUsername = sharedPreferences.getString("USERNAME", null)


                                    val intent = Intent(baseContext, ProfileActivity::class.java)
                                    intent.putExtra("USERNAME", user.username)
                                    if (myUsername == user.username) {
                                        intent.putExtra("MYSELF", "true")
                                    } else {
                                        intent.putExtra("MYSELF", "false")
                                    }


                                    val options = ActivityOptions.makeCustomAnimation(baseContext, 0, 0)
                                    startActivity(intent, options.toBundle())
                                }


                            } catch (e: Exception) {
                                Log.e("SEARCH: ", e.toString())
                                Toast.makeText(baseContext, "ERROR", Toast.LENGTH_SHORT).show()
                            }

                        }

                    }

                }
                handler.postDelayed(workRunnable!!, delayMillis)
            }

            override fun afterTextChanged(s: Editable?) {
                // Después de que el texto cambie
            }
        })

    }
}