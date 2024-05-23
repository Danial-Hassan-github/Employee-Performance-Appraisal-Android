package com.example.biitemployeeperformanceappraisalsystem;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.adapter.MyTasksAdapter;
import com.example.biitemployeeperformanceappraisalsystem.helper.SharedPreferencesManager;
import com.example.biitemployeeperformanceappraisalsystem.models.TaskWithEmployees;
import com.example.biitemployeeperformanceappraisalsystem.network.services.TaskService;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyTasksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyTasksFragment extends Fragment {
    SharedPreferencesManager sharedPreferencesManager;
    private ListView taskListView;
    private List<TaskWithEmployees> taskWithEmployeesList;
    private Spinner spinner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_my_tasks, container, false);

        sharedPreferencesManager = new SharedPreferencesManager(getContext());
        int employeeID=sharedPreferencesManager.getEmployeeUserObject().getEmployee().getId();

        //spinner=view.findViewById(R.id.spinner);
        taskListView=view.findViewById(R.id.my_tasks_list_view);

//        ArrayList<String> taskTypes = new ArrayList<String>();
//        taskTypes.add("All");
//        taskTypes.add("Pending");
//        taskTypes.add("Completed");

        TaskService taskService = new TaskService(view.getContext());

        taskService.getEmployeeTasks(
                employeeID,
                // onSuccess callback
                tasksWithEmployees -> {
                    taskWithEmployeesList = tasksWithEmployees;
                    // Create ArrayAdapter and set it to the ListView
                    MyTasksAdapter adapter = new MyTasksAdapter(getContext(), R.layout.my_tasks_list_item_layout, tasksWithEmployees);
                    taskListView.setAdapter(adapter);
                },
                // onFailure callback
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
        );

        // Inflate the layout for this fragment
        return view;
    }
}