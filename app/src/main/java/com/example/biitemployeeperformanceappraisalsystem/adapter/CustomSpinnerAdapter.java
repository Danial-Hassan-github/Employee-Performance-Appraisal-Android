package com.example.biitemployeeperformanceappraisalsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.biitemployeeperformanceappraisalsystem.R;

import java.util.ArrayList;
import java.util.List;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {

    private List<String> items;
    private List<Integer> evaluatee_ids;
    private List<Boolean> itemChecked;
    private LayoutInflater inflater;
    private boolean selectAllChecked = false;

    public CustomSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<String> items) {
        super(context, resource, items);
        this.items = items;
        this.itemChecked = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            itemChecked.add(false);
        }
        inflater = LayoutInflater.from(context);
        evaluatee_ids = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        final ViewHolder viewHolder;

        if (view == null) {
            view = inflater.inflate(R.layout.custom_spinner_item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.checkbox = view.findViewById(R.id.checkbox);
            viewHolder.text = view.findViewById(R.id.text);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final String item = items.get(position);
        viewHolder.text.setText(item);

        viewHolder.checkbox.setOnCheckedChangeListener(null); // Clear previous listener

        if (position == 0) {
            viewHolder.checkbox.setChecked(selectAllChecked);
        } else {
            viewHolder.checkbox.setChecked(itemChecked.get(position));
        }

        viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (position == 0) {
                    selectAllChecked = isChecked;
                    for (int i = 1; i < itemChecked.size(); i++) {
                        itemChecked.set(i, isChecked);
                    }
                    notifyDataSetChanged();
                } else {
                    itemChecked.set(position, isChecked);
                    if (isChecked) {
                        // If checked, add the corresponding employee ID to evaluatee_ids list
                        evaluatee_ids.add(position - 1); // Subtract 1 because 0th position is for select all
                    } else {
                        // If unchecked, remove the corresponding employee ID from evaluatee_ids list
                        evaluatee_ids.remove((Integer) (position - 1));
                    }
                }
            }
        });

        return view;
    }

    private static class ViewHolder {
        CheckBox checkbox;
        TextView text;
    }

    // Method to get the list of selected employee IDs
    public List<Integer> getSelectedEmployeeIds() {
        return evaluatee_ids;
    }
}
