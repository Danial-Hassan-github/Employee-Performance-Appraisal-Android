package com.example.biitemployeeperformanceappraisalsystem.director;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.helper.DateTime;
import com.example.biitemployeeperformanceappraisalsystem.helper.SharedPreferencesManager;
import com.example.biitemployeeperformanceappraisalsystem.models.EvaluationTime;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EvaluationService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EvaluationTimeService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PeerEvaluationSettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PeerEvaluationSettingFragment extends Fragment {
    SharedPreferencesManager sharedPreferencesManager;
    EvaluationTimeService evaluationTimeService;
    EditText startTimeEditText, endTimeEditText;
    Button saveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_peer_evaluation_setting, container, false);

        startTimeEditText = view.findViewById(R.id.start_time);
        endTimeEditText = view.findViewById(R.id.end_time);
        saveButton = view.findViewById(R.id.save_button);

        evaluationTimeService = new EvaluationTimeService(getContext());
        sharedPreferencesManager = new SharedPreferencesManager(getContext());

        DateTime dateTime=new DateTime();

        startTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateTime.showDateTimePicker(startTimeEditText,getContext());
            }
        });

        endTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        evaluationTime.setEvaluation_type("peer");
                        evaluationTime.setSession_id(sharedPreferencesManager.getSessionId());

                        evaluationTimeService.postEvaluationTime(
                                evaluationTime,
                                evaluationTime1 -> {
                                    Toast.makeText(getContext(), "Peer Evaluation time Added Successfully", Toast.LENGTH_SHORT).show();
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

        // Inflate the layout for this fragment
        return view;
    }
}