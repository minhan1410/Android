package com.minhan.todolist

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Handler
import android.os.Vibrator
import android.widget.Toast
import androidx.annotation.RequiresApi

class AlarmReceiver : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    override fun onReceive(context: Context, intent: Intent) {
        //we will use vibrator first
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(1000)


        for(i in 1..10 ){
            Toast.makeText(context, "Đã hết giờ làm việc của ngày ${CalendarUtils.selectedDate?.let {CalendarUtils.formattedDate(it)}}", Toast.LENGTH_LONG).show()
        }

        var alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }
        val ringtone = RingtoneManager.getRingtone(context, alarmUri)
        ringtone.play()
    }
}