package com.example.biitemployeeperformanceappraisalsystem.director;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.biitemployeeperformanceappraisalsystem.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KpiWeightageAdjustmentFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KpiWeightageAdjustmentFormFragment extends Fragment {

    ListView kpiWeightageListView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_kpi_weightage_adjustment_form, container, false);
        // Inflate the layout for this fragment
        return view;
    }
}