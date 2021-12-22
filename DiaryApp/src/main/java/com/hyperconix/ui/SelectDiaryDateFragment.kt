package com.hyperconix.ui

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

import com.hyperconix.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * This class defines the Select Diary Date fragment,
 * this fragment will be responsible for the first tab
 * in the application. This will be where the user selects
 * a date for the diary entry they wish to make.
 *
 * @author Luke S
 */
class SelectDiaryDateFragment : Fragment(R.layout.select_diary_date_fragment) {

    /**
     * This represents a simple date format, mapped to short and the UK locale, used for formatting dates.
     */
    private val simpleDateFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, Locale.UK)

    /**
     * This represents the calendar from Java util which is used to get and set dates
     */
    private lateinit var calendar: Calendar

    /**
     * This represents the CalendarView component which is displayed to the user
     * so they can select a date
     */
    private lateinit var calendarViewDatePicker: CalendarView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendarViewDatePicker = view.findViewById(R.id.calendarViewDatePicker)

        val buttonToday = view.findViewById<Button>(R.id.buttonToday)

        val buttonTomorrow = view.findViewById<Button>(R.id.buttonTomorrow)

        calendar = Calendar.getInstance()

        val todaysDate = simpleDateFormat.format(calendar.timeInMillis)

        //In the case where the user does not select a date before creating new entry, set result to todays date
        parentFragmentManager.setFragmentResult("requestKey", bundleOf("calendarDate" to todaysDate))

        calendarViewDatePicker.setOnDateChangeListener() { _, year, month, day ->
            //Set the calendar to the selected date's year, month and day
            calendar.set(year, month, day)

            val selectedDate = simpleDateFormat.format(calendar.timeInMillis)

            //Using Fragment Result API, bundle the formatted date that has been selected and set it as the fragment result
            parentFragmentManager.setFragmentResult("requestKey", bundleOf("calendarDate" to selectedDate))
        }

        buttonToday.setOnClickListener() {
            setDateAsToday()
        }

        buttonTomorrow.setOnClickListener() {
            setDateAsTomorrow()
        }

    }

    /**
     * This function will set the selected date
     * to today's date and update in the calendar
     * view. This is called whenever the
     * "Today Button" is clicked.
     *
     */
    private fun setDateAsToday() {
        calendar = Calendar.getInstance()

        calendarViewDatePicker.setDate(calendar.timeInMillis, true, true)

        val date = simpleDateFormat.format(calendar.timeInMillis)

        parentFragmentManager.setFragmentResult("requestKey", bundleOf("calendarDate" to date))
    }

    /**
     * This function will set the selected date
     * to tomorrow's date and update in the calendar
     * view. This is called whenever the
     * "Tomorrow Button" is clicked.
     *
     */
    private fun setDateAsTomorrow() {
        calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)

        val month = calendar.get(Calendar.MONTH)

        val day = calendar.get(Calendar.DAY_OF_MONTH)

        calendar.set(year, month, day + 1)

        calendarViewDatePicker.setDate(calendar.timeInMillis, true, true)

        val date = simpleDateFormat.format(calendar.timeInMillis)

        parentFragmentManager.setFragmentResult("requestKey", bundleOf("calendarDate" to date))

    }

}