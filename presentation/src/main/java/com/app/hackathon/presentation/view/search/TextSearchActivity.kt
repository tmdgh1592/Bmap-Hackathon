package com.app.hackathon.presentation.view.search

import android.content.Intent
import android.view.View
import android.view.inputmethod.EditorInfo
import com.app.hackathon.domain.model.SearchHistory
import com.app.hackathon.presentation.R
import com.app.hackathon.presentation.base.BaseActivity
import com.app.hackathon.presentation.databinding.ActivityTextSearchBinding
import com.app.hackathon.presentation.presenter.map.MapPresenter
import com.app.hackathon.presentation.presenter.search.SearchContract
import com.app.hackathon.presentation.presenter.search.SearchPresenter
import com.app.hackathon.presentation.widget.Constants.SEARCH_QUERY
import com.app.hackathon.presentation.widget.extensions.setStatusBarTransparent
import com.app.hackathon.presentation.widget.extensions.statusBarHeight
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TextSearchActivity(override val layoutResId: Int = R.layout.activity_text_search) :
    BaseActivity<ActivityTextSearchBinding>(), SearchContract.View {

    @Inject
    lateinit var presenter: SearchPresenter

    override fun initActivity() {
        // 상태바 투명화
        setScreen()

        presenter.onAttach(this)
        presenter.requestSearchHistory()

        setClickListener() // 클릭 리스너 설정
        initView() // 뷰 설정
    }


    private fun initView() {
        with(binding) {
            searchEditText.setOnEditorActionListener { textView, actionId, keyEvent ->
                var isHandled = false
                val searchText = searchEditText.text.toString()

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    isHandled = true

                    val resultIntent = Intent().putExtra(SEARCH_QUERY, searchText)
                    setResult(RESULT_OK, resultIntent)
                    finish()
                }

                return@setOnEditorActionListener isHandled
            }
        }
    }


    private fun setScreen() {
        setStatusBarTransparent()
        // 상태 바, 네비게이션 높이 만큼 padding 주기
        binding.root.setPadding(0, statusBarHeight(), 0, 0)
    }


    private fun setClickListener() {
        with(binding) {
            backBtn.setOnClickListener { finish() }
        }
    }

    override fun showLoading(isShow: Boolean) {
        if (isShow) {
            binding.loadingBar.visibility = View.VISIBLE
        } else {
            binding.loadingBar.visibility = View.GONE
        }
    }


    override fun showSearchHistory(searchHistoryList: List<SearchHistory>) {

    }
}