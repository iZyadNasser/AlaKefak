package com.example.alakefak.ui.authflow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.alakefak.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val signOutCheck = intent.getBooleanExtra("key", false)
        signedOut = signOutCheck
    }

    companion object {
        var signedOut = false
    }
}