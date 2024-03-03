package com.example.biitemployeeperformanceappraisalsystem.director;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.biitemployeeperformanceappraisalsystem.PerformanceFragment;
import com.example.biitemployeeperformanceappraisalsystem.PieChartPerformanceFragment;
import com.example.biitemployeeperformanceappraisalsystem.R;

import java.util.ArrayList;

public class DirectorReportFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_director_report, container, false);
        // Initialize your views and set up any listeners here
        // Assuming userRecyclerView is your RecyclerView object
        ListView userListView = view.findViewById(R.id.list_view);

        // Create a list of user names (replace with your actual user data)
        ArrayList<String> userList = new ArrayList<>();
        userList.add("User 1");
        userList.add("User 2");
        userList.add("User 3");
        // Add more users as needed

        // Create an ArrayAdapter and set it to the RecyclerView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, userList);

        userListView.setAdapter(adapter);

        //On listview item clcik
        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DirectorMainActivity directorMainActivity = (DirectorMainActivity) getActivity();
                directorMainActivity.replaceFragment(new PerformanceFragment());
            }
        });
        return view;
    }
}