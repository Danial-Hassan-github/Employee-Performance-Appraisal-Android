package com.example.biitemployeeperformanceappraisalsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.models.Session;

import java.util.ArrayList;
import java.util.List;

public class CustomSessionSpinner extends ArrayAdapter<Session> {

    public interface OnItemSelectionChangedListener {
        void onItemSelectionChanged(List<Integer> selectedEmployeeIds);
    }

    private List<Integer> session_ids;
    private List<Session> sessions;
    private List<Boolean> itemChecked;
    private LayoutInflater inflater;
    private boolean selectAllChecked = false;
    private OnItemSelectionChangedListener listener;

    public CustomSessionSpinner(@NonNull Context context, int resource, @NonNull List<Session> sessions) {
        super(context, resource, sessions);
        this.sessions = sessions;
        this.session_ids = new ArrayList<>();
        this.itemChecked = new ArrayList<>();
        for (int i = 0; i < sessions.size(); i++) {
            itemChecked.add(false);
        }
        inflater = LayoutInflater.from(context);
    }

    public void setOnItemSelectionChangedListener(OnItemSelectionChangedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        final ViewHolder viewHolder;

        if (view == null) {
            view = inflater.inflate(R.layout.custom_spinner_item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.checkbox = view.findViewById(R.id.checkbox);
            viewHolder.text = view.findViewById(R.id.text);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final Session session = sessions.get(position);
        viewHolder.text.setText(session.getTitle());

        viewHolder.checkbox.setOnCheckedChangeListener(null); // Clear previous listener

        if (position == 0) {
            viewHolder.checkbox.setChecked(selectAllChecked);
        } else {
            viewHolder.checkbox.setChecked(itemChecked.get(position));
        }

        viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (position == 0) {
                    selectAllChecked = isChecked;
                    for (int i = 1; i < itemChecked.size(); i++) {
                        itemChecked.set(i, isChecked);
                        session_ids.add(sessions.get(i).getId());
                    }
                    notifyDataSetChanged();
                } else {
                    itemChecked.set(position, isChecked);
                    if (isChecked) {
                        session_ids.add(session.getId());
                    } else {
                        session_ids.remove((Integer) session.getId());
                    }
                }
                if (listener != null) {
                    listener.onItemSelectionChanged(new ArrayList<>(session_ids));
                }
            }
        });

        return view;
    }

    private static class ViewHolder {
        CheckBox checkbox;
        TextView text;
    }

    public List<Integer> getSelectedSessionIds() {
        return new ArrayList<>(session_ids);
    }
}
