package com.example.biitemployeeperformanceappraisalsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.director.DirectorMainActivity;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeDetails;

import java.util.List;

public class EmployeeDetailsListAdapter extends ArrayAdapter<EmployeeDetails> {
    private Context context;
    private List<EmployeeDetails> employeeDetailsList;
    private FragmentActivity parentActivity;

    public EmployeeDetailsListAdapter(Context context, int resource, List<EmployeeDetails> objects, FragmentActivity parentActivity) {
        super(context, resource, objects);
        this.context = context;
        this.employeeDetailsList = objects;
        this.parentActivity = parentActivity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.employee_list_item_layout, parent, false);
        }

        EmployeeDetails employeeDetails = employeeDetailsList.get(position);

        TextView textName = convertView.findViewById(R.id.text_name);
        ImageView btnDelete = convertView.findViewById(R.id.btn_delete_employee);

        textName.setText(employeeDetails.getEmployee().getName());

        if (parentActivity instanceof DirectorMainActivity) {
            btnDelete.setVisibility(View.GONE);
        } else {
            btnDelete.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}


