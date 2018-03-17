package com.siimk.todolist_oopproject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;


public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "DatePickerFragment";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {

        Log.d(TAG, "----------------------------- onDateSet: Well... siiamaani ta jõudis!");
        Log.d(TAG, "onDateSet: \n" + year + " " + month + " " + day);

        //TODO - do sth with the date chosen by the user.

        String taskYear = Integer.toString(year);
        String taskMonth = Integer.toString(month);
        String taskDay = Integer.toString(day);

        String date = taskDay + " / " + taskMonth + " / " + taskYear;

        if(getActivity().getClass() == AddItemActivity.class){
            ((AddItemActivity) getActivity()).setDatePicked(date);
        }
        if(getActivity().getClass() == ViewTaskActivity.class){
            ((ViewTaskActivity) getActivity()).setDatePicked(date);
        }
    }
}
































