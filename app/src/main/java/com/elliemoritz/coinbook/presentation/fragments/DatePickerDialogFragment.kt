package com.elliemoritz.coinbook.presentation.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.Calendar

class DatePickerDialogFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        onDateChangedListener.onDateChanged(calendar)
    }

    interface OnDateChangedListener {
        fun onDateChanged(calendar: Calendar)
    }

    companion object {
        const val TAG = "date_picker"
        private lateinit var onDateChangedListener: OnDateChangedListener

        fun newInstance(listener: OnDateChangedListener): DatePickerDialogFragment {
            onDateChangedListener = listener
            return DatePickerDialogFragment()
        }
    }
}