package com.app.hackathon.presentation.view.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import com.app.hackathon.presentation.R
import com.app.hackathon.presentation.view.map.MapActivity
import com.app.hackathon.presentation.widget.extensions.setStatusBarTransparent
import com.app.hackathon.presentation.widget.extensions.statusBarHeight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        // 상태바 투명화
        setScreen()
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            startActivity(Intent(this@SplashActivity, MapActivity::class.java))
            finish()
            // overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in)
        }
    }

    private fun setScreen() {
        setStatusBarTransparent()
        // 상태 바, 네비게이션 높이 만큼 padding 주기
        findViewById<ConstraintLayout>(R.id.root_container).setPadding(0, statusBarHeight(), 0, 0)
    }
}