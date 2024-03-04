package com.example.biitemployeeperformanceappraisalsystem.director;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.PerformanceFragment;
import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.adapter.EmployeeDetailsScoreAdapter;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeDetailsScore;
import com.example.biitemployeeperformanceappraisalsystem.network.CommonData;

import java.util.List;

public class DirectorReportFragment extends Fragment {

    List<EmployeeDetailsScore> employeeDetailsScoreList;
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

        data.GetEmployeesWithKpiScores(
                // onSuccess callback
                employeeDetails -> {
                    employeeDetailsScoreList = employeeDetails;
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
                EmployeeDetailsScore employeeDetailsScore = employeeDetailsScoreList.get(position);
                // Pass employee data to PerformanceFragment
                PerformanceFragment fragment = new PerformanceFragment();
                Bundle args = new Bundle();
                args.putInt("id",employeeDetailsScore.getEmployee().getId());
//                args.putParcelable("employee_details", employeeDetailsScore);
                fragment.setArguments(args);
                DirectorMainActivity directorMainActivity = (DirectorMainActivity) getActivity();
                directorMainActivity.replaceFragment(fragment);
            }
        });
        return view;
    }
}