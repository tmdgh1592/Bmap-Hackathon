package com.app.hackathon.presentation.widget.extensions

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.content.res.Resources.getSystem
import android.os.Build
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import com.app.hackathon.domain.entity.LotEntity
import com.app.hackathon.domain.entity.SearchHistoryEntity
import com.app.hackathon.presentation.R
import com.bumptech.glide.Glide
import kotlin.random.Random

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
    return LotEntity(parkCode, lotName, newAddr, latitude, longitude)
}

fun LotEntity.convertToLotEntity(): SearchHistoryEntity {
    return SearchHistoryEntity(parkCode, parkName, newAddr, latitude, longitude)
}


// 스크린 높이 구하기
fun getWindowHeight(): Int {
    return getSystem().displayMetrics.heightPixels
}

// 스크린 너비 구하기
fun getWindowWidth(): Int {
    return getSystem().displayMetrics.widthPixels
}

fun Boolean.toInt(): Int{
    return if (this) {
        1
    } else {
        -1
    }
}

fun Int.toBoolean(): Boolean {
    return this == 1
}

// 주차장 이름 길이를 바탕으로 랜덤으로 가져옴
fun provideRandomUnselectedMarkerImage(parkName: String): Int {
    return when (parkName.length) {
        in 0..6 -> {
            R.drawable.img_unselected_green_lot
        }
        in 7..11 -> {
            R.drawable.img_unselected_yellow_lot
        }
        else -> {
            R.drawable.img_unselected_red_lot
        }
    }
}

fun provideRandomSelectedMarkerImage(parkName: String): Int {
    return when (parkName.length) {
        in 0..6 -> {
            R.drawable.img_selected_green_lot
        }
        in 7..11 -> {
            R.drawable.img_selected_yellow_lot
        }
        else -> {
            R.drawable.img_selected_red_lot
        }
    }
}

fun provideRandomParkStateImage(parkName: String): Int {
    return when (parkName.length) {
        in 0..6 -> {
            R.drawable.ic_green_circle
        }
        in 7..11 -> {
            R.drawable.ic_yellow_circle
        }
        else -> {
            R.drawable.ic_red_circle
        }
    }
}