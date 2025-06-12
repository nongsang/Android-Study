package com.example.todotest

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.todotest.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // 메뉴의 아이템이 선택되었을 때 호출
    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        R.id.menu_add_save -> { // menu_add_save라는 id의 메뉴 버튼이 눌리면
            val intent = intent // 인텐트 생성
            intent.putExtra("result", binding.addEditView.text.toString())  // 인텐트 엑스트라 정보 추가
            setResult(Activity.RESULT_OK, intent)   // 생성한 인텐트를 ActivityResultLauncher로 전달
            finish()    // 현재 화면의 액티비티를 종료 및 뒤로가기
            true
        }
        else -> true
    }
    
}