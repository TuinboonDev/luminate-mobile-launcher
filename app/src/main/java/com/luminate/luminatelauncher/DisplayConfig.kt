package com.luminate.luminatelauncher

import android.Manifest
import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

fun getScreenDp(mContext : Context) : Pair<Int,Int>{
    val displayMetrics: DisplayMetrics = mContext.getResources().getDisplayMetrics()
    val dpHeight = displayMetrics.heightPixels / displayMetrics.density
    val dpWidth = displayMetrics.widthPixels / displayMetrics.density

    return Pair(dpWidth.toInt(), dpHeight.toInt())
}

fun getStatusBarHeight(mContext : Context): Float {
    var result : Float = 0.toFloat()
    val resourceId: Int = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = mContext.getResources().getDimensionPixelSize(resourceId).toFloat()
    }
    val displayMetrics: DisplayMetrics = mContext.getResources().getDisplayMetrics()
    result = (result / displayMetrics.density)
    return result
}