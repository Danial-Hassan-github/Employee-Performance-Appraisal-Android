package com.example.biitemployeeperformanceappraisalsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.models.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseListAdapter extends ArrayAdapter<Course> {

    private LayoutInflater inflater;
    private int resourceId;
    public CourseListAdapter(Context context, int resourceId, List<Course> courses) {
        super(context, resourceId, courses);
        this.inflater = LayoutInflater.from(context);
        this.resourceId = resourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Course course = getItem(position);

        if (convertView == null) {
            convertView = inflater.from(getContext()).inflate(resourceId, parent, false);
        }

        TextView nameTextView = convertView.findViewById(R.id.course_name);
        TextView codeTextView = convertView.findViewById(R.id.course_code);

        if (course!=null){
            nameTextView.setText(course.getName());
            codeTextView.setText(course.getCode());
        }

        return convertView;
    }
}

