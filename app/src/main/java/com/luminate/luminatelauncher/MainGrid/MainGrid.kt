package com.luminate.luminatelauncher.MainGrid

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import androidx.core.graphics.drawable.toBitmap
import com.luminate.luminatelauncher.DEFAULTAPPS
import com.luminate.luminatelauncher.MainActivity
import com.luminate.luminatelauncher.R
import com.luminate.luminatelauncher.Vibrate
import com.luminate.luminatelauncher.getAppLabelColor
import com.luminate.luminatelauncher.getAppTitle
import com.luminate.luminatelauncher.getImage
import com.luminate.luminatelauncher.getScreenDp
import com.luminate.luminatelauncher.getStatusBarHeight
import com.luminate.luminatelauncher.removeItem
import com.luminate.luminatelauncher.ui.poppinsFont


val COLUMNS = 4
val ROWS = 7

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainGrid(dark: Boolean, activity:Activity, edit : Int) {

    val mContext = LocalContext.current
    val sharedPreferences = mContext.getSharedPreferences("luminatelauncherprefs", Context.MODE_PRIVATE)
    val listasstring : String = sharedPreferences.getString("applist", DEFAULTAPPS).toString()
    var list : List<String> = listOf()
    for(application in listasstring.split(":")){ list = list + application }
    val rearrangedapps = rearrangeApplist(list)
    var stringaslist : String = ""
    for(string in rearrangedapps.first){
        stringaslist += "$string:"
    }
    with (sharedPreferences.edit()) {
        putString("applist", stringaslist)
        apply()
    }
    Log.d("TIMER", "Time when got applist : " + System.currentTimeMillis())

    MainCarousel(
        rearrangedapps.first,
        rearrangedapps.second,
        dark,
        activity,
        edit
    )

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainCarousel(packagelist : List<String>, pages: Int, dark : Boolean, activity : Activity, initedit : Int) {

    val context = LocalContext.current
    val screenWidth = getScreenDp(context).first
    val screenHeight = getScreenDp(context).second
    val itemsize = (screenWidth / 6.5).dp
    val selections = mutableListOf<String>()

    var actualpage by remember { mutableStateOf(0) }

    val verticalspace = (screenHeight / 8.6).dp
    val mainpadding = (screenWidth / 13.11).dp
    val statusbarsize = getStatusBarHeight(context).dp
    Log.d("MATHS", "Status size : $statusbarsize")
    var edit by remember {
        mutableStateOf(initedit)
    }
    var showDialog by remember {
        mutableStateOf(0)
    }

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        Modifier
            .background(if (edit == 1) Color(0x29000000) else Color(0x00000000))
            .fillMaxSize()
            .combinedClickable(
                onClick = {
                    edit = 0
                    showDialog = 0
                    selections.clear()
                },
                onLongClick = {
                    if (edit == 0) {
                        edit = 1
                        Vibrate(200, context)
                    }
                },
                interactionSource = interactionSource,
                indication = null
            )){
        val items: List<@Composable () -> Unit> = List(pages + if (edit == 1) 1 else 0) {
            {

                actualpage = it

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(LocalConfiguration.current.screenWidthDp.dp)
                ){

                    packagelist.forEachIndexed { index, packageinfo ->

                        val packagename = packageinfo.split(",")[0]
                        val apposition = packageinfo.split(",")[1]

                        val page_num = apposition.split(";")[0].toInt()

                        if (it == page_num) {



                            val row_pos = apposition.split(";")[1].toInt()
                            val column_pos = apposition.split(";")[2].toInt()
                            val y_pos = (row_pos) * verticalspace + mainpadding + statusbarsize
                            val x_pos = (column_pos) * (itemsize + mainpadding) + mainpadding/ 2

                            val appImage = getImage(context, packagename)
                            val textColor = getAppLabelColor(dark)

                            var selected by remember { mutableStateOf(false) }

                            var draggable by remember { mutableStateOf(false) }

                            Column(
                                modifier = Modifier
                                    .width(itemsize + mainpadding)
                                    .zIndex(4F)
                                    .offset(
                                        x = x_pos,
                                        y = (if (edit == 1) { (verticalspace * 0.7.toFloat()).value } else { 0.toFloat() } + (y_pos.value * if (edit == 1) { 0.9.toFloat() } else { 1F })).dp
                                    )
                                    .padding(3.dp)
                                    .combinedClickable(
                                        onClick = {
                                            if (edit == 1) {

                                                if (selected) {
                                                    selections.removeAt(
                                                        selections.indexOf(
                                                            packageinfo
                                                        )
                                                    )
                                                } else {
                                                    selections += packageinfo
                                                }
                                                Log.d("Selection", "Selection : $selections")

                                                selected = !selected

                                            } else {
                                                val launchIntent: Intent? =
                                                    context.packageManager.getLaunchIntentForPackage(
                                                        packagename
                                                    )
                                                launchIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                if (launchIntent != null) {

                                                    activity.overridePendingTransition(
                                                        R.anim.slide_in_up,
                                                        R.anim.slide_out_down
                                                    )
                                                    context.startActivity(launchIntent)
                                                }
                                            }
                                        },
                                        onLongClick = {
                                            draggable = !draggable
                                            edit = 0
                                        },
                                        interactionSource = interactionSource,
                                        indication = null,
                                    ),

                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .zIndex(5F)
                                        .size(itemsize + itemsize / 4, itemsize + itemsize / 7)
                                ) {
                                    Image(
                                        bitmap = appImage.toBitmap().asImageBitmap(),
                                        modifier = Modifier
                                            .align(Alignment.BottomCenter)
                                            .size(itemsize)
                                            .clip(RoundedCornerShape(17.dp)),
                                        contentScale = ContentScale.FillBounds,
                                        contentDescription = null
                                    )
                                    if (edit == 1) {
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .size(itemsize / 3),
                                        ){
                                            Image(
                                                painter = painterResource(id = R.drawable.empty),
                                                modifier = Modifier
                                                    .align(Alignment.Center)
                                                    .fillMaxSize(),
                                                contentScale = ContentScale.FillBounds,
                                                contentDescription = null
                                            )
                                            if (selected){
                                                Image(
                                                    painter = painterResource(id = R.drawable.selection),
                                                    modifier = Modifier
                                                        .align(Alignment.Center)
                                                        .size(itemsize / 5),
                                                    contentScale = ContentScale.FillBounds,
                                                    contentDescription = null
                                                )
                                            }
                                        }
                                    }else {
                                        selected = false
                                    }
                                }
                                Text(
                                    text = getAppTitle(context, packagename),
                                    color = textColor,
                                    fontFamily = poppinsFont,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }

                Log.d("TIMER", "Time after displayed apps : " + System.currentTimeMillis())
            }
        }
        val bottompadding = screenHeight/7.06
        Row(
            modifier = Modifier
                .height((bottompadding + screenHeight / 116).dp)
                .padding(bottom = bottompadding.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ){
            for(dot in 0 until pages+1){
                if (edit == 0 && dot == pages) { break }
                Box(
                    modifier = Modifier
                        .size(
                            (screenHeight/58).dp,
                            (screenHeight/116).dp,
                        ),
                ){
                    Image(
                        painter = painterResource(id = R.drawable.selection),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size((screenHeight / 116).dp),
                        colorFilter = ColorFilter.tint(
                            if (dot == actualpage) Color(0xFFFFFFFF)
                            else Color(0xFFBBBBBB)
                        )
                    )
                }
            }
        }


        Log.d("TIMER", "Time before lazyrow: " + System.currentTimeMillis())

        val state = rememberLazyListState()
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            state = state,
            flingBehavior = rememberSnapFlingBehavior(lazyListState = state),
            content = {
                items(items.size) { index ->
                    items[index]() // Call the composable function to create the item
                }
            }
        )
        if(edit==0){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemsize + mainpadding)
                    .padding(bottom = mainpadding, start = mainpadding, end = mainpadding)
                    .align(Alignment.BottomCenter)
                    .background(
                        color = Color(0xFFF6D2FF),
                        shape = RoundedCornerShape(itemsize / 2)
                    )
            ){
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Spacer(Modifier.width((screenWidth/(375/10)).dp))
                    Box(
                        Modifier.size(itemsize/2)
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.search),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(itemsize / 3.375.toFloat()),
                            contentScale = ContentScale.FillBounds,
                            contentDescription = null
                        )
                    }
                    Text(
                        text = "Search",
                        color = Color(0xFFB275D8),
                        fontFamily = poppinsFont,
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                    )
                    Spacer(
                        Modifier
                            .fillMaxWidth()
                            .weight(1F))
                    Box(
                        Modifier.size(itemsize)
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.arrow),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(itemsize / 2.4545.toFloat()),
                            contentScale = ContentScale.FillHeight,
                            contentDescription = null
                        )
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemsize + mainpadding)
                    .padding(
                        bottom = mainpadding,
                        start = screenWidth.dp / 15,
                        end = screenWidth.dp / 15
                    )
                    .align(Alignment.BottomCenter)
            ){
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Box(
                        Modifier
                            .padding(horizontal = (screenWidth / 125).dp)
                            .background(
                                color = Color(0x61D9D9D9),
                                shape = RoundedCornerShape(itemsize / 2)
                            )
                            .width(screenWidth.dp / 3.95.toFloat())
                            .height(itemsize)
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.wallpaper),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(itemsize / 2),
                            contentScale = ContentScale.FillBounds,
                            contentDescription = null
                        )
                    }

                    Row(
                        Modifier
                            .padding(horizontal = (screenWidth / 125).dp)
                            .background(
                                color = Color(0x61D9D9D9),
                                shape = RoundedCornerShape(itemsize / 2)
                            )
                            .fillMaxWidth()
                            .weight(1F)
                            .height(itemsize),
                        verticalAlignment = Alignment.CenterVertically,
                    ){
                        Spacer(Modifier.width(screenWidth.dp/25))
                        Image(
                            painter = painterResource(id = R.drawable.widget),
                            modifier = Modifier
                                .size(itemsize / 2),
                            contentScale = ContentScale.FillBounds,
                            contentDescription = null
                        )
                        Text(
                            text = "Widgets",
                            color = Color(0xFF2D2D2D),
                            fontFamily = poppinsFont,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1F)
                        )
                        Spacer(Modifier.width(screenWidth.dp/25))
                    }

                    Box(
                        Modifier
                            .padding(horizontal = (screenWidth / 125).dp)
                            .background(
                                color = Color(0x61D9D9D9),
                                shape = RoundedCornerShape(itemsize / 2)
                            )
                            .width(screenWidth.dp / 3.95.toFloat())
                            .height(itemsize)
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.settings),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(itemsize / 2),
                            contentScale = ContentScale.FillBounds,
                            contentDescription = null
                        )
                    }
                }
            }
        }

        if(edit==1){

            val removeInteractionSource = remember { MutableInteractionSource() }
            var restart by remember { mutableStateOf(false) }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemsize + mainpadding + mainpadding / 2)
                    .padding(
                        top = mainpadding + mainpadding / 2,
                        start = screenWidth.dp / 15,
                        end = screenWidth.dp / 15
                    )
                    .align(Alignment.TopCenter)
            ){
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ){

                    var newList : List<String> = packagelist

                    Box(
                        Modifier
                            .padding(horizontal = (screenWidth / 125).dp)
                            .clickable(
                                interactionSource = removeInteractionSource,
                                indication = null
                            ) { edit = 0 }
                            .background(
                                color = Color(0x61D9D9D9),
                                shape = RoundedCornerShape(itemsize / 2)
                            )
                            .size(itemsize)
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.check),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(itemsize / 2),
                            contentScale = ContentScale.FillBounds,
                            contentDescription = null
                        )
                    }

                    Row(
                        Modifier
                            .clickable(
                                interactionSource = removeInteractionSource,
                                indication = null
                            ) {
                                selections.forEach { removeItem(it, context) }
                                val intent = Intent(context, MainActivity::class.java)
                                intent.putExtra("edit", 1)
                                context.startActivity(intent)
                                activity.overridePendingTransition(R.anim.no_anim, R.anim.no_anim)
                            }
                            .padding(horizontal = (screenWidth / 125).dp)
                            .background(
                                color = Color(0x61D9D9D9),
                                shape = RoundedCornerShape(itemsize / 2)
                            )
                            .fillMaxWidth()
                            .weight(1F)
                            .height(itemsize),
                        verticalAlignment = Alignment.CenterVertically,
                    ){
                        Spacer(Modifier.width(screenWidth.dp/10))
                        Image(
                            painter = painterResource(id = R.drawable.trash),
                            modifier = Modifier
                                .size(itemsize / 2),
                            contentScale = ContentScale.FillBounds,
                            contentDescription = null
                        )
                        Text(
                            text = "Remove",
                            color = Color(0xFF2D2D2D),
                            fontFamily = poppinsFont,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1F)
                        )
                        Spacer(Modifier.width(screenWidth.dp/10))
                    }

                    Box(
                        Modifier
                            .padding(horizontal = (screenWidth / 125).dp)
                            .background(
                                color = Color(0x61D9D9D9),
                                shape = RoundedCornerShape(itemsize / 2)
                            )
                            .size(itemsize)
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.folder),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(itemsize / 2),
                            contentScale = ContentScale.FillBounds,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}


fun rearrangeApplist(list: List<String>): Pair<List<String>,Int> {
    val positions = mutableListOf<String>()
    val reallist = mutableListOf<String>()
    var pages : Int = 1
    list.forEachIndexed { index, packageinfo ->
        if (packageinfo.isNotEmpty()){
            val position = packageinfo.split(",")[1]
            val modifiedpackage : String

            val taken : Boolean = position in positions
            if (taken) {
                modifiedpackage = packageinfo.split(",")[0] + "," + getNextPos(positions)
            } else {
                modifiedpackage = packageinfo
            }

            reallist.add(modifiedpackage)
            positions.add(modifiedpackage.split(",")[1])
        }
    }

    positions.forEach { item ->
        val page = item.split(";")[0].toInt()
        if (page > pages-1) {pages = page + 1}
    }

    return Pair(reallist,pages)
}

fun getNextPos(positions : List<String>) : String {
    for (page in 0 until 10) {
        for(row in 0 until ROWS){
            for(column in 0 until COLUMNS) {
                if (!("$page;$row;$column" in positions)) {
                    return "$page;$row;$column"
                }
            }
        }
    }
    return "-1;0;0"
}