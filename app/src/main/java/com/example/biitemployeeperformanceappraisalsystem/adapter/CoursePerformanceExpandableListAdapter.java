package com.example.biitemployeeperformanceappraisalsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.biitemployeeperformanceappraisalsystem.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CoursePerformanceExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> groupList;
    private HashMap<String, List<String>> childHashMap;

    public CoursePerformanceExpandableListAdapter(Context context, List<String> groupList, HashMap<String, List<String>> childHashMap) {
        this.context = context;
        this.groupList = groupList != null ? groupList : new ArrayList<>();
        this.childHashMap = childHashMap != null ? childHashMap : new HashMap<>();
    }

    public void updateData(List<String> newGroupList, HashMap<String, List<String>> newChildList) {
        this.groupList.clear();
        if (newGroupList != null) {
            this.groupList.addAll(newGroupList);
        }

        this.childHashMap.clear();
        if (newChildList != null) {
            this.childHashMap.putAll(newChildList);
        }

        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<String> children = childHashMap.get(groupList.get(groupPosition));
        return children != null ? children.size() : 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<String> children = childHashMap.get(groupList.get(groupPosition));
        return children != null ? children.get(childPosition) : null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_item_layout, parent, false);
        }

        TextView nameTextView = convertView.findViewById(R.id.nameTextView);
        TextView sessionTitleTextView = convertView.findViewById(R.id.sessionTitleTextView);
        TextView courseNameTextView = convertView.findViewById(R.id.courseNameTextView);
        TextView scoreTextView = convertView.findViewById(R.id.scoreTextView);

        String groupItem = groupList.get(groupPosition);
        String[] groupItemParts = groupItem.split(", ");
        if (groupItemParts.length == 4) {
            nameTextView.setText(groupItemParts[0]);
            sessionTitleTextView.setText(groupItemParts[1]);
            courseNameTextView.setText(groupItemParts[2]);
            scoreTextView.setText(groupItemParts[3]);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.child_item_layout, parent, false);
        }

        TextView childTextView = convertView.findViewById(R.id.subItemTextView);
        String childText = (String) getChild(groupPosition, childPosition);
        childTextView.setText(childText);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
