package com.example.jetpack

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.jetpack.OneFragment
import com.example.jetpack.databinding.ActivityMainBinding
import com.example.jetpack.R
import com.example.jetpack.ThreeFragment
import com.example.jetpack.TwoFragment

class MainActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle

    // 뷰 페이저, 화면을 스와이프해서 페이지를 넘길 수 있다.
    // 리사이클러 뷰로 구현할 수 있지만, 각 화면은 프래그먼트이므로, FragmentStateAdapter를 상속받아 구현한다.
    class MyFragmentPagerAdapter(activity: FragmentActivity): FragmentStateAdapter(activity){
        // 모든 프래그먼트 객체 생성
        val fragments: List<Fragment>
        init {
            fragments = listOf(OneFragment(), TwoFragment(), ThreeFragment())
        }

        // 프래그먼트 갯수 반환
        override fun getItemCount(): Int {
            return fragments.size
        }

        // 프래그먼트 뷰 생성
        // getItemCount() 갯수 만큼 호출
        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }

    // 메인 액티비티를 생성할 때 호출
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        // 드로우어 토글이 붙은 액션 바 생성
        toggle = ActionBarDrawerToggle(this, binding.main, R.string.drawer_opened,
            R.string.drawer_closed)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

        // 바인딩 레이아웃에 있는 뷰 페이저에 어댑터 적용
        val adapter = MyFragmentPagerAdapter(this)
        binding.viewpager.adapter = adapter
    }

    //
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)

        val menuItem = menu?.findItem(R.id.menu_search)
        val searchView = menuItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("kkang", "search text: $query")
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}