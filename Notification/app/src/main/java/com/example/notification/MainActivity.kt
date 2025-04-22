package com.example.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.RemoteInput
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notification.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)   // activity_main.xml을 바인딩 객체로 변환
        setContentView(binding.root)    // 화면에 activity_main.xml 바인딩 객체를 출력

        val permissionLauncher = registerForActivityResult( // 권한 요청
            ActivityResultContracts.RequestMultiplePermissions()    // 여러 권한을 동시 요청(알림, 카메라, 음성 등등)
        ) {
            // 요청한 모든 권한에 대해 람다로 실행
            if (it.all {permition -> permition.value == true}) {    // 모든 권한이 허용된 경우
                noti()  // 알림 함수 실행
            } else {    // 권한 중 하나라도 허용하지 않으면
                Toast.makeText(this, "permission denied...", Toast.LENGTH_SHORT).show() // 토스트 출력
            }
        }

        binding.notificationButton.setOnClickListener { // 바인딩 객체 중 notificationButton에 이벤트 출력
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {    // 현재 안드로이드 버전이 13 버전 이상이면
                if (ContextCompat.checkSelfPermission(  // 권한 허용됬는지 체크
                        this,   // 현재 실행 중인 액티비티
                        "android.permission.POST_NOTIFICATIONS")    // 알림 권한
                    == PackageManager.PERMISSION_GRANTED) { // 이미 권한이 허용되었다면
                    noti()  // 알림 함수 실행
                } else {    // 권한이 허용되지 않았다면
                    permissionLauncher.launch(  // 권한 허용 위젯 실행
                        arrayOf(    // 배열로 권한 허용 실행 목록을 전달
                            "android.permission.POST_NOTIFICATIONS"
                        )
                    )
                }
            } else {    // 연재 안드로이드 버전이 13 버전 미만이면
                noti()  // 어떤 권한 요청도 하지 않고 알림 함수 실행
            }
        }
    }

    fun noti() {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager // 안드로이드 시스템 기능 중 알림 서비스 기능 가져오기

        val builder: NotificationCompat.Builder // NotificationCompat.Builder 객체 선언
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {   // 현재 안드로이드 버전이 8 버전 이상이면
            val channelId = "one-channel"       // 고유한 채널 식별자
            val channelName = "My Channel One"  // 중복 가능한 채널 이름
            val channel = NotificationChannel(  // 채널 생성
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT  // 알림 중요도
            ).apply {   // 생성한 채널의 후설정
                description = "My Channel One Description"
                setShowBadge(true)  // 알림이 오면 앱 아이콘에 뱃지 출력
                val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION) // 기본 알림음 파일 경로
                val audioAttributes = AudioAttributes.Builder() // 오디오 정보 생성
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)  // 소리의 컨텐츠 타입 설정
                    .setUsage(AudioAttributes.USAGE_ALARM)  // 다른 소리와 겹칠 때 조율하는 용도
                    .build()
                setSound(uri, audioAttributes)  // 소리 설정
                enableVibration(true)   // 진동 설정
            }
            manager.createNotificationChannel(channel)  // NotificationManager에 채널 생성 및 등록
            builder = NotificationCompat.Builder(this, channelId)   // 빌더 반환
        } else {    // 현재 안드로이드 버전이 8 버전 미만이면
            builder = NotificationCompat.Builder(this)  // 채널을 사용하지 않으므로 그냥 알림 빌더 반환
        }

        builder.run {   // 알림 실행
            setSmallIcon(R.drawable.small)  // 작은 아이콘 설정
            setWhen(System.currentTimeMillis()) // 알림 발생 시각(현재 시각으로 설정)
            setContentTitle("신창섭")  // 제목
            setContentText("안녕하세요") // 내용
            setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.big))    // 큰 이미지
        }

        val KEY_TEXT_REPLY = "key_text_reply"   // 답장을 보낼 때 설정할 식별자()
        var replyLabel = "바보"   // 알림 답장을 보낼 때 placeholder에 출력할 텍스트
        var remoteInput: androidx.core.app.RemoteInput = androidx.core.app.RemoteInput.Builder(KEY_TEXT_REPLY).run {    // 원격 입력 객체 생성
            setLabel(replyLabel)
            build() // 객체를 생성해서 반환
        }
        val replyIntent = Intent(this, ReplyReceiver::class.java)   // ReplayReceiver와 연결 객체
        // replyIntent 발동기 생성
        val replyPendingIntent = PendingIntent.getBroadcast(this, 30 ,replyIntent, PendingIntent.FLAG_MUTABLE)

        // 원격 입력 액션 등록
        builder.addAction(
            NotificationCompat.Action.Builder(
                R.drawable.send,
                "답장",
                replyPendingIntent
            ).addRemoteInput(remoteInput).build()
        )

        manager.notify(11, builder.build()) // 알림 매니저에 알림 발생
    }
}