package com.app.dolt.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.dolt.R
import android.content.SharedPreferences
import com.app.dolt.utils.SharedPreferencesManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val token = getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE).getString("BEARER_TOKEN", null)
        val username = getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE).getString("USERNAME", null)

        if (token == null || username == null) {

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }else{
            val intent = Intent(this, FeedCActivity::class.java)
            startActivity(intent)
        }
        finish()
    }
}