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

public class CourseListAdapter extends ArrayAdapter<Course> {

    public CourseListAdapter(Context context, ArrayList<Course> courses) {
        super(context, 0, courses);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Course course = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.courses_list_view_layout, parent, false);
        }

        TextView nameTextView = convertView.findViewById(R.id.course_name);
        TextView codeTextView = convertView.findViewById(R.id.course_code);

        nameTextView.setText(course.getName());
        codeTextView.setText(course.getCode());

        return convertView;
    }
}

