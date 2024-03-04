package com.example.biitemployeeperformanceappraisalsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeDetails;

import java.util.List;

public class EmployeeDetailsListAdapter extends ArrayAdapter<EmployeeDetails> {
    private LayoutInflater inflater;
    private int resourceId;

    public EmployeeDetailsListAdapter(Context context, int resourceId, List<EmployeeDetails> employeeDetailsList) {
        super(context, resourceId, employeeDetailsList);
        this.inflater = LayoutInflater.from(context);
        this.resourceId = resourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(resourceId, parent, false);
        }

        // Get the current employee details
        EmployeeDetails employee = getItem(position);
        if (employee != null) {
            // Get the TextViews from the layout
            TextView textName = view.findViewById(R.id.text_name);

            // Set employee name
            textName.setText(employee.getEmployee().getName());
        }

        return view;
    }
}

