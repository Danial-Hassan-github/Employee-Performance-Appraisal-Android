package com.example.biitemployeeperformanceappraisalsystem.director;

import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeKpi;
import com.example.biitemployeeperformanceappraisalsystem.models.GroupKpiDetails;
import com.example.biitemployeeperformanceappraisalsystem.models.GroupKpiWithWeightage;
import com.example.biitemployeeperformanceappraisalsystem.models.KPI;
import com.example.biitemployeeperformanceappraisalsystem.models.KpiWithSubKpiWeightages;
import com.example.biitemployeeperformanceappraisalsystem.network.services.KpiService;

import java.util.List;

public class KpiWeightageAdjustmentFormFragment extends Fragment {
    List<KPI> kpiList;
    GroupKpiDetails groupKpiDetails;
    KpiWithSubKpiWeightages kpiWithSubKpiWeightages;
    EmployeeKpi employeeKpi;
    GroupKpiWithWeightage groupKpiWithWeightage;
    TextView txtTotalWeightage, txtExceedMsg;
    KpiService kpiService;
    float newSum = 0;

    public KpiWeightageAdjustmentFormFragment(GroupKpiDetails groupKpiDetails, KpiWithSubKpiWeightages kpiWithSubKpiWeightages) {
        this.groupKpiDetails = groupKpiDetails;
        this.kpiWithSubKpiWeightages = kpiWithSubKpiWeightages;
    }

    public KpiWeightageAdjustmentFormFragment(GroupKpiDetails groupKpiDetails, EmployeeKpi employeeKpi) {
        this.groupKpiDetails = groupKpiDetails;
        this.employeeKpi = employeeKpi;
    }

    public KpiWeightageAdjustmentFormFragment(GroupKpiDetails groupKpiDetails, GroupKpiWithWeightage groupKpiWithWeightage) {
        this.groupKpiDetails = groupKpiDetails;
        this.groupKpiWithWeightage = groupKpiWithWeightage;
    }

    public KpiWeightageAdjustmentFormFragment(List<KPI> kpiList, KpiWithSubKpiWeightages kpiWithSubKpiWeightages){
        this.kpiList = kpiList;
        this.kpiWithSubKpiWeightages = kpiWithSubKpiWeightages;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kpi_weightage_adjustment_form, container, false);
        LinearLayout layout = view.findViewById(R.id.kpi_weightage_adjustment_form_layout);
        txtTotalWeightage = view.findViewById(R.id.txt_total_weightage);
        txtExceedMsg = view.findViewById(R.id.exceed_msg);
        Button saveButton = view.findViewById(R.id.save_button);

        kpiService = new KpiService(getContext());
        // TODO
        // kpiList.add(kpiWithSubKpiWeightages.getKpi());

        EditText[] editTexts = new EditText[kpiList.size()];

        for (int i = 0; i < kpiList.size(); i++) {
            KPI kpiDetails = kpiList.get(i);

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
                    try {
                        kpiDetails.getKpiWeightage().setWeightage(Integer.parseInt(valueEditText.getText().toString()));
                    }catch (Exception ex){

                    }
                    newSum = calculateTotalSum(editTexts);
                    txtTotalWeightage.setText(String.format("%.2f", newSum));
                    if (newSum > 100) {
                        saveButton.setEnabled(false);
                        txtTotalWeightage.setTextColor(Color.RED);
                        txtExceedMsg.setText("Please adjust weightage below 100");
                    } else {
                        saveButton.setEnabled(true);
                        txtTotalWeightage.setTextColor(Color.GREEN);
                        txtExceedMsg.setText("");
                    }
                }
            });
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement save functionality here
                // KPI newKpi = groupKpiDetails.getKpiList().get(groupKpiDetails.getKpiList().size() - 1);
                // groupKpiDetails.getKpiList().get(groupKpiDetails.getKpiList().size() - 1).setSubKpiWeightages(kpiWithSubKpiWeightages);
                // newKpi.getKpiWeightage().
                kpiService.putKpi(
                        kpiList,
                        k -> {
                            Toast.makeText(getContext(), k, Toast.LENGTH_SHORT).show();
                        },
                        errorMessage -> {
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        });
                if (kpiWithSubKpiWeightages != null){
                    kpiService.postKpi(
                            kpiWithSubKpiWeightages,
                            kpi -> {
                                Toast.makeText(getContext(), "New Kpi Added Successfully", Toast.LENGTH_SHORT).show();
                            },
                            errorMessage -> {
                                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                            }
                    );
                }
            }
        });

        return view;
    }

    private float calculateTotalSum(EditText[] editTexts) {
        float sum = 0;
        for (EditText editText : editTexts) {
            String text = editText.getText().toString();
            if (!text.isEmpty()) {
                try {
                    sum += Float.parseFloat(text);
                } catch (NumberFormatException e) {
                    // Handle potential number format exception
                    Toast.makeText(requireContext(), "Invalid number format", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return sum;
    }

    private void distributeAdjustment(EditText[] editTexts, float adjustment, EditText excludeEditText) {
        float totalAdjustment = 0;

        float totalSumExcluding = 0;
        for (EditText editText : editTexts) {
            if (editText != excludeEditText) {
                String text = editText.getText().toString();
                if (!text.isEmpty()) {
                    totalSumExcluding += Float.parseFloat(text);
                }
            }
        }

        for (EditText editText : editTexts) {
            if (editText != excludeEditText) {
                String text = editText.getText().toString();
                if (!text.isEmpty()) {
                    float value = Float.parseFloat(text);
                    float adjustmentRatio = value / totalSumExcluding;
                    float individualAdjustment = adjustmentRatio * adjustment;

                    editText.setText(String.format("%.2f", value - individualAdjustment));

                    totalAdjustment += individualAdjustment;
                }
            }
        }

        if (excludeEditText != null) {
            String text = excludeEditText.getText().toString();
            if (!text.isEmpty()) {
                float value = Float.parseFloat(text);
                excludeEditText.setText(String.format("%.2f", value - (adjustment - totalAdjustment)));
            }
        }
    }
}
