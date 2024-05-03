package com.example.biitemployeeperformanceappraisalsystem.director;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddKpiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddKpiFragment extends Fragment {
    private TabLayout tabLayout;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddKpiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddKpiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddKpiFragment newInstance(String param1, String param2) {
        AddKpiFragment fragment = new AddKpiFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_kpi, container, false);

        tabLayout = rootView.findViewById(R.id.tab_layout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Handle tab selection
                replaceFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Do nothing
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Do nothing
            }
        });

        // Initially, replace the fragment with the first tab
        replaceFragment(0);

        return rootView;
    }

    private void replaceFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new AddGeneralKpiFragment();
                break;
            case 1:
                fragment = new AddGroupKpiFragment();
                break;
            case 2:
                fragment = new AddIndividualKpiFragment();
                break;
            // Add more cases for additional tabs
        }
        if (fragment != null) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.child_fragment_container, fragment)
                    .commit();
        }
    }
}