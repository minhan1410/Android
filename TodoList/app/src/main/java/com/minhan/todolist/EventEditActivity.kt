package com.minhan.todolist

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalTime
import java.util.*


class EventEditActivity : AppCompatActivity() {
    private var eventNameET: EditText? = null
    private var eventContentET: EditText? = null
    private var eventDateTV: TextView? = null
    private var eventTimeTV: TextView? = null
    private var timeButton: Button? = null
    private var saveEventAction: Button? = null
    private var hour = 0
    private var minute = 0

    @RequiresApi(Build.VERSION_CODES.O)
    private var time: LocalTime = LocalTime.now()


    private fun initWidgets() {
        eventNameET = findViewById(R.id.eventNameET)
        eventContentET = findViewById(R.id.eventContentET)
        eventDateTV = findViewById(R.id.eventDateTV)
        eventTimeTV = findViewById(R.id.eventTimeTV)
        timeButton = findViewById(R.id.timeButton)
        saveEventAction = findViewById(R.id.saveEventAction)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_edit)
        initWidgets()

        val intent = intent
        var event = intent.getParcelableExtra<Event>("Event")
        println("\nevent.name: ${event?.name}\n")
        if (event != null) {
            eventNameET?.setText(event.name)
            eventContentET?.setText(event.content)
            eventDateTV?.text = "Date: " + event.date.toString()
            eventTimeTV?.text = "Time: " + event.time.toString()
            saveEventAction!!.text = "Update"
        } else {
            eventDateTV!!.text =
                "Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate!!)
            eventTimeTV!!.text = "Time: " + CalendarUtils.formattedTime(time)
        }

        timeButton?.setOnClickListener {
            val onTimeSetListener = OnTimeSetListener { _, selectedHour, selectedMinute ->
                hour = selectedHour
                minute = selectedMinute
                timeButton?.text = String.format(Locale.getDefault(), "%02d:%02d", hour, minute)

//                alarm()
            }

            // int style = AlertDialog.THEME_HOLO_DARK;
            val timePickerDialog =
                TimePickerDialog(this,  /*style,*/onTimeSetListener, hour, minute, true)
            timePickerDialog.setTitle("Select Time")
            timePickerDialog.show()
        }

        saveEventAction?.setOnClickListener {
            val eventName = eventNameET!!.text.toString()
            val eventContent = eventContentET!!.text.toString()
            val eventDone = false

            if (event == null) {
                val newEvent =
                    Event(eventName, eventContent, eventDone, CalendarUtils.selectedDate!!, time)
                Event.eventsList.add(newEvent)
                finish()
            } else {
                var pos = intent.getIntExtra("Position", -1)

                for (e in Event.eventsForDate(event.date)) {
                    if (e.time == event.time) {
                        e.name = eventName
                        e.content = eventContent
                        e.done = eventDone
                    }
                }

                println(
                    "\npos: ${pos}\nEvent.eventsForDate(${event.date}):${
                        Event.eventsForDate(
                            event.date
                        )
                    }\n"
                )
                finish()
            }
            alarm()
        }
    }

    private fun alarm() {
        var time: Long
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = hour
        calendar[Calendar.MINUTE] = minute
        val intentAlarm = Intent(this, AlarmReceiver::class.java)
        var pendingIntent: PendingIntent = PendingIntent.getBroadcast(this, 0, intentAlarm, 0)
        var alarmManager: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        time = calendar.timeInMillis - calendar.timeInMillis % 60000
        if (System.currentTimeMillis() > time) {
            time =
                if (Calendar.AM_PM == 0) time + 1000 * 60 * 60 * 12 else time + 1000 * 60 * 60 * 24
        }
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent)
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, 10000, pendingIntent)
    }

}
