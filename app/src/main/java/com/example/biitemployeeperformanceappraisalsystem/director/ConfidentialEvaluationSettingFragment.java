package com.example.biitemployeeperformanceappraisalsystem.director;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.helper.DateTime;
import com.example.biitemployeeperformanceappraisalsystem.helper.SharedPreferencesManager;
import com.example.biitemployeeperformanceappraisalsystem.models.EvaluationPin;
import com.example.biitemployeeperformanceappraisalsystem.models.EvaluationTime;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EvaluationPinService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EvaluationTimeService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ConfidentialEvaluationSettingFragment extends Fragment {
    SharedPreferencesManager sharedPreferencesManager;
    EvaluationTimeService evaluationTimeService;
    EditText pinEditText, startTimeEditText, endTimeEditText;
    Button saveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confidential_evaluation_setting, container, false);

        pinEditText = view.findViewById(R.id.pin);
        startTimeEditText = view.findViewById(R.id.start_time);
        endTimeEditText = view.findViewById(R.id.end_time);
        saveButton = view.findViewById(R.id.save_button);

        sharedPreferencesManager = new SharedPreferencesManager(getContext());
        evaluationTimeService = new EvaluationTimeService(getContext());

        startTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTime dateTime=new DateTime();
                dateTime.showDateTimePicker(startTimeEditText,getContext());
            }
        });

        endTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTime dateTime=new DateTime();
                dateTime.showDateTimePicker(endTimeEditText,getContext());
            }
        });

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String startTimeStr = startTimeEditText.getText().toString();
                    String endTimeStr = endTimeEditText.getText().toString();

                    EvaluationTime evaluationTime = new EvaluationTime();

                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                        Date parsedStartDate = dateFormat.parse(startTimeStr);
                        Date parsedEndDate = dateFormat.parse(endTimeStr);
                        evaluationTime.setStart_time(parsedStartDate);
                        evaluationTime.setEnd_time(parsedEndDate);
                        evaluationTime.setEvaluation_type("confidential");
                        evaluationTime.setSession_id(sharedPreferencesManager.getSessionId());
                        // TODO: Add Pin
                        EvaluationPinService evaluationPinService = new EvaluationPinService(getContext());
                        EvaluationPin evaluationPin = new EvaluationPin();
                        evaluationPin.setPin(Integer.parseInt(pinEditText.getText().toString()));
                        evaluationPin.setSession_id(sharedPreferencesManager.getSessionId());
                        evaluationPinService.postConfidentialEvaluationPin(
                                evaluationPin,
                                evaluationPin1 -> {
                                    Toast.makeText(getContext(), "Pin Added Successfully", Toast.LENGTH_SHORT).show();
                                },
                                errorMessage -> {
                                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                                }
                        );
                        evaluationTimeService.postEvaluationTime(
                                evaluationTime,
                                evaluationTime1 -> {
                                    Toast.makeText(getContext(), "Confidential time added successfully", Toast.LENGTH_SHORT).show();
                                },
                                errorMessage -> {
                                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                                }
                        );
                    } catch (ParseException e) {
                        Toast.makeText(getContext(), "Invalid date/time format", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        return view;
    }
}
