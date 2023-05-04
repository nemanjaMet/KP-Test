package com.example.kupujemprodajemzadatak.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.kupujemprodajemzadatak.R
import com.example.kupujemprodajemzadatak.shared_view_models.SharedViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSystemSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    // Splash screen
    private fun installSystemSplashScreen() {
        val splashScreen = installSplashScreen()

        splashScreen.apply {
            setKeepOnScreenCondition {
                viewModel.isSplashVisible.value!!
            }
        }
    }


}