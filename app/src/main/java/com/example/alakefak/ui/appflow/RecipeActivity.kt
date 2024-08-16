package com.example.alakefak.ui.appflow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.alakefak.R
import com.example.alakefak.databinding.ActivityRecipeBinding
import com.example.alakefak.ui.appflow.favourites.FavouritesFragment
import com.example.alakefak.ui.appflow.home.HomeFragment
import com.example.alakefak.ui.appflow.settings.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class RecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)
        binding = ActivityRecipeBinding.inflate(layoutInflater)

        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        navView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, HomeFragment())
                        .commit()
                    true
                }
                R.id.fav -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, FavouritesFragment())
                        .commit()
                    true
                }
                R.id.settings -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, SettingsFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }

}