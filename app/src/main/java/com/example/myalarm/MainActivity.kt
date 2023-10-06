package com.example.myalarm

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkAndGetNotificationPermission()

        findViewById<Button>(R.id.button_set_alarm).setOnClickListener {
            Log.i("TAG","click set alarm")
            setAlarm(1234)
        }

        findViewById<Button>(R.id.button_cancel_alarm).setOnClickListener {
            Log.i("TAG","click cancel")
            cancelAlarm(1234)
        }

    }

    private val REQUEST_PERMISSION = 1000000
    private fun checkAndGetNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.POST_NOTIFICATIONS
            )
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQUEST_PERMISSION)
            }
        }
    }

    private fun getAlarmPendingIntent(id: Long): PendingIntent {
        Log.i(TAG,">>> getAlarmPendingIntent id = $id")
        val intent = Intent(applicationContext, MyBroadcastReceiver::class.java)
        intent.putExtra("int", 1111)
        return PendingIntent.getBroadcast(application, id.toInt(), intent, PendingIntent.FLAG_IMMUTABLE)  //will override if same id
    }

    private fun setAlarm(id: Long) {
        Log.i(TAG,">>> setAlarm id = $id")

        // creating alarmManager instance
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // adding intent and pending intent to go to AlarmReceiver Class in future
        val alarmPendingIntent = getAlarmPendingIntent(id)
        val date = Date()
        // creating clockInfo instance
        val clockInfo = AlarmManager.AlarmClockInfo(date.time+5000, alarmPendingIntent)
        // setting the alarm
        alarmManager.setAlarmClock(clockInfo, alarmPendingIntent)
    }

    private fun cancelAlarm(id: Long) {
        Log.i(TAG,">>> cancelAlarm id = $id")

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmPendingIntent = getAlarmPendingIntent(id)
        alarmManager.cancel(alarmPendingIntent)
    }
}