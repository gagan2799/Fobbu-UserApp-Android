package com.fobbu.member.android.utils

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.widget.DatePicker

import java.text.SimpleDateFormat
import java.util.*


@RequiresApi(Build.VERSION_CODES.N)
class CustomDatePicker(
    context: Context, theme: Int, callBack: DatePickerDialog.OnDateSetListener,
    year: Int, monthOfYear: Int, dayOfMonth: Int
): DatePickerDialog(context), DatePicker.OnDateChangedListener
{
    private var mDatePicker: DatePickerDialog? = null

    init
    {
        mDatePicker = DatePickerDialog(context, theme, callBack, year, monthOfYear, dayOfMonth)

        mDatePicker!!.datePicker.init(2013, 7, 16, this)

        updateTitle(year)
    }

    override fun onDateChanged(
        view: DatePicker, year: Int,
        month: Int, day: Int
    ) {
        updateTitle(year)
    }

    private fun updateTitle(year: Int) {
        val mCalendar = Calendar.getInstance()
        mCalendar.set(Calendar.YEAR, year)

        //       mCalendar.set(Calendar.DAY_OF_MONTH, day);
        mDatePicker!!.setTitle(getFormat().format(mCalendar.time))
    }

    fun getPicker(): DatePickerDialog? {

        return this.mDatePicker
    }

    fun getFormat(): SimpleDateFormat {
        return SimpleDateFormat("yyyy")
    };
}