package com.example.alakefak.ui.appflow

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.alakefak.R
import com.example.alakefak.data.source.local.model.User
import com.example.alakefak.databinding.ActivityRecipeBinding
import com.example.alakefak.ui.appflow.favorites.FavoritesFragment
import com.example.alakefak.ui.appflow.home.HomeFragment
import com.example.alakefak.ui.appflow.profile.ProfileFragment
import com.example.alakefak.ui.appflow.search.SearchFragment
import com.example.alakefak.ui.authflow.Utils
import com.google.android.material.bottomnavigation.BottomNavigationView

class RecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)
        binding = ActivityRecipeBinding.inflate(layoutInflater)

        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, HomeFragment())
            .addToBackStack("home")
            .setReorderingAllowed(true)
            .commit()

        val src = intent.getStringExtra("source")

        if (src == "login") {
            val user = intent.getParcelableExtra<User>(Utils.INTENT_KEY)
            curUser = user
            val sharedPrefs = getSharedPreferences("user", Context.MODE_PRIVATE)
            val editor = sharedPrefs.edit()
            editor.putLong("userId", user?.id!!)
            editor.apply()
        } else {
            val sharedPrefs = getSharedPreferences("user", Context.MODE_PRIVATE)
            val userId = sharedPrefs.getLong("userId", 0)

            for (user in users!!) {
                if (user.id == userId) {
                    curUser = user
                    break
                }
            }
        }

        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        navView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, HomeFragment())
                        .addToBackStack("home")
                        .setReorderingAllowed(true)
                        .commit()
                    true
                }
                R.id.fav -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, FavoritesFragment())
                        .addToBackStack("fav")
                        .setReorderingAllowed(true)
                        .commit()
                    true
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, ProfileFragment())
                        .addToBackStack("profile")
                        .setReorderingAllowed(true)
                        .commit()
                    true
                }
                R.id.search -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, SearchFragment())
                        .addToBackStack("search")
                        .setReorderingAllowed(true)
                        .commit()
                    true
                }
                else -> false
            }
        }

        supportFragmentManager.addOnBackStackChangedListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            when (fragment) {
                is HomeFragment -> navView.menu.findItem(R.id.home).isChecked = true
                is FavoritesFragment -> navView.menu.findItem(R.id.fav).isChecked = true
                is ProfileFragment -> navView.menu.findItem(R.id.profile).isChecked = true
                is SearchFragment -> navView.menu.findItem(R.id.search).isChecked = true
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    companion object {
        var users: List<User>? = null
        var curUser: User? = null
        var lastPressedButton: Button? = null
        var curFragment: String = "home"
    }

}