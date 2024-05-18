package com.example.biitemployeeperformanceappraisalsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeDetails;
import com.example.biitemployeeperformanceappraisalsystem.models.SubKpi;

import java.util.List;

public class SubKpiListAdapter extends ArrayAdapter<SubKpi> {
    private LayoutInflater inflater;
    private int resourceId;
    private List<SubKpi> subKpiList;
    public SubKpiListAdapter(Context context, int resourceId, List<SubKpi> subKpiList){
        super(context, resourceId, subKpiList);
        this.inflater = LayoutInflater.from(context);
        this.resourceId = resourceId;
        this.subKpiList = subKpiList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(resourceId, parent, false);
        }

        // Get the current employee details
        SubKpi subKpi = getItem(position);
        if (subKpi != null) {
            TextView textName = view.findViewById(R.id.text_subKpiName);
            EditText editTextWeightage = view.findViewById(R.id.edit_text_subKpi_weightage);
            ImageButton btnRemove = view.findViewById(R.id.btn_remove_subKpi);

            textName.setText(subKpi.getName());
            editTextWeightage.setText(String.valueOf(subKpi.getSubKpiWeightage().getWeightage()));
            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    subKpiList.remove(position);
                    notifyDataSetChanged();
                }
            });
        }

        return view;
    }
}
