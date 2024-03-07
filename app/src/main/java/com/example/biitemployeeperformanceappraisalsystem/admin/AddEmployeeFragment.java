package com.example.biitemployeeperformanceappraisalsystem.admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.network.services.CommonData;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddEmployeeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEmployeeFragment extends Fragment {
    Spinner employeeTypeSpinner,designationSpinner,departmentSpinner;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddEmployeeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddEmployeeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddEmployeeFragment newInstance(String param1, String param2) {
        AddEmployeeFragment fragment = new AddEmployeeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_add_employee, container, false);

        employeeTypeSpinner = view.findViewById(R.id.spinner_employee_type);
        designationSpinner = view.findViewById(R.id.spinner_designation);
        departmentSpinner = view.findViewById(R.id.spinner_department);

        CommonData data=new CommonData(getContext());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, data.generateEmployeeTypes());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employeeTypeSpinner.setAdapter(adapter);

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, data.generateDepartments());
        departmentSpinner.setAdapter(adapter);

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, data.generateDesignations());
        designationSpinner.setAdapter(adapter);

        // Inflate the layout for this fragment
        return view;
    }
}