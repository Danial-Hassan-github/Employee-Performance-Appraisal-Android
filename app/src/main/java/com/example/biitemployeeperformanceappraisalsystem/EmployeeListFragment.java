package com.example.biitemployeeperformanceappraisalsystem;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.adapter.EmployeeDetailsListAdapter;
import com.example.biitemployeeperformanceappraisalsystem.admin.AddEmployeeFragment;
import com.example.biitemployeeperformanceappraisalsystem.admin.AdminMainActivity;
import com.example.biitemployeeperformanceappraisalsystem.director.DirectorMainActivity;
import com.example.biitemployeeperformanceappraisalsystem.helper.FragmentUtils;
import com.example.biitemployeeperformanceappraisalsystem.helper.SharedPreferencesManager;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeDetails;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EvaluationService;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmployeeListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmployeeListFragment extends Fragment {
    AppCompatButton btnAddEmployee;
    List<EmployeeDetails> employeeDetailsList;
    ListView listView;
    SearchView searchView;
    SharedPreferencesManager sharedPreferencesManager;
    EvaluationService evaluationService;
    EmployeeDetailsListAdapter adapter;
    private FragmentActivity parentActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_employee_list, container, false);
        parentActivity = getActivity();
        listView = view.findViewById(R.id.list_view);
        searchView = view.findViewById(R.id.search_view);
        btnAddEmployee = view.findViewById(R.id.btn_add_employee);
        sharedPreferencesManager = new SharedPreferencesManager(getContext());
        evaluationService = new EvaluationService(getContext());

        // employeeDetailsList = view.findViewById(R.id.list_view);

        if (parentActivity instanceof DirectorMainActivity){
            btnAddEmployee.setVisibility(View.GONE);
        }

        EmployeeService data = new EmployeeService(view.getContext());

        data.getEmployeesWithDetails(
                employeeDetails -> {
                    employeeDetailsList = employeeDetails;
                    adapter = new EmployeeDetailsListAdapter(getContext(), R.layout.employee_list_item_layout, employeeDetails, parentActivity);
                    listView.setAdapter(adapter);
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
        );

        btnAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminMainActivity adminMainActivity=(AdminMainActivity) getActivity();
                AddEmployeeFragment fragment = new AddEmployeeFragment();
                adminMainActivity.replaceFragment(fragment);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        //On listview item clcik
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EmployeeDetails employeeDetails = employeeDetailsList.get(position);

                if (parentActivity instanceof AdminMainActivity){
                    // Pass employee data to PerformanceFragment
                    AddEmployeeFragment fragment = new AddEmployeeFragment(employeeDetails);
                    Bundle args = new Bundle();
                    args.putInt("id",employeeDetails.getEmployee().getId());
//                args.putParcelable("employee_details", employeeDetailsScore);
                    fragment.setArguments(args);
                    AdminMainActivity adminMainActivity=(AdminMainActivity) getActivity();
                    TextView textView=adminMainActivity.findViewById(R.id.txt_top);
                    textView.setText("Employee");
                    adminMainActivity.replaceFragment(fragment);
                }
                else if(parentActivity instanceof DirectorMainActivity){
                    evaluationService.IsEvaluated(
                            sharedPreferencesManager.getEmployeeUserObject().getEmployee().getId(),
                            employeeDetails.getEmployee().getId(),
                            0,
                            sharedPreferencesManager.getSessionId(),
                            "director",
                            result -> {
                                if (result) {
                                    Toast.makeText(getContext(), "You have already Evaluated this employee", Toast.LENGTH_SHORT).show();
                                } else {
                                    FragmentUtils.replaceFragment(getParentFragmentManager(),new EvaluationQuestionnaireFragment(employeeDetails.getEmployee().getId(),"director", getId()), getId());
                                }
                            },errorMessage -> {
                                Toast.makeText(getContext(), "something went wrong while checking evaluation", Toast.LENGTH_SHORT).show();
                            });
                }
            }
        });
        return view;
    }
}