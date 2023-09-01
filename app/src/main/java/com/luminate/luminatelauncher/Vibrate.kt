package com.luminate.luminatelauncher

import android.content.Context
import android.os.Vibrator

fun Vibrate(duration : Int,mContext : Context){
    val vibrator = mContext.getSystemService(android.content.Context.VIBRATOR_SERVICE) as Vibrator
    vibrator.vibrate(duration.toLong())
}