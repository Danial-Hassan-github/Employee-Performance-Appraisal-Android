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
import com.example.biitemployeeperformanceappraisalsystem.models.GroupKpiDetails;
import com.example.biitemployeeperformanceappraisalsystem.models.KPI;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KpiWeightageAdjustmentFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KpiWeightageAdjustmentFormFragment extends Fragment {
    GroupKpiDetails groupKpiDetails;

    public KpiWeightageAdjustmentFormFragment(GroupKpiDetails groupKpiDetails) {
        this.groupKpiDetails = groupKpiDetails;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kpi_weightage_adjustment_form, container, false);
        LinearLayout layout = view.findViewById(R.id.kpi_weightage_adjustment_form_layout);

        EditText[] editTexts = new EditText[groupKpiDetails.getKpiList().size()];

        for (int i = 0; i < groupKpiDetails.getKpiList().size(); i++) {
            KPI kpiDetails = groupKpiDetails.getKpiList().get(i);

            TextView titleTextView = new TextView(requireContext());
            titleTextView.setText(kpiDetails.getName());

            EditText valueEditText = new EditText(requireContext());
            valueEditText.setText(Float.toString(kpiDetails.getKpiWeightage().getWeightage()));

            layout.addView(titleTextView);
            layout.addView(valueEditText);

            editTexts[i] = valueEditText;

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
                    float newSum = calculateTotalSum(editTexts);
                    if (newSum > 100) {
                        float adjustment = newSum - 100;
                        distributeAdjustment(editTexts, adjustment, valueEditText);
                    }
                }
            });
        }

        Button saveButton = view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the modified values
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
