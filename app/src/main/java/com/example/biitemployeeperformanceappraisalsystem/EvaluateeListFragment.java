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
import com.example.biitemployeeperformanceappraisalsystem.faculty.TeacherCoursesFragment;
import com.example.biitemployeeperformanceappraisalsystem.helper.SharedPreferencesManager;
import com.example.biitemployeeperformanceappraisalsystem.hod.HodMainActivity;
import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.models.TeacherJunior;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EvaluatorService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.JuniorEmployeeService;
import com.google.android.material.tabs.TabLayout;

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
    ListView evaluateesListView, juniorsListView;
    List<Employee> evaluateeList;
    List<TeacherJunior> teacherJuniorList;
    List<String> evaluateesNames;
    List<String> juniorsWithCourseName;
    EvaluatorService evaluatorService;
    JuniorEmployeeService juniorEmployeeService;
    SharedPreferencesManager sharedPreferencesManager;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapterJuniors;
    TabLayout tabLayout;
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
        juniorEmployeeService = new JuniorEmployeeService(requireContext());
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
        juniorsListView = view.findViewById(R.id.juniors_list_view);
        evaluateesListView = view.findViewById(R.id.evaluatee_list_view);
        tabLayout = view.findViewById(R.id.evaluatee_list_fragment_tabLayout);
        fragmentContainer = parentActivity.findViewById(R.id.fragment_container);

        showPeers();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position){
                    case 0:
                        evaluateesListView.setVisibility(View.VISIBLE);
                        juniorsListView.setVisibility(View.GONE);
                        showPeers();
                        break;
                    case 1:
                        juniorsListView.setVisibility(View.VISIBLE);
                        evaluateesListView.setVisibility(View.GONE);
                        showJuniors();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        juniorsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TeacherJunior selectedTeacherJunior = teacherJuniorList.get(position);
                int evaluateeID = selectedTeacherJunior.getEmployee().getId();
                int courseID = selectedTeacherJunior.getCourse().getId();

                EvaluationQuestionnaireFragment fragment=new EvaluationQuestionnaireFragment(evaluateeID, courseID,"Senior", fragmentContainer.getId());

                replaceFragment(parentActivity.getSupportFragmentManager(),fragment,fragmentContainerId);

            }
        });

        evaluateesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Employee selectedEmployee = evaluateeList.get(position);
                int evaluateeID = selectedEmployee.getId();

                TeacherCoursesFragment fragment=new TeacherCoursesFragment(evaluateeID);

                replaceFragment(parentActivity.getSupportFragmentManager(),fragment,fragmentContainerId);

            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    public void showJuniors(){
        juniorEmployeeService.getTeacherJuniors(
                evaluatorID,
                sessionID,
                teacherJuniors -> {
                    teacherJuniorList = teacherJuniors;
                    if (teacherJuniors != null){
                        juniorsWithCourseName = new ArrayList<>();
                        for (TeacherJunior t:teacherJuniors){
                            juniorsWithCourseName.add(t.getEmployee().getName()+"\n"+t.getCourse().getName());
                        }
                        adapterJuniors = new ArrayAdapter<>(getContext(), R.layout.evaluatee_list_item_layout, juniorsWithCourseName);
                        juniorsListView.setAdapter(adapterJuniors);
                    }
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
        );
    }

    public void showPeers(){
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
                    evaluateesListView.setAdapter(adapter);
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                });
    }
}