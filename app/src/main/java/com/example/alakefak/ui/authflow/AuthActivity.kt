package com.example.alakefak.ui.authflow

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.TextUtilsCompat
import androidx.navigation.navOptions
import com.example.alakefak.R
import com.example.alakefak.databinding.ActivityAuthBinding
import java.util.Locale

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkSignOut()
    }

    private fun checkSignOut() {
        val signOutCheck = intent.getBooleanExtra(SIGN_OUT_CHECK_INTENT_KEY, false)
        signedOut = signOutCheck
    }

    companion object {
        const val SIGN_OUT_CHECK_INTENT_KEY = "signOutCheck"
        var signedOut = false

        val navOptions = navOptions {
            anim {
                enter = if (isRtl()) R.anim.pop_enter_anim else R.anim.enter_anim
                exit = if (isRtl()) R.anim.pop_exit_anim else R.anim.exit_anim
                popEnter = if (isRtl()) R.anim.enter_anim else R.anim.pop_enter_anim
                popExit = if (isRtl()) R.anim.exit_anim else R.anim.pop_exit_anim
            }
        }

        private fun isRtl(): Boolean {
            return TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == View.LAYOUT_DIRECTION_RTL
        }
    }
}