package com.app.dolt

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Visibility
import com.app.dolt.databinding.ActivityMainBinding
import com.app.dolt.model.Challenge
import androidx.activity.addCallback


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        val name = resources.getString(R.string.app_name) + " AAAA"
        Toast.makeText(this, name, Toast.LENGTH_LONG).show()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        val items = listOf(
            Challenge(1,"Desafío 1", "Descripción del desafío 1"),
            Challenge(2,"Desafío 2","Descripción del desafío 2"),
            Challenge(3,"Desafío 3","Descripción del desafío 3"),
            Challenge(4,"Desafío 4","Descripción del desafío 4")
        )

        val adapter = ChallengeAdapter()
        adapter.updateChallenges(items)
        binding.recyclerView.adapter = adapter


        adapter.setOnItemClickListener { position ->
            val challenge = items[position]
            binding.nameTextOverlay.text = challenge.name
            binding.detailTextOverlay.text = challenge.detail
            binding.overlay.visibility = View.VISIBLE

            onBackPressedDispatcher.addCallback{
                if (binding.overlay.visibility == View.VISIBLE){
                    binding.overlay.visibility = View.GONE
                }

                    isEnabled = false

            }

            binding.closeOverlay.setOnClickListener{
                binding.overlay.visibility = View.GONE
            }
        }


    }


}