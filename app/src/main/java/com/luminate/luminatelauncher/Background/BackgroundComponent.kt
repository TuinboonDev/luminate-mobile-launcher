package com.luminate.luminatelauncher.Background

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.luminate.luminatelauncher.R
import android.provider.Settings


@Composable
fun background(context: Context, activity : Activity) : Boolean{
    //USE THE PHONES HOME BACKGROUND INSTEAD
    val wallpaperDrawable : Bitmap = getWall(context, activity)

    Image(
        bitmap = wallpaperDrawable.asImageBitmap(),
        modifier = Modifier.fillMaxSize(),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )

    return isDark(wallpaperDrawable)
}

@Composable
@SuppressLint("MissingPermission", "NewApi")
fun getWall(context : Context, activity : Activity) : Bitmap {
    val wallpaperManager : WallpaperManager = WallpaperManager.getInstance(context);
    return try {
        //Only works when under android 13
        wallpaperManager.drawable!!.toBitmap()
    } catch(e : Exception) {
        //Runs this if phone is android 13+
        Log.e("Error" , "$e")
        ContextCompat.getDrawable(context, R.drawable.purple_light)!!.toBitmap()
    }
}

fun isDark(bitmap: Bitmap): Boolean {
    var dark = false
    val darkThreshold = bitmap.width * bitmap.height * 0.45f
    var darkPixels = 0
    val pixels = IntArray(bitmap.width * bitmap.height)
    bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
    for (pixel in pixels) {
        val color = pixel
        val r: Int = Color.red(color)
        val g: Int = Color.green(color)
        val b: Int = Color.blue(color)
        val luminance = 0.299 * r + 0.587 * g + 0.114 * b
        if (luminance < 150) {
            darkPixels++
        }
    }
    if (darkPixels >= darkThreshold) {
        dark = true
    }
    Log.d("Maths", "Dark : $dark")
    return dark
}