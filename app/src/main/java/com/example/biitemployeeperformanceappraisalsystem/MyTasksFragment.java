package com.example.biitemployeeperformanceappraisalsystem;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.adapter.MyTasksAdapter;
import com.example.biitemployeeperformanceappraisalsystem.adapter.TaskAdapter;
import com.example.biitemployeeperformanceappraisalsystem.models.TaskDetails;
import com.example.biitemployeeperformanceappraisalsystem.network.CommonData;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyTasksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyTasksFragment extends Fragment {
    private ListView taskListView;
    private List<TaskDetails> taskDetailsList;
    private Spinner spinner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_my_tasks, container, false);
        //spinner=view.findViewById(R.id.spinner);
        taskListView=view.findViewById(R.id.my_tasks_list_view);

        ArrayList<String> taskTypes = new ArrayList<String>();
        taskTypes.add("All");
        taskTypes.add("Pending");
        taskTypes.add("Completed");

        CommonData data = new CommonData(view.getContext());

        data.getTasks(
                // onSuccess callback
                taskDetails -> {
                    taskDetailsList = taskDetails;
                    // Create ArrayAdapter and set it to the ListView
                    MyTasksAdapter adapter = new MyTasksAdapter(getContext(), R.layout.my_tasks_list_item_layout, taskDetails);
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