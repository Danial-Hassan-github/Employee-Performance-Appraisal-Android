package com.example.biitemployeeperformanceappraisalsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.models.TaskWithEmployees;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<TaskWithEmployees> {

    private LayoutInflater inflater;
    private int resourceId;
    public TaskAdapter(Context context, int resourceId, List<TaskWithEmployees> tasks) {
        super(context, resourceId, tasks);
        this.inflater = LayoutInflater.from(context);
        this.resourceId = resourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TaskWithEmployees task = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_list_item_layout, parent, false);
        }

        TextView taskDescriptionTextView =  convertView.findViewById(R.id.text_task_description);
        //TextView tas
        TextView dueDateTextView = convertView.findViewById(R.id.text_due_date);
        TextView assignedToTextView = convertView.findViewById(R.id.text_assigned_to);
        TextView weightageTextView = convertView.findViewById(R.id.text_weightage);
        TextView assignedByTextView = convertView.findViewById(R.id.text_assigned_by);

        // Populate the data into the template view using the data object
        taskDescriptionTextView.setText(task.getTask().getTask_description().toString());
        dueDateTextView.setText("Due: "+task.getTask().getDue_date().toGMTString()); // You may need to format the date properly
        assignedToTextView.setText("TO: "+String.valueOf(task.getAssigned_to().getName()));
        weightageTextView.setText("Weightage"+String.valueOf(task.getTask().getWeightage()));
        assignedByTextView.setText("By: "+String.valueOf(task.getAssigned_by().getName()));

        return convertView;
    }
}
