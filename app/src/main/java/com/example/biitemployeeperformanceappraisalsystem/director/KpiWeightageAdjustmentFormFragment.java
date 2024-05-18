package com.example.biitemployeeperformanceappraisalsystem.director;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.biitemployeeperformanceappraisalsystem.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KpiWeightageAdjustmentFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KpiWeightageAdjustmentFormFragment extends Fragment {
    ArrayList<Float> pieChartValues = new ArrayList<>();
    ArrayList<String> pieChartTitles;

    public KpiWeightageAdjustmentFormFragment(ArrayList<Float> pieChartValues, ArrayList<String> pieChartTitles){
        this.pieChartValues = pieChartValues;
        this.pieChartTitles = pieChartTitles;
    }

    public KpiWeightageAdjustmentFormFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kpi_weightage_adjustment_form, container, false);

        // Retrieve pie chart values and titles from the arguments bundle
        Bundle args = getArguments();
        //float[] pieChartValues = args.getFloatArray("pieChartValues");
        //ArrayList<String> pieChartTitles = args.getStringArrayList("pieChartTitles");

        // Get the layout where you want to add the dynamic input fields
        LinearLayout layout = view.findViewById(R.id.kpi_weightage_adjustment_form_layout);

        // Create and add input fields dynamically based on pie chart values and titles
        EditText[] editTexts = new EditText[pieChartValues.size()];
        float totalSum = 0;

        for (int i = 0; i < pieChartValues.size(); i++) {
            TextView titleTextView = new TextView(requireContext());
            titleTextView.setText(pieChartTitles.get(i));

            EditText valueEditText = new EditText(requireContext());
            valueEditText.setText(Float.toString(pieChartValues.get(i)));

            // Add input fields to the layout
            layout.addView(titleTextView);
            layout.addView(valueEditText);

            // Keep track of EditTexts
            editTexts[i] = valueEditText;

            // Set OnChangeListener for the EditText
            valueEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // Not needed
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // Not needed
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // Calculate the total sum of values
                    float newSum = calculateTotalSum(editTexts);

                    if (newSum > 100) {
                        // Calculate the adjustment needed
                        float adjustment = newSum - 100;

                        // Distribute the adjustment proportionally across all EditTexts
                        distributeAdjustment(editTexts, adjustment, valueEditText);
                    }
                }
            });

            // Update the total sum
            totalSum += pieChartValues.get(i);
        }

        // Implement functionality to save modified values when save button is clicked
        Button saveButton = view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the modified values
                // You can retrieve the modified values from the input fields here
                // and implement the saving logic accordingly
            }
        });

        return view;
    }

    // Helper method to calculate the total sum of values in EditTexts
    private float calculateTotalSum(EditText[] editTexts) {
        float sum = 0;

        for (EditText editText : editTexts) {
            String text = editText.getText().toString();
            if (!text.isEmpty()) {
                sum += Float.parseFloat(text);
            }
        }

        return sum;
    }

    // Helper method to distribute adjustment across EditTexts
    private void distributeAdjustment(EditText[] editTexts, float adjustment, EditText excludeEditText) {
        float totalAdjustment = 0;

        // Calculate total sum of values excluding the excluded EditText
        float totalSumExcluding = 0;
        for (EditText editText : editTexts) {
            if (editText != excludeEditText) {
                String text = editText.getText().toString();
                if (!text.isEmpty()) {
                    totalSumExcluding += Float.parseFloat(text);
                }
            }
        }

        // Distribute the adjustment proportionally across all EditTexts
        for (EditText editText : editTexts) {
            if (editText != excludeEditText) {
                String text = editText.getText().toString();
                if (!text.isEmpty()) {
                    float value = Float.parseFloat(text);
                    float adjustmentRatio = value / totalSumExcluding;
                    float individualAdjustment = adjustmentRatio * adjustment;

                    // Update EditText value
                    editText.setText(Float.toString(value - individualAdjustment));

                    totalAdjustment += individualAdjustment;
                }
            }
        }

        // Apply any remaining adjustment to the excluded EditText
        if (excludeEditText != null) {
            String text = excludeEditText.getText().toString();
            if (!text.isEmpty()) {
                float value = Float.parseFloat(text);
                excludeEditText.setText(Float.toString(value - (adjustment - totalAdjustment)));
            }
        }
    }


}
