package com.example.biitemployeeperformanceappraisalsystem.helper;

import android.content.Context;
import android.graphics.Color;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.models.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CommonMethods {
    public static ArrayList<Integer> generateRandomColors(int count) {
        ArrayList<Integer> colors = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
            colors.add(color);
        }
        return colors;
    }

    public static boolean isSpinnerPopulated(Spinner spinner) {
        return spinner.getAdapter() != null && spinner.getAdapter().getCount() > 0;
    }
}
