package com.nuclan2024.myapplication

import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest

class ScheduleNotificationActivity : AppCompatActivity() {
    private lateinit var btnSelectTime: Button
    private lateinit var etNotificationText: EditText
    private lateinit var btnSave: Button

    private var selectedHour = 20
    private var selectedMinute = 0

    companion object {
        private const val REQUEST_CODE_NOTIFICATION_PERMISSION = 123
    }

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_notification)

        btnSelectTime = findViewById(R.id.btn_select_time)
        etNotificationText = findViewById(R.id.et_notification_text)
        btnSave = findViewById(R.id.btn_save)


        btnSelectTime.setOnClickListener {
            showTimePickerDialog()
        }

        btnSave.setOnClickListener {
            val notificationText = etNotificationText.text.toString()
            if (notificationText.isNotEmpty()) {
                checkAndRequestNotificationPermission()
            } else {
                Toast.makeText(this, "Введите текст уведомления", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                selectedHour = hourOfDay
                selectedMinute = minute
                btnSelectTime.text = String.format("%02d:%02d", hourOfDay, minute)
            },
            currentHour,
            currentMinute,
            true
        )
        timePickerDialog.show()
    }

    private fun scheduleNotification() {
        val notificationText = etNotificationText.text.toString()
        NotificationScheduler.rescheduleNotification(this, selectedHour, selectedMinute, notificationText)
        Toast.makeText(this, "Уведомление запланировано", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQUEST_CODE_NOTIFICATION_PERMISSION)
            } else {
                scheduleNotification()
            }
        } else {
            scheduleNotification()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_NOTIFICATION_PERMISSION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                scheduleNotification()
            } else {
                Toast.makeText(this, "Разрешение на отправку уведомлений не предоставлено", Toast.LENGTH_SHORT).show()
            }
        }
    }

}