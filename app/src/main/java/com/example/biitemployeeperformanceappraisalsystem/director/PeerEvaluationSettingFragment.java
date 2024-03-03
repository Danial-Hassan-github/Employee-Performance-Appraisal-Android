package com.example.biitemployeeperformanceappraisalsystem.director;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.helper.DateTime;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PeerEvaluationSettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PeerEvaluationSettingFragment extends Fragment {

    EditText startTimeEditText, endTimeEditText;
    Button saveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_peer_evaluation_setting, container, false);

        startTimeEditText = view.findViewById(R.id.start_time);
        endTimeEditText = view.findViewById(R.id.end_time);
        saveButton = view.findViewById(R.id.save_button);

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

        // Inflate the layout for this fragment
        return view;
    }
}