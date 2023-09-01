package com.luminate.luminatelauncher

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat


const val DEFAULTAPPS : String =

    "com.instagram.android,0;0;0:"+"com.kick.mobile,0;0;0:"+
    "com.reddit.frontpage,0;0;0:"+"com.snapchat.android,0;0;0:"+
    "com.spotify.music,0;0;0:"+"tv.twitch.android.app,0;0;0:"+
    "com.luminate.wave,0;0;0:"+"com.whatsapp,0;0;0:"+
    "com.twitter.android,0;0;0:"+"com.android.chrome,0;0;0:"+
    "com.sec.android.app.clockpackage,0;0;0:"+"com.discord,0;0;0:"+
    "com.figma.mirror,0;0;0:"+"com.google.android.apps.nbu.files,0;0;0:"+
    "com.github.android,0;0;0:"+"com.google.android.gm,0;0;0:"+
    "com.sec.android.daemonapp,0;0;0:"+"com.google.android.youtube,0;0;0:"+
            "com.luminate.wave,0;0;0:"+"com.whatsapp,0;0;0:"+
            "com.twitter.android,0;0;0:"+"com.android.chrome,0;0;0:"+
            "com.sec.android.app.clockpackage,0;0;0:"+"com.discord,0;0;0:"+
            "com.figma.mirror,0;0;0:"+"com.google.android.apps.nbu.files,0;0;0:"+
            "com.github.android,0;0;0:"+"com.google.android.gm,0;0;0:"+
            "com.sec.android.daemonapp,0;0;0:"+"com.google.android.youtube,0;0;0:"

@SuppressLint("RestrictedApi")
fun getImage(mContext: Context, key: String): Drawable {
    return try {
        mContext.packageManager.getApplicationIcon(key)
    } catch (e: PackageManager.NameNotFoundException) {
        ContextCompat.getDrawable(mContext, R.drawable.unknown_app)!!
    }
}

fun getAppTitle(context: Context, packageName: String): String {
    val packageManager: PackageManager = context.packageManager

    return try {
        val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
        packageManager.getApplicationLabel(applicationInfo).toString()
    } catch (e: PackageManager.NameNotFoundException) {
        ""
    }
}

fun getAppLabelColor(darkbackground : Boolean) : Color {
    return if(darkbackground){
        Color.White
    } else {
        Color.Black
    }
}

fun removeItem(item:String,mContext : Context){
    try{
        val sharedPref = mContext.getSharedPreferences("luminatelauncherprefs", Context.MODE_PRIVATE)
        val applist : String = sharedPref.getString("applist", DEFAULTAPPS).toString()
        val list = mutableListOf<String>()
        for(application in applist.split(":")){ list.add(application) }
        Log.d("LIST", "List : $list")
        list.remove(item)
        var listasstring = ""
        list.forEach { if (it.isNotEmpty()) { listasstring += it + ":" }}
        Log.d("LIST", "List as string : $listasstring")
        with(sharedPref.edit()) {
            putString("applist", listasstring)
            apply()
        }
    }catch(e : Exception){
        Toast.makeText(
            mContext,
            "Fail!",
            Toast.LENGTH_SHORT
        ).show()
    }
}