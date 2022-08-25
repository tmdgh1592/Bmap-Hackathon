package com.app.hackathon.presentation.view.search

import android.content.Intent
import android.view.View
import android.view.inputmethod.EditorInfo
import com.app.hackathon.domain.entity.SearchHistoryEntity
import com.app.hackathon.presentation.R
import com.app.hackathon.presentation.base.BaseActivity
import com.app.hackathon.presentation.databinding.ActivityTextSearchBinding
import com.app.hackathon.presentation.presenter.search.SearchContract
import com.app.hackathon.presentation.presenter.search.SearchPresenter
import com.app.hackathon.presentation.widget.Constants.SEARCH_QUERY
import com.app.hackathon.presentation.widget.adapter.HistoryAdapter
import com.app.hackathon.presentation.widget.extensions.setStatusBarTransparent
import com.app.hackathon.presentation.widget.extensions.statusBarHeight
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TextSearchActivity(override val layoutResId: Int = R.layout.activity_text_search) :
    BaseActivity<ActivityTextSearchBinding>(), SearchContract.View {

    @Inject
    lateinit var presenter: SearchPresenter
    lateinit var historyAdapter: HistoryAdapter

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
            // 검색 에딧텍스트 설정
            setSearchEditText()

            // 검색 히스토리 리사이클러뷰 어댑터 설정
            setHistoryRvAdapter()
        }
    }

    private fun ActivityTextSearchBinding.setHistoryRvAdapter() {
        historyAdapter = HistoryAdapter(mutableListOf(), object : HistoryAdapter.OnClickListener {
            override fun onClickItem(item: SearchHistoryEntity) {
                finishWithResult(item.lotName)
            }

            override fun onDelete(item: SearchHistoryEntity) {
                presenter.requestRemoveHistory(item)
            }
        })
        historyRv.adapter = historyAdapter
        presenter.requestSearchHistory() // 검색 히스토리 데이터 요청
    }

    private fun ActivityTextSearchBinding.setSearchEditText() {
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            var isHandled = false
            val searchText = searchEditText.text.toString()

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                isHandled = true
                presenter.requestSaveHistory(searchText)
            }

            return@setOnEditorActionListener isHandled
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


    override fun showSearchHistory(searchHistoryList: List<SearchHistoryEntity>) {
        historyAdapter.updateData(searchHistoryList)
    }

    override fun removeSearchHistory(searchHistoryEntity: SearchHistoryEntity) {
        historyAdapter.removeData(searchHistoryEntity)
    }

    override fun finishWithResult(lotName: String) {
        val resultIntent = Intent().putExtra(SEARCH_QUERY, lotName)
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    override fun justFinish() {
        finish()
    }


}