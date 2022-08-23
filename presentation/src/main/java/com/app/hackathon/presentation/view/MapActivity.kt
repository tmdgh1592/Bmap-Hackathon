package com.app.hackathon.presentation.view

import com.app.hackathon.presentation.R
import com.app.hackathon.presentation.base.BaseActivity
import com.app.hackathon.presentation.databinding.ActivityMapBinding
import com.app.hackathon.presentation.presenter.MapContract
import com.app.hackathon.presentation.widget.extensions.navigationHeight
import com.app.hackathon.presentation.widget.extensions.setStatusBarTransparent
import com.app.hackathon.presentation.widget.extensions.statusBarHeight
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MapActivity(override val layoutResId: Int = R.layout.activity_map) :
    BaseActivity<ActivityMapBinding>(), MapContract.View {
    @Inject
    lateinit var presenter: MapContract.Presenter<MapContract.View>

    override fun initActivity() {
        // 상태바 투명화
        setStatusBarTransparent()
        // 상태 바, 네비게이션 높이 만큼 padding 주기
        binding.innerContainer.setPadding(0, statusBarHeight(), 0, navigationHeight())
        presenter.onAttach(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }
}