package com.app.hackathon.presentation.view.search

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.app.hackathon.presentation.R
import com.app.hackathon.presentation.base.BaseActivity
import com.app.hackathon.presentation.databinding.ActivityVoiceSearchBinding
import com.app.hackathon.presentation.widget.Constants.SEARCH_QUERY
import com.app.hackathon.presentation.widget.extensions.setStatusBarTransparent
import com.app.hackathon.presentation.widget.extensions.statusBarHeight

class VoiceSearchActivity(override val layoutResId: Int = R.layout.activity_voice_search) :
    BaseActivity<ActivityVoiceSearchBinding>() {

    var mRecognizer: SpeechRecognizer? = null


    override fun initActivity() {
        // 상태바 투명화
        setScreen()
        // 음성 인식 설정
        setRecognizer()
        setClickListener()
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
        mRecognizer?.startListening(
            // 여분의 키
            Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).putExtra(
                RecognizerIntent.EXTRA_CALLING_PACKAGE,
                packageName
            ).putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR") // 한국어 설정
        )
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
                mRecognizer?.cancel()
                mRecognizer?.startListening(
                    Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).putExtra(
                        RecognizerIntent.EXTRA_CALLING_PACKAGE,
                        packageName
                    ).putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
                )
            }

            // 인식 결과가 준비되면 호출됨
            override fun onResults(results: Bundle?) {
                val matches = results!!.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

                // 인식된 음성을 전달하며 액티비티 종료
                setResult(RESULT_OK, Intent().apply {
                    putExtra(SEARCH_QUERY, matches?.get(0))
                })
                finish()
            }

            // 부분 인식 결과를 사용할 수 있을 때 호출
            override fun onPartialResults(p0: Bundle?) {}

            // 향후 이벤트를 추가하기 위해 예약
            override fun onEvent(p0: Int, p1: Bundle?) {}
        }
    }
}