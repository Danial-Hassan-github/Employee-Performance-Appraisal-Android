package com.example.biitemployeeperformanceappraisalsystem;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskFragment extends Fragment {
    private Spinner spinner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_task, container, false);

        ArrayList<String> taskTypes = new ArrayList<String>();
        taskTypes.add("All");
        taskTypes.add("Pending");
        taskTypes.add("Completed");

        ArrayAdapter<String> taskTypeAdapter=new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item,taskTypes);
        spinner=view.findViewById(R.id.spinner);
        spinner.setAdapter(taskTypeAdapter);
        // Inflate the layout for this fragment
        return view;
    }
}