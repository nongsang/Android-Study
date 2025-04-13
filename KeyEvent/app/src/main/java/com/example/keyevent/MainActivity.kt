package com.example.keyevent

import android.os.Bundle
import android.os.SystemClock
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.keyevent.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // 뒤로가기 버튼을 누른 시각을 저장
    var initTime = 0L
    // 멈춘 시각을 저장
    var pauseTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)    // active_main.xml을 화면에 출력

        binding.startButton.setOnClickListener {    // activity_main.xml에서 startButton id를 가진 UI를 찾아서 클릭 이벤트 등록
            binding.Chronometer.base = SystemClock.elapsedRealtime() + pauseTime    // 시스템 시계를 이용해서
            binding.Chronometer.start()
            binding.stopButton.isEnabled = true
            binding.resetButton.isEnabled = true
            binding.startButton.isEnabled = false
        }

        binding.stopButton.setOnClickListener {
            pauseTime = binding.Chronometer.base - SystemClock.elapsedRealtime()
            binding.Chronometer.stop()
            binding.stopButton.isEnabled = false
            binding.resetButton.isEnabled = true
            binding.startButton.isEnabled = true
        }

        binding.resetButton.setOnClickListener {
            pauseTime = 0L
            binding.Chronometer.stop()
            binding.stopButton.isEnabled = false
            binding.resetButton.isEnabled = false
            binding.startButton.isEnabled = true
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 안드로이드 키 입력이 뒤로가기라면
            if (System.currentTimeMillis() - initTime > 3000) { // 3초동안
                Toast.makeText(this, "종료하려면 한 번 더 누르세요!!", Toast.LENGTH_SHORT).show()   // 토스트 출력
                initTime = System.currentTimeMillis()   // 앱이 백그라운드로 돌아간 시간을 저장
                return true
            }
        }

        return super.onKeyDown(keyCode, event)
    }
}