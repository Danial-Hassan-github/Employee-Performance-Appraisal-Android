package com.example.biitemployeeperformanceappraisalsystem.director;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.biitemployeeperformanceappraisalsystem.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateKpiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateKpiFragment extends Fragment {
    public UpdateKpiFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static UpdateKpiFragment newInstance(String param1, String param2) {
        UpdateKpiFragment fragment = new UpdateKpiFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_kpi, container, false);
        return view;
    }
}