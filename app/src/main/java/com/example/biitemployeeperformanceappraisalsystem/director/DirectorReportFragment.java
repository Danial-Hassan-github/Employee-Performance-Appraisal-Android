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
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.PerformanceFragment;
import com.example.biitemployeeperformanceappraisalsystem.PieChartPerformanceFragment;
import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.adapter.EmployeeDetailsScoreAdapter;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeDetails;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeDetailsScore;
import com.example.biitemployeeperformanceappraisalsystem.network.CommonData;

import java.util.ArrayList;
import java.util.List;

public class DirectorReportFragment extends Fragment {

    List<EmployeeDetails> employeeDetailsList;
    ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_director_report, container, false);
        // Initialize your views and set up any listeners here
        // Assuming userRecyclerView is your RecyclerView object
        listView = view.findViewById(R.id.list_view);
        // employeeDetailsList = view.findViewById(R.id.list_view);

        CommonData data = new CommonData(view.getContext());

        // Create an ArrayAdapter and set it to the RecyclerView
        // ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, employeeDetailsList);
        data.GetEmployeesWithKpiScores(
                // onSuccess callback
                employeeDetails -> {
                    List<EmployeeDetailsScore> employeeDetailsScoreList = employeeDetails;
                    // Extract employee names and scores
                    List<String> employeeNames = new ArrayList<>();
                    int i=1;
                    for (EmployeeDetailsScore employee : employeeDetails) {
                        employeeNames.add("#" + i + "   " + employee.getEmployee().getName() + "    " + employee.getTotalScore());
                        i++;
                    }

                    // Create ArrayAdapter and set it to the ListView
                    EmployeeDetailsScoreAdapter adapter = new EmployeeDetailsScoreAdapter(getContext(), R.layout.list_item_layout, employeeDetails);
                    listView.setAdapter(adapter);
                },
                // onFailure callback
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
        );
        //On listview item clcik
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DirectorMainActivity directorMainActivity = (DirectorMainActivity) getActivity();
                directorMainActivity.replaceFragment(new PerformanceFragment());
            }
        });
        return view;
    }
}