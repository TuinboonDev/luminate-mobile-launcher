package com.luminate.luminatelauncher.MainGrid

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luminate.luminatelauncher.getScreenDp
import com.luminate.luminatelauncher.ui.poppinsFont

@Composable
fun AppDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    remove: () -> Unit,
    uninstall: () -> Unit,
    appname : String
) {

    val mContext = LocalContext.current
    val screenWidth = getScreenDp(mContext).first
    val screenHeight = getScreenDp(mContext).second
    val interactionSource = remember { MutableInteractionSource() }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            containerColor = Color(0xFFFFFFFF),
            titleContentColor = Color.Black,
            shape = RoundedCornerShape(30.dp),
            title = {
                Text(
                    text = appname,
                    textAlign = TextAlign.Center,
                    fontFamily = poppinsFont,
                    fontSize = 25.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth().height(screenHeight.dp/5/2)
                )
            },
            text = {
                Text(
                    text = "Remove $appname ?",
                    textAlign = TextAlign.Center,
                    fontFamily = poppinsFont,
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            },
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth().height(screenHeight.dp/5/2)
                ){
                    val actions : List<String> = listOf(
                        "Keep","Remove","Uninstall"
                    )
                    actions.forEachIndexed{index, action ->
                        Box(
                            Modifier
                                .fillMaxSize()
                                .weight(1F)
                                .padding(horizontal = 5.dp)
                                .border(width = 2.dp, color = Color.Black, shape= RectangleShape)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ){
                                    when(index){
                                        0 -> onDismiss()
                                        1 -> remove()
                                        2 -> uninstall()
                                        else -> onDismiss()
                                    }
                                }
                                .background(Color.White)
                        ){
                            Text(
                                text = action,
                                textAlign = TextAlign.Center,
                                fontFamily = poppinsFont,
                                fontSize = 16.sp,
                                color = Color.Black,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .fillMaxWidth()
                            )
                        }
                    }
                }
            },
            dismissButton = {},
        )
    }
}