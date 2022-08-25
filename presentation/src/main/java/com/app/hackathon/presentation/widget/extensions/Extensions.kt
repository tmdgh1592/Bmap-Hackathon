package com.app.hackathon.presentation.widget.extensions

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import com.app.hackathon.domain.entity.LotEntity
import com.app.hackathon.domain.entity.SearchHistoryEntity
import com.bumptech.glide.Glide

fun Activity.setStatusBarTransparent() {
    window.apply {
        setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }
    if (Build.VERSION.SDK_INT >= 30) {    // API 30 에 적용
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}

fun Context.statusBarHeight(): Int {
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")

    return if (resourceId > 0) resources.getDimensionPixelSize(resourceId)
    else 0
}

fun Context.navigationHeight(): Int {
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")

    return if (resourceId > 0) resources.getDimensionPixelSize(resourceId)
    else 0
}

fun Context.loadImage(imgRes: Int, view: ImageView) {
    Glide.with(this).load(imgRes).into(view)
}

fun Context.checkLocationPermission(): Boolean {
    return (ActivityCompat.checkSelfPermission(this,
        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this,
        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
}

fun Context.checkRecordAudioPermission(): Boolean {
    return (ActivityCompat.checkSelfPermission(this,
        Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
}

fun SearchHistoryEntity.convertToLotEntity(): LotEntity {
    return LotEntity(lotName, newAddr, latitude, longitude)
}

fun LotEntity.convertToLotEntity(): SearchHistoryEntity {
    return SearchHistoryEntity(parkName, newAddr, latitude, longitude)
}