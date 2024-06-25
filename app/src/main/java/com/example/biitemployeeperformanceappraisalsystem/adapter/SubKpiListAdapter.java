package com.example.biitemployeeperformanceappraisalsystem.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.helper.SharedPreferencesManager;
import com.example.biitemployeeperformanceappraisalsystem.models.SubKpi;
import com.example.biitemployeeperformanceappraisalsystem.models.SubKpiWeightage;

import java.util.List;

public class SubKpiListAdapter extends ArrayAdapter<SubKpi> {
    private LayoutInflater inflater;
    private int resourceId;
    private List<SubKpi> subKpiList;
    private boolean isUpdating = false;
    SharedPreferencesManager sharedPreferencesManager;
    private OnSubKpiRemoveListener onSubKpiRemoveListener;

    public SubKpiListAdapter(Context context, int resourceId, List<SubKpi> subKpiList) {
        super(context, resourceId, subKpiList);
        this.inflater = LayoutInflater.from(context);
        this.resourceId = resourceId;
        this.subKpiList = subKpiList;
    }

    public interface OnSubKpiRemoveListener {
        void onSubKpiRemove(SubKpi subKpi);
    }

    public void setOnSubKpiRemoveListener(OnSubKpiRemoveListener listener) {
        this.onSubKpiRemoveListener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(resourceId, parent, false);
        }

        sharedPreferencesManager = new SharedPreferencesManager(getContext());

        // Get the current sub KPI
        SubKpi subKpi = getItem(position);
        if (subKpi != null) {
            TextView textName = view.findViewById(R.id.text_subKpiName);
            EditText editTextWeightage = view.findViewById(R.id.edit_text_subKpi_weightage);
            ImageButton btnRemove = view.findViewById(R.id.btn_remove_subKpi);

            textName.setText(subKpi.getName());

            if (subKpi.getSubKpiWeightage() != null) {
                editTextWeightage.setText(String.valueOf(subKpi.getSubKpiWeightage().getWeightage()));
            } else {
                editTextWeightage.setText(String.valueOf(0));
            }

            // Remove existing TextWatcher
            if (editTextWeightage.getTag() instanceof TextWatcher) {
                editTextWeightage.removeTextChangedListener((TextWatcher) editTextWeightage.getTag());
            }

            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    if (isUpdating) return; // Prevent infinite loop
                    isUpdating = true;

                    int totalWeightage = 0;
                    int currentWeightage = 0;

                    try {
                        currentWeightage = Integer.parseInt(s.toString());
                    } catch (NumberFormatException e) {
                        currentWeightage = 0;
                    }

                    SubKpiWeightage subKpiWeightage = new SubKpiWeightage();
                    subKpiWeightage.setSub_kpi_id(subKpi.getId());
                    subKpiWeightage.setSession_id(sharedPreferencesManager.getSessionId());
                    subKpiWeightage.setWeightage(currentWeightage);
                    subKpi.setSubKpiWeightage(subKpiWeightage);

                    for (int i = 0; i < getCount(); i++) {
                        SubKpi currentSubKpi = getItem(i);
                        if (currentSubKpi != null && currentSubKpi.getSubKpiWeightage() != null) {
                            totalWeightage += currentSubKpi.getSubKpiWeightage().getWeightage();
                        }
                    }

                    if (totalWeightage > 100) {
                        Toast.makeText(getContext(), "Total Sub Kpi weightage cannot exceed 100", Toast.LENGTH_SHORT).show();
                    } else if (totalWeightage < 100) {
                        Toast.makeText(getContext(), "Total Sub Kpi weightage must be 100", Toast.LENGTH_SHORT).show();
                    }

                    isUpdating = false;
                }
            };

            editTextWeightage.addTextChangedListener(textWatcher);
            editTextWeightage.setTag(textWatcher);

            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    subKpiList.remove(position);
                    notifyDataSetChanged();

                    if (onSubKpiRemoveListener != null) {
                        onSubKpiRemoveListener.onSubKpiRemove(subKpi);
                    }
                }
            });
        }

        return view;
    }
}
