package com.example.biitemployeeperformanceappraisalsystem;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.adapter.TaskAdapter;
import com.example.biitemployeeperformanceappraisalsystem.helper.DateTime;
import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeType;
import com.example.biitemployeeperformanceappraisalsystem.models.Task;
import com.example.biitemployeeperformanceappraisalsystem.models.TaskWithEmployees;
import com.example.biitemployeeperformanceappraisalsystem.network.services.CommonData;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.TaskService;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskFragment extends Fragment {
    private Task task;
    private ListView taskListView;
    private List<TaskWithEmployees> taskWithEmployeesList;
    private List<EmployeeType> employeeTypeList;
    private List<Employee> employeeList;
    private Spinner tasksSpinner,employeeSpinner,employeeTypeSpinner;
    EditText dueDateEditText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        tasksSpinner = view.findViewById(R.id.spinner_tasks);
        taskListView = view.findViewById(R.id.task_list_view);

        ArrayList<String> taskTypes = new ArrayList<String>();
        taskTypes.add("All");
        taskTypes.add("Pending");
        taskTypes.add("Completed");

        ArrayAdapter<String> taskTypeAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, taskTypes);
        tasksSpinner.setAdapter(taskTypeAdapter);

        TaskService taskService = new TaskService(view.getContext());

        tasksSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    taskService.getTasks(
                            tasksWithEmployees -> {
                                taskWithEmployeesList = tasksWithEmployees;
                                TaskAdapter adapter = new TaskAdapter(getContext(), R.layout.task_list_item_layout, tasksWithEmployees);
                                taskListView.setAdapter(adapter);
                            },
                            errorMessage -> {
                                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                            }
                    );
                }else if (position==1){
                    taskService.getPendingTasks(
                            tasksWithEmployees -> {
                                taskWithEmployeesList = tasksWithEmployees;
                                TaskAdapter adapter = new TaskAdapter(getContext(), R.layout.task_list_item_layout, tasksWithEmployees);
                                taskListView.setAdapter(adapter);
                            },
                            errorMessage -> {
                                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                            }
                    );
                }else {
                    taskService.getCompletedTasks(
                            tasksWithEmployees -> {
                                taskWithEmployeesList = tasksWithEmployees;
                                TaskAdapter adapter = new TaskAdapter(getContext(), R.layout.task_list_item_layout, tasksWithEmployees);
                                taskListView.setAdapter(adapter);
                            },
                            errorMessage -> {
                                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                            }
                    );
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Modal code
        // Inside your activity or fragment
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(R.layout.task_modal_layout);


        AlertDialog dialog = builder.create();

        Button showModalButton = view.findViewById(R.id.btn_add_task);
        showModalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflate the layout for the dialog
                View dialogView = getLayoutInflater().inflate(R.layout.task_modal_layout, null);
                employeeSpinner = dialogView.findViewById(R.id.spinner_employee);
                employeeTypeSpinner = dialogView.findViewById(R.id.spinner_employee_type);
                dueDateEditText = dialogView.findViewById(R.id.text_due_date);

                DateTime dateTime=new DateTime();

                dueDateEditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dateTime.showDateTimePicker(dueDateEditText,getContext());
                    }
                });

                EmployeeService employeeService=new EmployeeService(getContext());
                CommonData data = new CommonData(getContext());

                employeeService.getEmployeeTypes(
                        employeeTypes -> {
                            employeeTypeList = employeeTypes;
                            employeeService.populateEmployeeTypeSpinner(employeeTypes, employeeTypeSpinner);
                        },
                        errorMessage -> {
                            Toast.makeText(getContext(),errorMessage,Toast.LENGTH_SHORT);
                        }
                );

                employeeService.getEmployees(
                        employees -> {
                            employeeList = employees;
                            employeeService.populateEmployeesSpinner(employees, employeeSpinner);
                        },
                        errorMessage -> {
                            Toast.makeText(getContext(),errorMessage,Toast.LENGTH_SHORT);
                        }
                );

//                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, data.generateEmployeeTypes());
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                employeeSpinner.setAdapter(adapter);
//
//                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, data.generateEmployeeTypes());
//                employeeTypeSpinner.setAdapter(adapter);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(dialogView);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle Save button click
                        AlertDialog alertDialog = (AlertDialog) dialog;
                        TextView descriptionTextView = alertDialog.findViewById(R.id.text_task_description);
                        Employee employee=employeeList.get(employeeSpinner.getSelectedItemPosition());
                        EmployeeType employeeType=employeeTypeList.get(employeeTypeSpinner.getSelectedItemPosition());


                        // Get the description text and save the task
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        // Inflate the layout for this fragment
        return view;
    }
}