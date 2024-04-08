package com.example.biitemployeeperformanceappraisalsystem.helper;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class FragmentUtils {

    public static void replaceFragment(FragmentManager fragmentManager, Fragment fragment, int containerViewId) {
        fragmentManager.beginTransaction()
                .replace(containerViewId, fragment)
                .addToBackStack(null)
                .commit();
    }
}
