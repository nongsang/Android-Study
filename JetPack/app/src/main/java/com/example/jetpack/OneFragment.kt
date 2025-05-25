package com.example.jetpack

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jetpack.databinding.FragmentOneBinding
import com.example.jetpack.databinding.ItemRecyclerviewBinding
import com.example.jetpack.R

// 1. 각 아이템에 뷰 객체(Image, Text 등)를 가진다는 의미
class MyViewHolder(val binding: ItemRecyclerviewBinding): RecyclerView.ViewHolder(binding.root)

// 2. 뷰 홀더에 있는 뷰 객체에 적절한 데이터를 대입한다.
class MyAdapter(val datas: MutableList<String>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    // 생성할 아이템의 갯수
    override fun getItemCount(): Int {
        return datas.size
    }

    // 아이템이 생성되면 뷰 홀더를 반환한다.
    // getItemCount() 갯수만큼 호출된다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context),
            parent, false))
    }

    // 아이템에 있는 뷰 홀더에 어떤 리소스를 할당할지 정의한다.
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder).binding
        binding.itemData.text = datas[position]
    }
}

// 3. 리사이클러 뷰 꾸미기
// 꾸미고 싶지 않으면 없어도 된다.
class MyDecoration(val context: Context): RecyclerView.ItemDecoration(){

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val width = parent.width
        val height = parent.height

        val dr = ResourcesCompat.getDrawable(context.resources, R.drawable.kbo, null)
        val drWidth = dr?.intrinsicWidth
        val drHeight = dr?.intrinsicHeight

        val left = width/2 - drWidth?.div(2) as Int
        val top = height/2 - drHeight?.div(2) as Int

        c.drawBitmap(
            BitmapFactory.decodeResource(context.resources, R.drawable.kbo),
            left.toFloat(),
            top.toFloat(),
            null
        )
    }

    // 아이템을 꾸미는 용도
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val index = parent.getChildAdapterPosition(view)+1

        if(index % 3 == 0)
            outRect.set(10, 10, 10, 60)
        else
            outRect.set(10, 10, 10, 0)

        view.setBackgroundColor(Color.parseColor("#28A0FF"))
        ViewCompat.setElevation(view, 20.0f)
    }
}

class OneFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 바인딩 레이아웃 찾기
        val binding =  FragmentOneBinding.inflate(inflater, container, false)

        // 데이터 생성
        val datas = mutableListOf<String>()
        for(i in 1..9){
            datas.add("Item $i")
        }

        // 4. 바인딩 레이아웃에 있는 리사이클러 뷰에 레이아웃 매니저를 생성하여 등록
        // 이 과정이 있어야 화면에 아이템을 생성 및 출력한다.
        val layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.layoutManager = layoutManager

        // 바인딩 레이아웃에 있는 리사이클러 뷰에 생성한 데이터만큼 어댑터를 생성하고 등록
        val adapter = MyAdapter(datas)
        binding.recyclerView.adapter = adapter

        // 데코레이션에 정의된 내용으로 리사이클러 뷰 꾸미기
        binding.recyclerView.addItemDecoration(MyDecoration(activity as Context))

        return binding.root
    }

}