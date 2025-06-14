package com.example.batteryinfo

import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.batteryinfo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val permissionLauncher = registerForActivityResult( // 권한 요청
            ActivityResultContracts.RequestMultiplePermissions()    // 여러 권한을 동시 요청
        ){
            if(it.all { permission -> permission.value == true}){   // 모든 권한이 허용되면
                val intent = Intent(this, MyReceiver::class.java)   // MyReceiver 컴포넌트 실행 인텐트 생성
                sendBroadcast(intent)   // 인텐트 정보로 브로드캐스트 실행
            }else { // 권한이 허용되지 않았다면
                Toast.makeText(this, "permission denied...", Toast.LENGTH_SHORT).show() // 토스트 메세지 출력
            }
        }

        registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))!!.apply {   // 리시버에 등록
            when(getIntExtra(BatteryManager.EXTRA_STATUS, -1)){
                BatteryManager.BATTERY_STATUS_CHARGING -> {
                    when(getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)){
                        BatteryManager.BATTERY_PLUGGED_USB -> {
                            binding.chargingResultView.text = "USB Plugged"
                            binding.chargingImageView.setImageBitmap(BitmapFactory.decodeResource(
                                resources, R.drawable.usb
                            ))
                        }
                        BatteryManager.BATTERY_PLUGGED_AC -> {
                            binding.chargingResultView.text = "AC Plugged"
                            binding.chargingImageView.setImageBitmap(BitmapFactory.decodeResource(
                                resources, R.drawable.ac
                            ))
                        }
                    }
                }else -> {
                    binding.chargingResultView.text = "No Plugged"
                }
            }

            val level = getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val batteryPct = level / scale.toFloat() * 100

            binding.percentResultView.text = "$batteryPct %"
        }

        binding.button.setOnClickListener { // 바인에 있는 버튼을 클릭하면 실행할 이벤트
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){  // 안드로이드 버전 13 이상이면
                if(ContextCompat.checkSelfPermission(   // 권한 체크
                    this,
                    "android.permission.POST_NOTIFICATIONS"
                ) == PackageManager.PERMISSION_GRANTED){    // 권한이 허용 되어 있으면
                    val intent = Intent(this, MyReceiver::class.java)   // 리시버 컴포넌트 실행 인텐트 생성
                    sendBroadcast(intent)   // 생성한 인텐트로 브로드캐스트 실행
                }else { // 권한이 허용되어 있지 않으면
                    permissionLauncher.launch(  // 권한 허용 실행
                        arrayOf("android.permission.POST_NOTIFICATIONS")
                    )
                }
            }else { // 안드로이드 버전 13 미만이면
                val intent = Intent(this, MyReceiver::class.java)
                sendBroadcast(intent)
            }
        }

    }
}