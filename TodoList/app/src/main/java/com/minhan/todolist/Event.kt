package com.minhan.todolist

import android.os.Parcelable
import java.time.LocalDate
import java.time.LocalTime
import kotlinx.parcelize.Parcelize

@Parcelize
class Event(var name: String,var content: String,var done:Boolean, var date: LocalDate, var time: LocalTime) :
    Parcelable {

    companion object {
        var eventsList: ArrayList<Event> = ArrayList<Event>();

        fun eventsForDate(date: LocalDate?): ArrayList<Event> {
            val events: ArrayList<Event> =
                ArrayList<Event>()
            for (event in eventsList) {
                if (event.date == date) events.add(event)
            }
            return events
        }
    }

    override fun toString(): String {
        return "Event(name='$name', content='$content', done=$done, date=$date, time=$time)"
    }

}