package com.example.biitemployeeperformanceappraisalsystem;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.biitemployeeperformanceappraisalsystem.director.DirectorMainActivity;
import com.example.biitemployeeperformanceappraisalsystem.faculty.FacultyMain;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EvaluateeListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EvaluateeListFragment extends Fragment {
    ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_evaluatee_list, container, false);
        listView=view.findViewById(R.id.evaluatee_list_view);

        String[] namesArray = {"Umar Farooq", "Waseem", "Qasim Shehzad", "Amir Rasheed", "Shahid Abid"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.evaluatee_list_item_layout, namesArray);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EvaluationQuestionnaireFragment fragment=new EvaluationQuestionnaireFragment();
                FacultyMain facultyMainActivity = (FacultyMain) getActivity();
                facultyMainActivity.replaceFragment(fragment);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

}