package com.example.biitemployeeperformanceappraisalsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.director.DirectorMainActivity;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeDetails;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeService;

import java.util.ArrayList;
import java.util.List;

import android.widget.Filter;
import android.widget.Filterable;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.widget.Filter;
import android.widget.Filterable;

public class EmployeeDetailsListAdapter extends ArrayAdapter<EmployeeDetails> implements Filterable {
    private Context context;
    private List<EmployeeDetails> employeeDetailsList;
    private List<EmployeeDetails> filteredEmployeeDetailsList;
    private FragmentActivity parentActivity;
    private String searchText = ""; // Store the current search text
    EmployeeService employeeService;

    public EmployeeDetailsListAdapter(Context context, int resource, List<EmployeeDetails> objects, FragmentActivity parentActivity) {
        super(context, resource, objects);
        this.context = context;
        this.employeeDetailsList = objects;
        this.filteredEmployeeDetailsList = objects;
        this.parentActivity = parentActivity;
        employeeService = new EmployeeService(context);
    }

    @Override
    public int getCount() {
        return filteredEmployeeDetailsList.size();
    }

    @Nullable
    @Override
    public EmployeeDetails getItem(int position) {
        return filteredEmployeeDetailsList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.employee_list_item_layout, parent, false);
        }

        EmployeeDetails employeeDetails = filteredEmployeeDetailsList.get(position);

        TextView textName = convertView.findViewById(R.id.text_name);
        ImageView btnDelete = convertView.findViewById(R.id.btn_delete_employee);

        // Highlight matching text
        String employeeName = employeeDetails.getEmployee().getName();
        SpannableString spannableString = new SpannableString(employeeName);
        if (!searchText.isEmpty()) {
            int startIndex = employeeName.toLowerCase().indexOf(searchText.toLowerCase());
            if (startIndex != -1) {
                spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), startIndex, startIndex + searchText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        textName.setText(spannableString);

        if (parentActivity instanceof DirectorMainActivity) {
            btnDelete.setVisibility(View.GONE);
        } else {
            btnDelete.setVisibility(View.VISIBLE);
        }

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                employeeService.deleteEmployee(
                        employeeDetails.getEmployee().getId(),
                        employee -> {
                            employeeDetailsList.remove(employeeDetails);
                            notifyDataSetChanged();
                        },
                        errorMessage -> {
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                );
            }
        });

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                searchText = constraint == null ? "" : constraint.toString(); // Update search text
                if (constraint == null || constraint.length() == 0) {
                    results.values = employeeDetailsList;
                    results.count = employeeDetailsList.size();
                } else {
                    String filterString = constraint.toString().toLowerCase();
                    List<EmployeeDetails> filteredList = new ArrayList<>();

                    for (EmployeeDetails employeeDetails : employeeDetailsList) {
                        if (employeeDetails.getEmployee().getName().toLowerCase().contains(filterString)) {
                            filteredList.add(employeeDetails);
                        }
                    }

                    results.values = filteredList;
                    results.count = filteredList.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredEmployeeDetailsList = (List<EmployeeDetails>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}