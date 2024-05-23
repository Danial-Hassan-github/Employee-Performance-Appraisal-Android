package com.example.biitemployeeperformanceappraisalsystem.helper;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class DateTime {
    public void showDateTimePicker(final EditText editText, Context context) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // Format date
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String formattedDate = dateFormat.format(calendar.getTime());

                        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                                new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                        calendar.set(Calendar.MINUTE, minute);

                                        // Format time
                                        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm", Locale.getDefault());
                                        String formattedTime = timeFormat.format(calendar.getTime());

                                        editText.setText(formattedDate + " " + formattedTime);
                                    }
                                }, hour, minute, false); // Set false to use 12-hour format
                        timePickerDialog.show();
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    public static String formatLocalDateTimeToString(LocalDateTime dateTime) {
        // Define the desired format pattern
        String pattern = "dd/MM/yyyy hh:mm";

        // Create a formatter with the specified pattern
        DateTimeFormatter formatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern(pattern);
        }

        // Format the LocalDateTime object using the formatter
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return dateTime.format(formatter);
        }
        return null;
    }

    // Parse String to LocalDateTime
    public static LocalDateTime parseStringToLocalDateTime(String dateTimeString) {
        // Define the format pattern for the input string
        String pattern = "dd/MM/yyyy hh:mm";

        // Create a formatter with the specified pattern
        DateTimeFormatter formatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern(pattern);
        }

        // Parse the string into a LocalDateTime object using the formatter
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return LocalDateTime.parse(dateTimeString, formatter);
        }
        return null;
    }
}
