package com.example.biitemployeeperformanceappraisalsystem.admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.adapter.EmployeeDetailsListAdapter;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeDetails;
import com.example.biitemployeeperformanceappraisalsystem.network.services.CommonData;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeService;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmployeeListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmployeeListFragment extends Fragment {
    List<EmployeeDetails> employeeDetailsList;
    ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_employee_list, container, false);

        listView = view.findViewById(R.id.list_view);
        // employeeDetailsList = view.findViewById(R.id.list_view);

        EmployeeService data = new EmployeeService(view.getContext());

        data.getEmployeesWithDetails(
                employeeDetails -> {
                    employeeDetailsList = employeeDetails;
                    EmployeeDetailsListAdapter adapter = new EmployeeDetailsListAdapter(getContext(), R.layout.employee_list_item_layout, employeeDetails);
                    listView.setAdapter(adapter);
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
        );
        //On listview item clcik
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EmployeeDetails employeeDetails = employeeDetailsList.get(position);
                // Pass employee data to PerformanceFragment
                AddEmployeeFragment fragment = new AddEmployeeFragment();
                Bundle args = new Bundle();
                args.putInt("id",employeeDetails.getEmployee().getId());
//                args.putParcelable("employee_details", employeeDetailsScore);
                fragment.setArguments(args);
                AdminMainActivity adminMainActivity=(AdminMainActivity) getActivity();
                TextView textView=adminMainActivity.findViewById(R.id.txt_top);
                textView.setText("Employee");
                adminMainActivity.replaceFragment(fragment);
            }
        });
        return view;
    }
}