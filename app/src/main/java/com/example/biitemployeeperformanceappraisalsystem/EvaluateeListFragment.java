package com.example.biitemployeeperformanceappraisalsystem;

import static com.example.biitemployeeperformanceappraisalsystem.helper.FragmentUtils.replaceFragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.director.DirectorMainActivity;
import com.example.biitemployeeperformanceappraisalsystem.faculty.FacultyMain;
import com.example.biitemployeeperformanceappraisalsystem.helper.SharedPreferencesManager;
import com.example.biitemployeeperformanceappraisalsystem.hod.HodMainActivity;
import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EvaluatorService;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EvaluateeListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EvaluateeListFragment extends Fragment {
    int evaluatorID, sessionID;
    String evaluationType;
    ListView listView;
    List<Employee> evaluateeList;
    List<String> evaluateesNames;
    EvaluatorService evaluatorService;
    SharedPreferencesManager sharedPreferencesManager;
    ArrayAdapter<String> adapter;
    private FrameLayout fragmentContainer;
    private FragmentActivity parentActivity;

    private int fragmentContainerId;

    public EvaluateeListFragment(int fragmentContainerId) {
        this.fragmentContainerId = fragmentContainerId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferencesManager = new SharedPreferencesManager(requireContext());
        evaluatorService = new EvaluatorService(requireContext());
        parentActivity = getActivity();
        evaluatorID = sharedPreferencesManager.getEmployeeUserObject().getEmployee().getId();
        sessionID = sharedPreferencesManager.getSessionId();
        evaluationType = sharedPreferencesManager.getEmployeeUserObject().getEmployeeType().getTitle();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_evaluatee_list, container, false);
        listView=view.findViewById(R.id.evaluatee_list_view);
        fragmentContainer = parentActivity.findViewById(R.id.fragment_container);

        evaluatorService.getEvaluatees(
                evaluatorID,
                sessionID,
                evaluatees -> {
                    evaluateeList = evaluatees;
                    evaluateesNames = new ArrayList<>();
                    for (Employee e:evaluatees) {
                        evaluateesNames.add(e.getName());
                    }
                    adapter = new ArrayAdapter<>(getContext(), R.layout.evaluatee_list_item_layout, evaluateesNames);
                    listView.setAdapter(adapter);
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Employee selectedEmployee = evaluateeList.get(position);
                int evaluateeID = selectedEmployee.getId();

                EvaluationQuestionnaireFragment fragment=new EvaluationQuestionnaireFragment(evaluateeID,0, "Peer", fragmentContainer.getId());

                replaceFragment(parentActivity.getSupportFragmentManager(),fragment,fragmentContainerId);

            }
        });
        // Inflate the layout for this fragment
        return view;
    }

}