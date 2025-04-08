package com.example.android_basic

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 텍스트 요소
        val name = TextView(this).apply {
            typeface = Typeface.DEFAULT_BOLD
            text = "Lake Louise"                // 표시할 문자열
        }

        // 이미지 요소
        val image = ImageView(this).also {
            // res/drawable/ic_launcher_background로 등록된 리소스 출력
            it.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_launcher_background))
        }

        val address = TextView(this).apply {
            typeface = Typeface.DEFAULT_BOLD
            text = "Lake Louise, AB, 캐나다"
        }

        // 화면 구성
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL // 세로보기
            gravity = Gravity.CENTER    // 중앙 정렬

            // 텍스트
            addView(name, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            // 이미지
            addView(image, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            // 텍스트
            addView(address, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }

        // 화면 출력
        setContentView(layout);
    }
}