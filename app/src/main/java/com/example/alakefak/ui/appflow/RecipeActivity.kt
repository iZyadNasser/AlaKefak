package com.example.alakefak.ui.appflow

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.alakefak.R
import com.example.alakefak.data.source.local.database.UserDatabase
import com.example.alakefak.data.source.local.model.User
import com.example.alakefak.databinding.ActivityRecipeBinding
import com.example.alakefak.ui.appflow.favorites.FavoritesFragment
import com.example.alakefak.ui.appflow.home.HomeFragment
import com.example.alakefak.ui.appflow.profile.ProfileFragment
import com.example.alakefak.ui.appflow.search.SearchFragment
import com.example.alakefak.ui.authflow.FormUtils
import com.example.alakefak.ui.authflow.login.LoginFragment
import com.example.alakefak.ui.authflow.login.LoginFragment.Companion.KEY_IS_LOGGED_IN
import com.example.alakefak.ui.authflow.login.LoginFragment.Companion.PREFS_NAME
import com.example.alakefak.ui.authflow.splash.SplashFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson

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

    @SuppressLint("StaticFieldLeak")
    companion object {
        var users: List<User>? = null
        var curUser: User? = null
        var lastPressedButton: Button? = null
    }

}