package com.example.biitemployeeperformanceappraisalsystem.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeDetailsScore;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDetailsScoreAdapter extends ArrayAdapter<EmployeeDetailsScore> implements Filterable {
    private LayoutInflater inflater;
    private int resourceId;
    private List<EmployeeDetailsScore> originalList;
    private List<EmployeeDetailsScore> filteredList;
    private String searchText = "";

    public EmployeeDetailsScoreAdapter(Context context, int resourceId, List<EmployeeDetailsScore> employeeDetailsList) {
        super(context, resourceId, employeeDetailsList);
        this.inflater = LayoutInflater.from(context);
        this.resourceId = resourceId;
        this.originalList = employeeDetailsList;
        this.filteredList = employeeDetailsList;
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Nullable
    @Override
    public EmployeeDetailsScore getItem(int position) {
        return filteredList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(resourceId, parent, false);
        }

        EmployeeDetailsScore employee = getItem(position);
        if (employee != null) {
            TextView textName = view.findViewById(R.id.text_rank);
            TextView textDepartment = view.findViewById(R.id.text_name);
            TextView textScore = view.findViewById(R.id.text_score);

            textName.setText("#" + (position + 1));

            String employeeName = employee.getEmployee().getName();
            SpannableString spannableString = new SpannableString(employeeName);
            if (!searchText.isEmpty()) {
                int startIndex = employeeName.toLowerCase().indexOf(searchText.toLowerCase());
                if (startIndex != -1) {
                    spannableString.setSpan(new StyleSpan(Typeface.BOLD), startIndex, startIndex + searchText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            textDepartment.setText(spannableString);

            textScore.setText(String.valueOf(employee.getTotalScore()) + "%");
        }

        return view;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                searchText = constraint == null ? "" : constraint.toString();
                if (constraint == null || constraint.length() == 0) {
                    results.values = originalList;
                    results.count = originalList.size();
                } else {
                    String filterString = constraint.toString().toLowerCase();
                    List<EmployeeDetailsScore> filtered = new ArrayList<>();

                    for (EmployeeDetailsScore employee : originalList) {
                        if (employee.getEmployee().getName().toLowerCase().contains(filterString)) {
                            filtered.add(employee);
                        }
                    }

                    results.values = filtered;
                    results.count = filtered.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (List<EmployeeDetailsScore>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
