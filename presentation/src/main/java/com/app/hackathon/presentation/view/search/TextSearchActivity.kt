package com.app.hackathon.presentation.view.search

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.app.hackathon.domain.entity.LotEntity
import com.app.hackathon.domain.entity.SearchHistoryEntity
import com.app.hackathon.presentation.R
import com.app.hackathon.presentation.base.BaseActivity
import com.app.hackathon.presentation.databinding.ActivityTextSearchBinding
import com.app.hackathon.presentation.presenter.search.SearchContract
import com.app.hackathon.presentation.presenter.search.SearchPresenter
import com.app.hackathon.presentation.widget.Constants.LATITUDE
import com.app.hackathon.presentation.widget.Constants.LONGITUDE
import com.app.hackathon.presentation.widget.Constants.SEARCH_RESULT
import com.app.hackathon.presentation.widget.Constants.VOICE_QUERY
import com.app.hackathon.presentation.widget.adapter.HistoryAdapter
import com.app.hackathon.presentation.widget.adapter.SearchLotAdapter
import com.app.hackathon.presentation.widget.extensions.convertToLotEntity
import com.app.hackathon.presentation.widget.extensions.setStatusBarTransparent
import com.app.hackathon.presentation.widget.extensions.statusBarHeight
import com.app.hackathon.presentation.widget.utils.TopItemDecorator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TextSearchActivity(override val layoutResId: Int = R.layout.activity_text_search) :
    BaseActivity<ActivityTextSearchBinding>(), SearchContract.View {

    @Inject
    lateinit var presenter: SearchPresenter
    lateinit var historyAdapter: HistoryAdapter
    lateinit var searchLotAdapter: SearchLotAdapter

    override fun initActivity() {
        // 상태바 투명화
        setScreen()

        presenter.onAttach(this)
        presenter.setLatLng(
            intent.getDoubleExtra(LATITUDE, 0.0),
            intent.getDoubleExtra(LONGITUDE, 0.0)
        )
        presenter.requestSearchHistory()

        setClickListener() // 클릭 리스너 설정
        initView() // 뷰 설정
        setVoiceQuery() // 음성 인식으로 검색한 경우
    }

    private fun setVoiceQuery() {
        val voiceQuery: String? = intent.getStringExtra(VOICE_QUERY)
        voiceQuery.let { query ->
            binding.searchEditText.setText(query)
            //presenter.requestLotList(query!!)
        }
    }


    private fun initView() {
        with(binding) {
            // 검색 에딧텍스트 설정
            setSearchEditText()
            // 검색 히스토리 리사이클러뷰 어댑터 설정
            setHistoryRvAdapter()
            // 검색 결과 어댑터 설정
            setSearchResultRvAdapter()
        }
    }

    private fun ActivityTextSearchBinding.setHistoryRvAdapter() {
        historyAdapter = HistoryAdapter(mutableListOf(), object : HistoryAdapter.OnClickListener {
            override fun onClickItem(item: SearchHistoryEntity) {
                finishWithSearchResult(item.convertToLotEntity())
            }

            override fun onDelete(item: SearchHistoryEntity) {
                presenter.requestRemoveHistory(item)
            }
        })
        historyRv.adapter = historyAdapter
        presenter.requestSearchHistory() // 검색 히스토리 데이터 요청
    }


    private fun ActivityTextSearchBinding.setSearchResultRvAdapter() {
        searchLotAdapter =
            SearchLotAdapter(mutableListOf(), object : SearchLotAdapter.OnClickListener {
                override fun onClickItem(item: LotEntity) {
                    presenter.requestSaveHistory(item.convertToLotEntity())
                    finishWithSearchResult(item)
                }
            })
        historyRv.adapter = searchLotAdapter
        historyRv.addItemDecoration(TopItemDecorator(22))
    }


    private fun ActivityTextSearchBinding.setSearchEditText() {
        // 검색을 하면 서버에 주차장 데이터 요청
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                searchQuery: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                val query = searchQuery.toString()
                presenter.requestLotList(query)

                // 두 글자 이상 검색어이면 검색어 데이터 바인딩
                if (query.length > 1) {
                    searchQueryTv.text = query
                }
            }

            override fun beforeTextChanged(
                searchQuery: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(searchQuery: Editable?) {}
        })
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

    override fun refreshSearchResult(lotList: List<LotEntity>) {
        searchLotAdapter.updateData(lotList)
    }

    override fun showSearchHistoryList() {
        binding.historyRv.visibility = View.VISIBLE
        binding.parkingLotResultContainer.visibility = View.GONE
    }

    override fun showSearchResultList() {
        binding.parkingLotResultContainer.visibility = View.VISIBLE
        binding.historyRv.visibility = View.GONE
    }

    override fun showErrorToast(errorMessage: String) {
        showToast(errorMessage)
    }


    override fun finishWithSearchResult(lot: LotEntity) {
        val resultIntent = Intent().putExtra(SEARCH_RESULT, lot)
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    override fun justFinish() {
        finish()
    }


}