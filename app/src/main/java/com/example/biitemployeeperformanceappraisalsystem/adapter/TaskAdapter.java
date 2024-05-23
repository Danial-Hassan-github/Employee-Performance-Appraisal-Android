package com.example.biitemployeeperformanceappraisalsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.models.TaskWithEmployees;
import com.example.biitemployeeperformanceappraisalsystem.network.services.TaskService;

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
        Button okButton = convertView.findViewById(R.id.btn_ok);
        EditText scoreEditText = convertView.findViewById(R.id.text_score);

        // Populate the data into the template view using the data object
        taskDescriptionTextView.setText(task.getTask().getTask_description());
        dueDateTextView.setText(task.getTask().getDue_date().toLocaleString()); // You may need to format the date properly
        assignedToTextView.setText(String.valueOf(task.getAssigned_to().getName()));
        weightageTextView.setText(String.valueOf(task.getTask().getWeightage()));
        assignedByTextView.setText(String.valueOf(task.getAssigned_by().getName()));

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the task object associated with this button
                TaskWithEmployees taskClicked = getItem(position);

                // Perform the action you want with the task object
                if (taskClicked != null) {
                    // Call put method and pass the task object
                    taskClicked.getTask().setScore(Integer.parseInt(scoreEditText.getText().toString()));
                    TaskService taskService = new TaskService(getContext());
                    taskService.putTask(
                            taskClicked.getTask(),
                            task1 -> {
                                // update task in list and also show it to completed list
                            },
                            errorMessage -> {
                                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT);
                            }
                    );
                    Toast.makeText(getContext(), taskClicked.getTask().getTask_description().toString(), Toast.LENGTH_SHORT).show();
                    // putMethod(taskClicked);
                }
            }
        });

        return convertView;
    }
}
