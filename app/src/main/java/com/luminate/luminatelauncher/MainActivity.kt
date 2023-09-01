package com.luminate.luminatelauncher

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.luminate.luminatelauncher.Background.background
import com.luminate.luminatelauncher.MainGrid.MainGrid

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TIMER", "Start : " + System.currentTimeMillis())
        val edit = intent.getIntExtra("edit", 0)

        setContent {

            val dark = background(this,this)
            Log.d("TIMER", "Time when finished bg : " + System.currentTimeMillis())

            WindowCompat.setDecorFitsSystemWindows(window, false)
            val systemUiController = rememberSystemUiController()
            val view = LocalView.current
            SideEffect {
                systemUiController.setStatusBarColor(color = Color.Transparent)
                systemUiController.setNavigationBarColor(color = Color.Transparent)
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !dark
            }
            Log.d("TIMER", "Time when finished bars : " + System.currentTimeMillis())
            MainGrid(dark, this,edit)
            Log.d("TIMER", "End : " + System.currentTimeMillis())
        }

    }
}