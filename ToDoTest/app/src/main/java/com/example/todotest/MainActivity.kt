package com.example.todotest

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todotest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var datas: MutableList<String>? = null
    lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 액티비티의 결과를 인텐트로 받아 실행하는 실행기
        val requestLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            // 만약 받은 인텐트의 key에 "result"가 있다면
            it.data!!.getStringExtra("result")?.let {
                datas?.add(it)  // 리스트에 추가
                adapter.notifyDataSetChanged()  // 어댑터에 데이터가 바뀌었다고 알림
            }
        }

        // 할일 탭 버튼을 누르면
        binding.mainFab.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)  // AddActivity 실행 인텐트를 생성
            requestLauncher.launch(intent)  // 시스템으로 생성한 인텐트를 전달하며 실행
        }

        // 저장해뒀던 번들 데이터를 가져와 저장한다.
        datas = savedInstanceState?.let {
            it.getStringArrayList("datas")?.toMutableList() // 만약 "datas"라는 key의 데이터가 있으면 반환
        } ?: let {
            mutableListOf() // 없으면 빈 리스트 반환
        }
        

        val layoutManager = LinearLayoutManager(this)
        // 리사이클러뷰의 레이아웃 매니저에 생성한 레이아웃 매니저를 등록
        binding.mainRecyclerView.layoutManager=layoutManager
        // 데이터를 가지고 어댑터를 생성
        adapter=MyAdapter(datas)
        // 어댑터 등록
        binding.mainRecyclerView.adapter=adapter
        binding.mainRecyclerView.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )
    }

    // 액티비티가 종료되고 다시 시작할 때 정보를 복원할 데이터를 저장할 때 호출
    override fun onSaveInstanceState(outState: Bundle) {    // 번들이라는 이름의 데이터로 저장한다.
        super.onSaveInstanceState(outState)
        outState.putStringArrayList("datas", ArrayList(datas))
    }
}