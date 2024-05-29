package com.example.biitemployeeperformanceappraisalsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.models.Task;
import com.example.biitemployeeperformanceappraisalsystem.models.TaskWithEmployees;
import com.example.biitemployeeperformanceappraisalsystem.network.services.TaskService;

import java.util.List;

public class MyTasksAdapter extends ArrayAdapter<TaskWithEmployees> {
    private LayoutInflater inflater;
    private int resourceId;
    List<TaskWithEmployees> tasks;
    public MyTasksAdapter(Context context, int resourceId, List<TaskWithEmployees> tasks) {
        super(context, resourceId, tasks);
        this.inflater = LayoutInflater.from(context);
        this.resourceId = resourceId;
        this.tasks = tasks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TaskWithEmployees task = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.my_tasks_list_item_layout, parent, false);
        }

        TextView taskDescriptionTextView =  convertView.findViewById(R.id.text_task_description);
        //TextView task
        TextView dueDateTextView = convertView.findViewById(R.id.text_due_date);
        TextView assignedByTextView = convertView.findViewById(R.id.text_assigned_by);
        Button btnDone = convertView.findViewById(R.id.btn_done_task);

        // Populate the data into the template view using the data object
        taskDescriptionTextView.setText(task.getTask().getTask_description().toString());
        dueDateTextView.setText("Due: "+task.getTask().getDue_date().toString()); // You may need to format the date properly
        assignedByTextView.setText("By: "+String.valueOf(task.getAssigned_by().getName()));

        TaskService taskService = new TaskService(getContext());
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task t = new Task();
                t = task.getTask();
                t.setStatus(1);
                taskService.putTask(
                        t,
                        task1 -> {
                            tasks.remove(task);
                            notifyDataSetChanged();
                        },
                        errorMessage -> {
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        });
            }
        });

        return convertView;
    }
}
