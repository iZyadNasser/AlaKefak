package com.example.alakefak.ui.authflow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.alakefak.R
import com.example.alakefak.databinding.ActivityAuthBinding
import com.example.alakefak.databinding.ActivityRecipeBinding
import com.example.alakefak.ui.authflow.register.RegisterFragment

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val signOutCheck = intent.getBooleanExtra("key",false)

        if(signOutCheck){
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, RegisterFragment())
                .commit()
        }
    }
}