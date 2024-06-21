package com.example.biitemployeeperformanceappraisalsystem.admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.helper.FragmentUtils;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminSettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminSettingFragment extends Fragment {
    TabLayout tabLayout;
    private FrameLayout fragmentContainer;

    public AdminSettingFragment() {
        // Required empty public constructor
    }

    public static AdminSettingFragment newInstance(String param1, String param2) {
        AdminSettingFragment fragment = new AdminSettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Handle fragment arguments if any
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_setting, container, false);
        tabLayout = view.findViewById(R.id.admin_setting_tab_layout);
        fragmentContainer = view.findViewById(R.id.fragment_container);

        // Set default fragment
        FragmentUtils.replaceFragment(getChildFragmentManager(), new StudentEvaluationSettingFragment(), fragmentContainer.getId());

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        FragmentUtils.replaceFragment(getChildFragmentManager(), new StudentEvaluationSettingFragment(), fragmentContainer.getId());
                        break;
                    case 1:
                        FragmentUtils.replaceFragment(getChildFragmentManager(), new SelectStudentFragment(), fragmentContainer.getId());
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Handle tab unselected state if needed
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Handle tab reselected state if needed
            }
        });
        return view;
    }
}
