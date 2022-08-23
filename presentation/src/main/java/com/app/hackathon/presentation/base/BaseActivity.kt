package com.app.hackathon.presentation.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.snackbar.Snackbar


abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {

    lateinit var binding: T
        private set

    abstract val layoutResId: Int
    abstract fun initActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Override될 layoutResId로 data binding 객체 생성
        binding = DataBindingUtil.setContentView(this, layoutResId)
        // Live data를 사용하기 위한 lifecycleOwner 지정
        binding.lifecycleOwner = this@BaseActivity
        initActivity()
    }

    // 토스트 띄우기
    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    // 스낵바 띄우기
    fun showSnackBar(msg: String) {
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG).show()
    }
}