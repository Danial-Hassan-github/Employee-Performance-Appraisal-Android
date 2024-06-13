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
import com.example.biitemployeeperformanceappraisalsystem.models.Course;

import java.util.ArrayList;
import java.util.List;

public class CustomCourseSpinnerAdapter extends ArrayAdapter<Course> {

    public interface OnItemSelectionChangedListener {
        void onItemSelectionChanged(List<Integer> selectedCourseIds);
    }

    private List<Integer> selectedCourseIds;
    private List<Course> courses;
    private List<Boolean> itemChecked;
    private LayoutInflater inflater;
    private boolean selectAllChecked = false;
    private OnItemSelectionChangedListener listener;

    public CustomCourseSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<Course> courses) {
        super(context, resource, courses);
        this.courses = courses;
        this.selectedCourseIds = new ArrayList<>();
        this.itemChecked = new ArrayList<>();
        for (int i = 0; i < courses.size(); i++) {
            itemChecked.add(false);
        }
        inflater = LayoutInflater.from(context);
    }

    public void setOnItemSelectionChangedListener(OnItemSelectionChangedListener listener) {
        this.listener = listener;
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

        final Course course = courses.get(position);
        viewHolder.text.setText(course.getName());

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
                        selectedCourseIds.add(courses.get(i).getId());
                    }
                    notifyDataSetChanged();
                } else {
                    itemChecked.set(position, isChecked);
                    if (isChecked) {
                        selectedCourseIds.add(course.getId());
                    } else {
                        selectedCourseIds.remove((Integer) course.getId());
                    }
                }
                if (listener != null) {
                    listener.onItemSelectionChanged(new ArrayList<>(selectedCourseIds));
                }
            }
        });

        return view;
    }

    private static class ViewHolder {
        CheckBox checkbox;
        TextView text;
    }

    public List<Integer> getSelectedCourseIds() {
        return new ArrayList<>(selectedCourseIds);
    }
}
