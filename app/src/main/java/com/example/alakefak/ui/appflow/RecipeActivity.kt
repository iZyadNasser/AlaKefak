package com.example.alakefak.ui.appflow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.alakefak.R
import com.example.alakefak.data.source.local.model.User
import com.example.alakefak.databinding.ActivityRecipeBinding
import com.example.alakefak.ui.appflow.favorites.FavoritesFragment
import com.example.alakefak.ui.appflow.home.HomeFragment
import com.example.alakefak.ui.appflow.profile.ProfileFragment
import com.example.alakefak.ui.appflow.search.SearchFragment
import com.example.alakefak.ui.authflow.FormUtils
import com.google.android.material.bottomnavigation.BottomNavigationView

class RecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)
        binding = ActivityRecipeBinding.inflate(layoutInflater)

        val src = intent.getStringExtra("source")

        if (src == "login") {
            val user = intent.getParcelableExtra<User>(FormUtils.INTENT_KEY)
            curUser = user
        }

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
                        .replace(R.id.nav_host_fragment, FavoritesFragment())
                        .commit()
                    true
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, ProfileFragment())
                        .commit()
                    true
                }
                R.id.search -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, SearchFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }

    companion object {
        var curUser: User? = null
    }

}