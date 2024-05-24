package com.example.biitemployeeperformanceappraisalsystem;

import android.content.DialogInterface;
import android.opengl.Visibility;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.adapter.TaskAdapter;
import com.example.biitemployeeperformanceappraisalsystem.helper.DateTime;
import com.example.biitemployeeperformanceappraisalsystem.helper.SharedPreferencesManager;
import com.example.biitemployeeperformanceappraisalsystem.models.Department;
import com.example.biitemployeeperformanceappraisalsystem.models.Designation;
import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeRole;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeType;
import com.example.biitemployeeperformanceappraisalsystem.models.Task;
import com.example.biitemployeeperformanceappraisalsystem.models.TaskWithEmployees;
import com.example.biitemployeeperformanceappraisalsystem.models.TaskWithRole;
import com.example.biitemployeeperformanceappraisalsystem.network.services.CommonData;
import com.example.biitemployeeperformanceappraisalsystem.network.services.DepartmentService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.DesignationService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.TaskService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskFragment extends Fragment {
    boolean isIndividual = true;
    TaskService taskService;
    SharedPreferencesManager sharedPreferencesManager;
    EmployeeService employeeService;
    DesignationService designationService;
    DepartmentService departmentService;
    private Task task;
    private ListView taskListView;
    private List<TaskWithEmployees> taskWithEmployeesList;
    private List<EmployeeType> employeeTypeList;
    private List<Designation> designationList;
    private List<Department> departmentList;
    private List<Employee> employeeList;
    private LinearLayout employeeRoleLayout, employeeSpinnerLayout;
    private Spinner employeeSpinner, employeeTypeSpinner, designationSpinner, departmentSpinner;
    private TabLayout tasksTabLayout;
    private TabLayout taskAssignmentType;
    EditText dueDateEditText, taskWeightageEditText;
    FloatingActionButton addTaskButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        tasksTabLayout = view.findViewById(R.id.task_fragment_tabLayout);
        taskListView = view.findViewById(R.id.task_list_view);

        taskService = new TaskService(view.getContext());
        designationService = new DesignationService(view.getContext());
        departmentService = new DepartmentService(view.getContext());
        sharedPreferencesManager = new SharedPreferencesManager(view.getContext());


        // TabLayout
        tasksTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // This method will be called when a tab is selected.
                loadTasksForSelectedTab(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // This method will be called when a tab is unselected.
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // This method will be called when a tab is reselected.
            }
        });

        // Manually trigger the selection of the default tab (tab 0)
        loadTasksForSelectedTab(0);


        //Modal code
        // Inside your activity or fragment
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(R.layout.task_modal_layout);


        AlertDialog dialog = builder.create();

        addTaskButton = view.findViewById(R.id.btn_add_task);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflate the layout for the dialog
                View dialogView = getLayoutInflater().inflate(R.layout.task_modal_layout, null);
                employeeSpinner = dialogView.findViewById(R.id.spinner_employee);
                employeeSpinnerLayout = dialogView.findViewById(R.id.employee_spinner_layout);
                designationSpinner = dialogView.findViewById(R.id.spinner_designation);
                departmentSpinner = dialogView.findViewById(R.id.spinner_department);
                employeeTypeSpinner = dialogView.findViewById(R.id.spinner_employee_type);
                dueDateEditText = dialogView.findViewById(R.id.text_due_date);
                taskAssignmentType = dialogView.findViewById(R.id.task_assignment_type_tab);
                employeeRoleLayout = dialogView.findViewById(R.id.employee_role_layout);

                taskAssignmentType.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        int position = tab.getPosition();
                        switch(position){
                            case 0:
                                employeeSpinnerLayout.setVisibility(View.VISIBLE);
                                employeeRoleLayout.setVisibility(View.GONE);
                                isIndividual = true;
                                break;
                            case 1:
                                employeeRoleLayout.setVisibility(View.VISIBLE);
                                employeeSpinnerLayout.setVisibility(View.GONE);
                                isIndividual = false;
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });

                DateTime dateTime=new DateTime();

                dueDateEditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dateTime.showDateTimePicker(dueDateEditText,getContext());
                    }
                });

                employeeService=new EmployeeService(getContext());

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

                designationService.getDesignations(
                        designations -> {
                            designationList = designations;
                            designationService.populateDesignationSpinner(designations, designationSpinner);
                        },
                        errorMessage -> {
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }
                );

                departmentService.getDepartments(
                        departments -> {
                            departmentList = departments;
                            departmentService.populateDepartmentSpinner(departments, departmentSpinner);
                        },
                        errorMessage -> {
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }
                );

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
                        taskWeightageEditText = alertDialog.findViewById(R.id.text_weightage);
                        Employee employee=employeeList.get(employeeSpinner.getSelectedItemPosition());
                        EmployeeType employeeType=employeeTypeList.get(employeeTypeSpinner.getSelectedItemPosition());
                        Designation designation = designationList.get(designationSpinner.getSelectedItemPosition());
                        Department department = departmentList.get(departmentSpinner.getSelectedItemPosition());

                        task = new Task();
                        task.setAssigned_by_id(sharedPreferencesManager.getEmployeeUserObject().getEmployee().getId());
                        task.setTask_description(descriptionTextView.getText().toString());
                        task.setWeightage(Integer.parseInt(taskWeightageEditText.getText().toString()));
                        task.setSession_id(sharedPreferencesManager.getSessionId());
                        Date date = new Date();
                        task.setAssigned_date(date);
                        try {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                            Date parsedDate = dateFormat.parse(dueDateEditText.getText().toString());
                            task.setDue_date(parsedDate);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }

                        if (isIndividual){
                            task.setAssigned_to_id(employee.getId());
                            taskService.postTask(
                                    task,
                                    task -> {
                                        // set response task in list and update list view
                                    },
                                    errorMessage -> {
                                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                                    }
                            );
                        }else {
                            TaskWithRole taskWithRole = new TaskWithRole();
                            EmployeeRole role = new EmployeeRole();
                            taskWithRole.setTask(task);
                            role.setDesignation(designation);
                            role.setDepartment(department);
                            role.setEmployeeType(employeeType);
                            taskWithRole.setRole(role);
                            taskService.postRoleBasedTask(
                                    taskWithRole,
                                    task -> {
                                        // set response task in list and update list view
                                    },
                                    errorMessage -> {
                                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                                    }
                            );
                        }

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

    private void loadTasksForSelectedTab(int position){
        switch (position) {
            case 0:
                // Code for handling the first tab selection
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
                break;
            case 1:
                // Code for handling the second tab selection
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
                break;
            case 2:
                // Code for handling the third tab selection
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
                break;
            default:
                break;
        }
    }
}