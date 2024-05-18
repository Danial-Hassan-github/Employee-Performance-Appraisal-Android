package com.example.biitemployeeperformanceappraisalsystem.director;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.models.OptionWeightage;
import com.example.biitemployeeperformanceappraisalsystem.network.services.OptionWeightageService;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OptionsWeightageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OptionsWeightageFragment extends Fragment {
    OptionWeightageService optionWeightageService;
    List<OptionWeightage> optionWeightageList;
    List<EditText> editTextList = new ArrayList<>();
    Button btnSave;
    LinearLayout layoutView;
    public OptionsWeightageFragment() {
        // Required empty public constructor
    }

    public static OptionsWeightageFragment newInstance(String param1, String param2) {
        OptionsWeightageFragment fragment = new OptionsWeightageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_options_weightage, container, false);
        layoutView = view.findViewById(R.id.options_weightage_adjustment_form);
        btnSave = view.findViewById(R.id.btn_save);

        optionWeightageService = new OptionWeightageService(getContext());
        optionWeightageService.getOptionsWeightage(
                optionsWeightages -> {
                    optionWeightageList = optionsWeightages;
                    // Dynamically create and add
                    for (OptionWeightage option : optionWeightageList) {
                        TextView titleTextView = new TextView(requireContext());
                        titleTextView.setText(option.getName());

                        EditText valueEditText = new EditText(requireContext());
                        valueEditText.setText(option.getWeightage()+"");

                        // Add input fields to the layout
                        layoutView.addView(titleTextView);
                        layoutView.addView(valueEditText);

                        editTextList.add(valueEditText);
                    }
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT);
                }
        );

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < optionWeightageList.size(); i++) {
                    OptionWeightage option = optionWeightageList.get(i);
                    EditText editText = editTextList.get(i);
                    option.setWeightage(Integer.parseInt(editText.getText().toString())); // Update weightage
                }
                optionWeightageService.putOptionsWeightage(
                        optionWeightageList,
                        optionWeightageList1 -> {
                            optionWeightageList = optionWeightageList1;
                        },
                        errorMessage -> {
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }
                );
            }
        });

        return view;
    }
}