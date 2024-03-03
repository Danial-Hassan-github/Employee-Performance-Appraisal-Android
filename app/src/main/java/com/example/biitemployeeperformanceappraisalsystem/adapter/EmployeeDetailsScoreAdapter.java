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
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeDetailsScore;

import java.util.List;

public class EmployeeDetailsScoreAdapter extends ArrayAdapter<EmployeeDetailsScore> {
    private LayoutInflater inflater;
    private int resourceId;

    public EmployeeDetailsScoreAdapter(Context context, int resourceId, List<EmployeeDetailsScore> employeeDetailsList) {
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
        EmployeeDetailsScore employee = getItem(position);
        if (employee != null) {
            // Get the TextViews from the layout
            TextView textName = view.findViewById(R.id.text_rank);
            TextView textDepartment = view.findViewById(R.id.text_name);
            TextView textScore = view.findViewById(R.id.text_score);

            // Set employee name, department, and score to the TextViews
            textName.setText("#"+(position+1));
            textDepartment.setText(employee.getEmployee().getName());
            textScore.setText(String.valueOf(employee.getTotalScore()));
        }

        return view;
    }
}
