package com.app.hackathon.presentation.view.search

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import com.app.hackathon.presentation.R
import com.app.hackathon.presentation.base.BaseActivity
import com.app.hackathon.presentation.databinding.ActivityVoiceSearchBinding
import com.app.hackathon.presentation.view.map.MapActivity
import com.app.hackathon.presentation.widget.Constants
import com.app.hackathon.presentation.widget.Constants.VOICE_QUERY
import com.app.hackathon.presentation.widget.extensions.checkRecordAudioPermission
import com.app.hackathon.presentation.widget.extensions.setStatusBarTransparent
import com.app.hackathon.presentation.widget.extensions.statusBarHeight
import com.gun0912.tedpermission.rx3.TedPermission

class VoiceSearchActivity(override val layoutResId: Int = R.layout.activity_voice_search) :
    BaseActivity<ActivityVoiceSearchBinding>() {

    private var mRecognizer: SpeechRecognizer? = null
    private var currentLat: Double = 0.0
    private var currentLng: Double = 0.0

    override fun initActivity() {
        // 상태바 투명화
        setScreen()
        // 위도 경도 설정
        setLngLng()
        // 음성 인식 설정
        requestPermissions()
        setRecognizer()
        setClickListener()
    }

    private fun setLngLng() {
        currentLat = intent.getDoubleExtra(Constants.LATITUDE, 0.0)
        currentLng = intent.getDoubleExtra(Constants.LONGITUDE, 0.0)
    }

    // 음성 녹음 권한 요청
    private fun requestPermissions() {
        TedPermission.create()
            .setRationaleTitle("음성 권한 요청")
            .setRationaleMessage("음성 검색을 위해 권한이 필요합니다.") // "we need permission for read contact and find your location"
            .setPermissions(
                Manifest.permission.RECORD_AUDIO
            )
            .request()
            .subscribe({ tedPermissionResult ->
                if (!tedPermissionResult.isGranted) {
                    showToast("음성 권한을 허용해주세요.")
                    finish()
                } else {
                    startRecognizing()
                }
            }) { throwable -> Log.e(MapActivity.TAG, throwable.message.toString()) }
    }

    private fun setScreen() {
        setStatusBarTransparent()
        // 상태 바, 네비게이션 높이 만큼 padding 주기
        binding.root.setPadding(0, statusBarHeight(), 0, 0)
    }

    private fun setClickListener() {
        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    // 음성인식 설정
    private fun setRecognizer() {
        mRecognizer =
            SpeechRecognizer.createSpeechRecognizer(this) // 새로운 SpeechRecognizer 를 만드는 팩토리 메서드
        mRecognizer?.setRecognitionListener(provideRecognitionListener())

        // 음성 인식 권한이 있는지 체크
        if (!checkRecordAudioPermission()) return

        // 음성 인식 시작
        startRecognizing()
    }


    // 음성인식 리스너
    private fun provideRecognitionListener(): RecognitionListener {
        return object : RecognitionListener {

            // 말하기 시작할 준비가되면 호출
            override fun onReadyForSpeech(params: Bundle?) {}

            // 말하기 시작했을 때 호출
            override fun onBeginningOfSpeech() {}

            // 입력받는 소리의 크기를 알려줌
            override fun onRmsChanged(dB: Float) {}

            // 말을 시작하고 인식이 된 단어를 buffer에 담음
            override fun onBufferReceived(p0: ByteArray?) {}

            // 말하기가 끝났을 때
            override fun onEndOfSpeech() {}

            // 에러 발생
            override fun onError(error: Int) {
                Log.d("VoiceSearchActivity", "onError: 음성 녹음 에러로 인한 재실행")
                mRecognizer?.cancel()
                startRecognizing()
            }

            // 인식 결과가 준비되면 호출됨
            override fun onResults(results: Bundle?) {
                val matches = results!!.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

                // 인식된 음성을 전달
                startActivity(
                    Intent(
                        this@VoiceSearchActivity,
                        TextSearchActivity::class.java
                    ).putExtra(VOICE_QUERY, matches?.get(0))
                        .putExtra(Constants.LATITUDE, currentLat)
                        .putExtra(Constants.LONGITUDE, currentLng)
                )
                finish()
            }

            // 부분 인식 결과를 사용할 수 있을 때 호출
            override fun onPartialResults(p0: Bundle?) {}

            // 향후 이벤트를 추가하기 위해 예약
            override fun onEvent(p0: Int, p1: Bundle?) {}
        }
    }


    // 음성 인식 시작
    private fun startRecognizing() {
        mRecognizer?.startListening(
            // 여분의 키
            Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).putExtra(
                RecognizerIntent.EXTRA_CALLING_PACKAGE,
                packageName
            ).putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR") // 한국어 설정
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        mRecognizer?.cancel()
        mRecognizer?.destroy()
    }
}