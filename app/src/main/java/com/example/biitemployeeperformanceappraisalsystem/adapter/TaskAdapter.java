package com.example.biitemployeeperformanceappraisalsystem.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.models.TaskWithEmployees;
import com.example.biitemployeeperformanceappraisalsystem.network.services.TaskService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends ArrayAdapter<TaskWithEmployees> {
    private LayoutInflater inflater;
    private int resourceId;
    private OnTaskEditListener onTaskEditListener;
    TaskService taskService;

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

        taskService = new TaskService(getContext());

        TextView taskDescriptionTextView = convertView.findViewById(R.id.text_task_description);
        TextView dueDateTextView = convertView.findViewById(R.id.text_due_date);
        TextView assignedToTextView = convertView.findViewById(R.id.text_assigned_to);
        TextView weightageTextView = convertView.findViewById(R.id.text_weightage);
        TextView assignedByTextView = convertView.findViewById(R.id.text_assigned_by);
        ImageView btnOptions = convertView.findViewById(R.id.btn_options_task);

        taskDescriptionTextView.setText(task.getTask().getTask_description());
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        try {
            Date dueDate = dateFormat.parse(task.getTask().getDue_date().toString());
            SimpleDateFormat dateFormatOutput = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            dueDateTextView.setText(dateFormatOutput.format(dueDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assignedToTextView.setText(task.getAssigned_to().getName());
        weightageTextView.setText(String.valueOf(task.getTask().getWeightage()));
        assignedByTextView.setText(task.getAssigned_by().getName());

        btnOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsDialog(task, position);
            }
        });

        return convertView;
    }


    private void showOptionsDialog(TaskWithEmployees task, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_task_options, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        Button btnEditTask = dialogView.findViewById(R.id.btn_edit_task);
        Button btnDeleteTask = dialogView.findViewById(R.id.btn_delete_task);

        btnEditTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showEditDialog(task); // Call the method to show the edit dialog
            }
        });

        btnDeleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                deleteTask(task, position);
            }
        });

        dialog.show();
    }

    // Method to show the edit dialog
    private void showEditDialog(TaskWithEmployees task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_edit_task, null);
        builder.setView(dialogView);

        EditText editWeightageEditText = dialogView.findViewById(R.id.edit_weightage);
        EditText editDueDateEditText = dialogView.findViewById(R.id.edit_due_date);
        EditText editDescriptionEditText = dialogView.findViewById(R.id.edit_description);

        // Set initial values in the edit text fields
        editWeightageEditText.setText(String.valueOf(task.getTask().getWeightage()));
        editDescriptionEditText.setText(task.getTask().getTask_description());

        // Format the due date
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
        try {
            Date dueDate = inputFormat.parse(task.getTask().getDue_date().toString());
            editDueDateEditText.setText(outputFormat.format(dueDate));
        } catch (ParseException e) {
            e.printStackTrace();
            editDueDateEditText.setText(""); // Set empty if parsing fails
        }

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get updated values from the edit text fields
                int newWeightage = Integer.parseInt(editWeightageEditText.getText().toString());
                String newTaskDescription = editDescriptionEditText.getText().toString();
                String newDueDate = editDueDateEditText.getText().toString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                // Update the task object
                task.getTask().setWeightage(newWeightage);
                task.getTask().setTask_description(newTaskDescription);
                try {
                    Date parsedDate = dateFormat.parse(newDueDate);
                    task.getTask().setDue_date(parsedDate);
                } catch (ParseException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                taskService.putTask(
                        task.getTask(),
                        task1 -> {
                            // Notify the adapter about the change
                            task.setTask(task1);
                            notifyDataSetChanged();
                        },
                        errorMessage -> {
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }
                );
                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void deleteTask(TaskWithEmployees task, int position) {
        TaskService taskService = new TaskService(getContext());
        taskService.deleteTask(
                task.getTask().getId(),
                task1 -> {
                    remove(task);
                    notifyDataSetChanged();
                },
                errorMessage -> Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show()
        );
    }

    public interface OnTaskEditListener {
        void onEditTask(TaskWithEmployees task);
    }
}
