package com.so.composecarhom

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.so.composecarhome.AppGridItem
import com.so.composecarhome.MinimalAppIcon
import com.so.composecarhome.StarryAnimatedBackground
import com.so.composecarhome.ZenAnimatedBackground
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter

import java.util.*

// ---------- DATA CLASS ----------
data class AppInfo(val name: String, val icon: Drawable?, val intent: Intent)

// ---------- MAIN SCREEN (PAGER) ----------
@Composable
fun LauncherScreen() {
    val context = LocalContext.current
    val allApps = remember { getLaunchableApps(context) }
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()

    // BACK HANDLER: If on App Drawer (page 1), go back to Home (page 0)
    BackHandler(enabled = pagerState.currentPage == 1) {
        coroutineScope.launch {
            pagerState.animateScrollToPage(0)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // --- 1. ANIMATED ZEN BACKGROUND (Behind everything) ---
        //ZenAnimatedBackground()
        StarryAnimatedBackground()

        // --- 2. PAGES (Foreground) ---
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            // Makes the pager stick to the edges nicely
        ) {  page: Int ->
            when (page) {
                0 -> HomePage(allApps) // Page 0: Clock + minimal shortcuts
                1 -> AppDrawerPage(allApps) // Page 1: All apps grid
            }
        }

        // --- 3. PAGE INDICATOR (Minimal dots at the bottom) ---
        PageIndicator(
            currentPage = pagerState.currentPage,
            totalPages = 2,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}

// ---------- PAGE 0: HOME (Zen Clock + Favorite Apps) ----------
fun getGreeting(time: LocalTime): String {
    return when (time.hour) {
        in 5..11 -> "Good Morning"
        in 12..17 -> "Good Afternoon"
        in 18..20 -> "Good Evening"
        else -> "Good Night"
    }
}

@Composable
fun HomePage(apps: List<AppInfo>) {

    var currentTime by remember { mutableStateOf(LocalTime.now()) }
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = LocalTime.now()
             delay(60000) // Use this if you ONLY want hours:minutes (battery friendly)
        }
    }

    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val formattedTime = currentTime.format(timeFormatter)
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp, vertical = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Large Zen Clock
        Text(
            text = formattedTime,
            fontSize = 96.sp,
            fontWeight = FontWeight.Light,
            color = Color.White.copy(alpha = 0.9f),
            letterSpacing = 4.sp
        )
        Text(
            text =getGreeting(currentTime),
            fontSize = 24.sp,
            fontWeight = FontWeight.Thin,
            color = Color.White.copy(alpha = 0.6f),
            modifier = Modifier.padding(top = 8.dp)
        )
        Spacer(modifier = Modifier.weight(1f))

        // Quick Shortcuts (Top 4 apps)
        Text(
            text = "",
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.5f),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            apps.take(4).forEach { app: AppInfo  ->
                MinimalAppIcon(app) {
                    context.startActivity(app.intent)
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))

        // Swipe hint
        Text(
            text = "↕ Swipe up for all apps",
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.3f),
            modifier = Modifier.padding(top = 20.dp)
        )
    }
}

// ---------- PAGE 1: APP DRAWER (All Apps) ----------
@Composable
fun AppDrawerPage(apps: List<AppInfo>) {

    val context = LocalContext.current

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        items(apps) { app: AppInfo ->
            AppGridItem(app) {
                context.startActivity(app.intent)
            }
        }
    }
}

// ---------- COMPONENT: Page Indicator Dots ----------
@Composable
fun PageIndicator(currentPage: Int, totalPages: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(totalPages) { index ->
            Box(
                modifier = Modifier
                    .size(if (index == currentPage) 12.dp else 8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(
                        if (index == currentPage) Color.White
                        else Color.White.copy(alpha = 0.3f)
                    )
            )
        }
    }
}

// ---------- FUNCTION: Fetch Installed Apps ----------
fun getLaunchableApps(context: Context): List<AppInfo> {
    val pm = context.packageManager
    val mainIntent = Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER)
    val resolveInfos = pm.queryIntentActivities(mainIntent, PackageManager.GET_RESOLVED_FILTER)
    return resolveInfos.mapNotNull { resolveInfo ->
        val activityInfo = resolveInfo.activityInfo
        val packageName = activityInfo.packageName
        val className = activityInfo.name
        val intent = Intent(Intent.ACTION_MAIN).setClassName(packageName, className)
        val name = activityInfo.loadLabel(pm).toString()
        val icon = activityInfo.loadIcon(pm)
        AppInfo(name, icon, intent)
    }.sortedBy { it.name }
}