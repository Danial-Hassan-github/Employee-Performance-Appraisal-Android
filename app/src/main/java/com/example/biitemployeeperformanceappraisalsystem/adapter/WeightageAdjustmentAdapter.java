package com.example.biitemployeeperformanceappraisalsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.models.KPI;
import com.example.biitemployeeperformanceappraisalsystem.models.Question;

import java.util.List;

public class WeightageAdjustmentAdapter extends ArrayAdapter<KPI> {
    private LayoutInflater inflater;
    private int resourceId;
    public WeightageAdjustmentAdapter(Context context, int resourceId, List<KPI> kpiList){
        super(context, resourceId, kpiList);
        this.inflater = LayoutInflater.from(context);
        this.resourceId = resourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        KPI kpi=getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.kpi_weightage_adjustment_list_item_layout, parent, false);
        }
        TextView kpiTextView = convertView.findViewById(R.id.txt_kpi_title);
        EditText KpiWeightage = convertView.findViewById(R.id.text_kpi_weightage);

        kpiTextView.setText(kpi.getName().toString());
        //KpiWeightage.setText(kpi.);

        return super.getView(position, convertView, parent);
    }
}
