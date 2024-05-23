package com.example.biitemployeeperformanceappraisalsystem.director;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.PerformanceFragment;
import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.adapter.EmployeeDetailsScoreAdapter;
import com.example.biitemployeeperformanceappraisalsystem.helper.SharedPreferencesManager;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeDetailsScore;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeService;

import java.util.List;

public class DirectorReportFragment extends Fragment {
    SharedPreferencesManager sharedPreferencesManager;
    List<EmployeeDetailsScore> employeeDetailsScoreList;
    EmployeeDetailsScoreAdapter adapter;
    ListView listView;
    SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_director_report, container, false);

        listView = view.findViewById(R.id.list_view);
        searchView = view.findViewById(R.id.search_view);

        EmployeeService data = new EmployeeService(view.getContext());
        sharedPreferencesManager = new SharedPreferencesManager(getContext());
        int session_id = sharedPreferencesManager.getSessionId();

        data.getEmployeesWithKpiScores(
                session_id,
                // onSuccess callback
                employeeDetails -> {
                    employeeDetailsScoreList = employeeDetails;
                    adapter = new EmployeeDetailsScoreAdapter(getContext(), R.layout.list_item_layout, employeeDetails);
                    listView.setAdapter(adapter);
                },
                // onFailure callback
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
        );

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        //On listview item click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EmployeeDetailsScore employeeDetailsScore = adapter.getItem(position);
                // Pass employee data to PerformanceFragment
                int employeeID = employeeDetailsScore.getEmployee().getId();
                PerformanceFragment fragment = new PerformanceFragment(employeeID);
                Bundle args = new Bundle();
                args.putInt("id", employeeDetailsScore.getEmployee().getId());
                fragment.setArguments(args);
                DirectorMainActivity directorMainActivity = (DirectorMainActivity) getActivity();
                directorMainActivity.replaceFragment(fragment);
            }
        });

        return view;
    }
}
