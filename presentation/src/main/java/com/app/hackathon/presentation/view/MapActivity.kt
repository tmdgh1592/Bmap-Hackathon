package com.app.hackathon.presentation.view

import com.app.hackathon.presentation.R
import com.app.hackathon.presentation.base.BaseActivity
import com.app.hackathon.presentation.databinding.ActivityMapBinding
import com.app.hackathon.presentation.presenter.MapContract
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MapActivity(override val layoutResId: Int = R.layout.activity_map) :
    BaseActivity<ActivityMapBinding>(), MapContract.View {
    @Inject
    lateinit var presenter: MapContract.Presenter<MapContract.View>

    override fun initActivity() {
        presenter.onAttach(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }
}