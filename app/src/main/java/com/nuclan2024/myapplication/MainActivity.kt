package com.nuclan2024.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val scheduleNotification = findViewById<Button>(R.id.schedule_notification)
        scheduleNotification.setOnClickListener {
            val intent = Intent(this, ScheduleNotificationActivity::class.java)
            startActivity(intent)
        }
    }
}