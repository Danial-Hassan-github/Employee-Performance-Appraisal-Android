package com.example.biitemployeeperformanceappraisalsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.biitemployeeperformanceappraisalsystem.R;

import java.util.List;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {

    private List<String> items;
    private LayoutInflater inflater;

    public CustomSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<String> items) {
        super(context, resource, items);
        this.items = items;
        inflater = LayoutInflater.from(context);
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

    private View getCustomView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            view = inflater.inflate(R.layout.custom_spinner_item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.checkbox = view.findViewById(R.id.checkbox);
            viewHolder.text = view.findViewById(R.id.text);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        String item = items.get(position);
        viewHolder.text.setText(item);

        // You can set listeners or handle checkbox state here if needed

        return view;
    }

    private static class ViewHolder {
        CheckBox checkbox;
        TextView text;
    }
}
