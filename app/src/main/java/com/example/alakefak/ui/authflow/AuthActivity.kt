package com.example.alakefak.ui.authflow

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.alakefak.R

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_auth)
        val rootView = findViewById<View>(R.id.auth_activity)
        rootView.setOnApplyWindowInsetsListener { v, insets ->
            val systemInsets = insets.systemGestureInsets
            v.setPadding(
                v.paddingLeft + systemInsets.left,
                v.paddingTop + systemInsets.top,
                v.paddingRight + systemInsets.right,
                v.paddingBottom + systemInsets.bottom
            )
            insets
        }
    }
}