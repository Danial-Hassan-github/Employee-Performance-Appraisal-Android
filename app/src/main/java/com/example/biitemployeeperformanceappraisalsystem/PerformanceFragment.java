package com.example.biitemployeeperformanceappraisalsystem;

import android.graphics.Color;
import android.os.Bundle;

import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.adapter.CoursePerformanceExpandableListAdapter;
import com.example.biitemployeeperformanceappraisalsystem.faculty.FacultyMain;
import com.example.biitemployeeperformanceappraisalsystem.helper.CommonMethods;
import com.example.biitemployeeperformanceappraisalsystem.models.Course;
import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeKpiScore;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeKpiScoreMultiSession;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeQuestionScore;
import com.example.biitemployeeperformanceappraisalsystem.models.Session;
import com.example.biitemployeeperformanceappraisalsystem.network.services.CommonData;
import com.example.biitemployeeperformanceappraisalsystem.network.services.CourseService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeCoursePerformanceService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeKpiScoreService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.SessionService;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerformanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerformanceFragment extends Fragment {
    Boolean isCoursePerformance = false;
    Boolean isComparison = false;
    int employeeID;
    Employee employee2;
    Session session;
    Course course;
    List<EmployeeKpiScore> employeeKpiScoreList;
    List<List<EmployeeKpiScore>> multiEmployeesKpiScoreList;
    List<EmployeeKpiScoreMultiSession> employeeKpiScoreMultiSessionList;
    EmployeeCoursePerformanceService employeeCoursePerformanceService;
    List<Employee> employeeList;
    List<Course> courseList;
    List<Session> sessionList;
    LinearLayout sessionLayout, comparisonSessionLayout, employeeLayout;
    TextView txtEmployeeName;
    PieChart pieChart;
    BarChart barChart;
    TabLayout tabLayout;
    Spinner sessionSpinner,fromSessionSpinner,toSessionSpinner,courseSpinner, employeeSpinner;
    CheckBox checkCoursePerformance;
    ExpandableListView expandableListView;
    CoursePerformanceExpandableListAdapter adapter;

    public PerformanceFragment(int employeeID){
        this.employeeID = employeeID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_performance, container, false);

        checkCoursePerformance = view.findViewById(R.id.check_course_performance);
        txtEmployeeName = view.findViewById(R.id.txt_employeee_name);
        pieChart = view.findViewById(R.id.pie_chart);
        barChart = view.findViewById(R.id.bar_chart);
        tabLayout = view.findViewById(R.id.performance_type_tab);
        employeeLayout = view.findViewById(R.id.employee_spinner_layout);
        sessionLayout = view.findViewById(R.id.session_spinner_layout);
        comparisonSessionLayout = view.findViewById(R.id.comparison_sessions_layout);
        expandableListView = view.findViewById(R.id.employee_course_questions_scores);

        if (getActivity() instanceof FacultyMain){
            tabLayout.setVisibility(View.GONE);
            txtEmployeeName.setVisibility(View.GONE);
        }

        adapter = new CoursePerformanceExpandableListAdapter(getContext(), new ArrayList<>(), new HashMap<>());
        expandableListView.setAdapter(adapter);

        pieChart.getDescription().setTextColor(Color.TRANSPARENT);
        barChart.getDescription().setTextColor(Color.TRANSPARENT);

        employeeSpinner = view.findViewById(R.id.spinner_employee);
        courseSpinner = view.findViewById(R.id.spinner_course);
        sessionSpinner = view.findViewById(R.id.spinner_session);
//        fromSessionSpinner = view.findViewById(R.id.spinner_session_from);
//        toSessionSpinner = view.findViewById(R.id.spinner_session_to);

        employeeCoursePerformanceService = new EmployeeCoursePerformanceService(getContext());
        EmployeeKpiScoreService employeeKpiScoreService = new EmployeeKpiScoreService(getContext());

        checkCoursePerformance.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                expandableListView.setVisibility(View.VISIBLE);
                pieChart.setVisibility(View.GONE);
                barChart.setVisibility(View.GONE);
                addDataToExpandableListView(isComparison);
                // sessionLayout.setVisibility(View.GONE);
                // comparisonSessionLayout.setVisibility(View.GONE);
                isCoursePerformance = true;
            } else {
                expandableListView.setVisibility(View.GONE);
                if (isComparison){
                    updateBarChart();
                }else {
                    updatePieChart();
                }
                isCoursePerformance = false;
                // sessionLayout.setVisibility(View.VISIBLE);
                // comparisonSessionLayout.setVisibility(View.VISIBLE);
            }
        });

        EmployeeService employeeService = new EmployeeService(getContext());
        employeeService.getEmployees(
                employees -> {
                    employeeList = employees;
                    employeeService.populateEmployeesSpinner(employeeList, employeeSpinner);
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
        );

        CourseService courseService = new CourseService(getContext());
        courseService.getCourses(
                courses -> {
                    courseList = courses;
                    List<String> coursesName = new ArrayList<>();
                    for (Course c: courseList) {
                        coursesName.add(c.getName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, coursesName);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    courseSpinner.setAdapter(adapter);
                },
                errorMessage -> {

                }
        );


        SessionService sessionService = new SessionService(view.getContext());
        sessionService.getSessions(sessions -> {
                    // Handle the list of sessions here
                    sessionList = sessions;
                    // Populate the spinner with session titles
                    sessionService.populateSpinner(sessions,sessionSpinner);
//                    sessionService.populateSpinner(sessions,fromSessionSpinner);
 //                   sessionService.populateSpinner(sessions,toSessionSpinner);
                },
                // onFailure callback
                errorMessage -> {
                    // Handle failure
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                });

        employeeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                employee2 = employeeList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                course = courseList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Set an item selected listener for the session spinner
        sessionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected session
                session = sessionList.get(position);
                updatePieChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case where nothing is selected
            }
        });

        // Set an item selected listener for the from_session spinner
//        fromSessionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                // Get the selected session
//                Session selectedSession = sessionList.get(position);
//                // Use the ID of the selected session
//                int sessionId = selectedSession.getId();
//                // Perform actions with the session ID
//                Toast.makeText(getContext(), sessionId+"", Toast.LENGTH_LONG).show();
//                updateBarChart();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // Handle case where nothing is selected
//            }
//        });
//
//        // Set an item selected listener for the to_session spinner
//        toSessionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                // Get the selected session
//                Session selectedSession = sessionList.get(position);
//                // Use the ID of the selected session
//                int sessionId = selectedSession.getId();
//                // Perform actions with the session ID
//                Toast.makeText(getContext(), sessionId+"", Toast.LENGTH_LONG).show();
//                updateBarChart();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // Handle case where nothing is selected
//            }
//        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position){
                    case 0:
                        isComparison = false;
                        if (isCoursePerformance){
                            addDataToExpandableListView(isComparison);
                        }else {
                            updatePieChart();
                        }
                        break;
                    case 1:
                        isComparison = true;
                        if (isCoursePerformance){
                            addDataToExpandableListView(isComparison);
                        }else {
                            updateBarChart();
                        }
                        break;

                    default:
                        break;
//                        ArrayList<BarEntry> group1 = new ArrayList<>();
//                        group1.add(new BarEntry(0, 27)); // Note the change in x-position
//                        group1.add(new BarEntry(3, 23));
//                        group1.add(new BarEntry(6, 35));
//
//                        ArrayList<BarEntry> group2 = new ArrayList<>();
//                        group2.add(new BarEntry(1, 32)); // Adjust the x-position to group the bars
//                        group2.add(new BarEntry(4, 26)); // Note the difference in x-position
//                        group2.add(new BarEntry(7, 40));
//
//                        ArrayList<BarEntry> group3 = new ArrayList<>();
//                        group3.add(new BarEntry(2, 28)); // Adjust the x-position to group the bars
//                        group3.add(new BarEntry(5, 30)); // Note the difference in x-position
//                        group3.add(new BarEntry(8, 35));
//
//                        BarDataSet barDataSet1 = new BarDataSet(group1, "Academic");
//                        barDataSet1.setColor(Color.rgb(0, 155, 0));
//
//                        BarDataSet barDataSet2 = new BarDataSet(group2, "Project");
//                        barDataSet2.setColor(Color.rgb(155, 0, 0));
//
//                        BarDataSet barDataSet3 = new BarDataSet(group3, "Punctuality");
//                        barDataSet3.setColor(Color.rgb(0, 0, 155));
//
//// Adjust the bar width and spacing
//                        float groupSpace = 0.2f; // space between groups of bars
//                        float barSpace = 0.02f; // space between individual bars within a group
//                        float barWidth = 0.15f; // width of each bar
//
//                        BarData barData = new BarData(barDataSet1, barDataSet2, barDataSet3);
//                        barData.setBarWidth(barWidth);
//
//                        // Group the bars
//                        barChart.setData(barData);
//                        barChart.groupBars(0, groupSpace, barSpace); // Grouped bars with space between groups
//                        barChart.invalidate();
//                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void updatePieChart() {
        barChart.setVisibility(View.GONE);
        employeeLayout.setVisibility(View.GONE);

        isComparison = false;

        pieChart.setVisibility(View.VISIBLE);
        sessionLayout.setVisibility(View.VISIBLE);
        EmployeeKpiScoreService employeeKpiScoreService = new EmployeeKpiScoreService(getContext());
        employeeKpiScoreService.getEmployeeKpiScore(
                employeeID,
                sessionList.get(sessionSpinner.getSelectedItemPosition()).getId(),
                employeeKpiScores -> {
                    employeeKpiScoreList = employeeKpiScores;
                    ArrayList<PieEntry> entries = new ArrayList<>();
                    float totalScore = 0;

                    // Calculate total score
                    for (EmployeeKpiScore e : employeeKpiScores) {
                        entries.add(new PieEntry(e.getScore(), e.getKpi_title() + "=" + e.getWeightage()));
                        totalScore += e.getScore();
                    }

                    // Calculate remaining percentage
                    float remainingPercentage = 100 - totalScore;
                    if (remainingPercentage > 0) {
                        // PieEntry remainingEntry = new PieEntry(remainingPercentage, "");
                        // remainingEntry.("");
                        entries.add(new PieEntry(remainingPercentage, ""));
                    }

                    // Create the PieDataSet
                    PieDataSet dataSet = new PieDataSet(entries, "");
                    dataSet.setValueTextSize(20f);

                    pieChart.setHoleRadius(10);
                    pieChart.setTransparentCircleRadius(10f + 5f);

                    // Generate colors dynamically
                    CommonMethods commonMethods = new CommonMethods();
                    ArrayList<Integer> colors = commonMethods.generateRandomColors(entries.size() - 1); // exclude the transparent entry
                    if (remainingPercentage > 0) {
                        colors.add(Color.TRANSPARENT); // Add transparent color for the remaining space
                    }
                    dataSet.setColors(colors);

                    // Set the data for the pie chart
                    PieData data = new PieData(dataSet);
                    pieChart.setData(data);
                    pieChart.invalidate();
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
        );
    }



    private void updateBarChart() {
        pieChart.setVisibility(View.GONE);
        sessionLayout.setVisibility(View.GONE);

        isComparison = true;
        barChart.setVisibility(View.VISIBLE);
        employeeLayout.setVisibility(View.VISIBLE);

        if (employeeSpinner.getAdapter().isEmpty() || sessionSpinner.getAdapter().isEmpty() || courseSpinner.getAdapter().isEmpty()){
            return;
        }

        try {
            // Define the labels for each group
            List<String> groupLabels = new ArrayList<>();

            EmployeeKpiScoreService employeeKpiScoreService = new EmployeeKpiScoreService(getContext());

            employeeKpiScoreService.compareEmployeeKpiScore(
                    employeeID,
                    employee2.getId(),
                    session.getId(),
                    employeeKpiScores -> {
                        // TODO
                        multiEmployeesKpiScoreList = employeeKpiScores;
                        List<String> kpiLabels = new ArrayList<>();
                        // Determine the maximum size of the groups
                        int maxSize = 0;
                        for (List<EmployeeKpiScore> scores : multiEmployeesKpiScoreList) {
                            int newSize = Math.max(maxSize, scores.size());
                            if (newSize > maxSize){
                                for (EmployeeKpiScore s:scores) {
                                    kpiLabels.add(s.getKpi_title());
                                }
                            }
                            maxSize = Math.max(maxSize, scores.size());
                        }

                        if (multiEmployeesKpiScoreList.size() > 0) {
                            ArrayList<Integer> colors = null;
                            CommonMethods commonMethods = new CommonMethods();
                            colors = commonMethods.generateRandomColors(maxSize);
                            int xCounter = 0;
                            int xInitialCounter = 1;
                            BarData barData = new BarData();

                            List<List<BarEntry>> groups = new ArrayList<>();

                            // Initialize the list of groups
                            for (int i = 0; i < maxSize; i++) {
                                groups.add(new ArrayList<>());
                            }

                            for (int i = 0; i < multiEmployeesKpiScoreList.size(); i++) {
                                List<EmployeeKpiScore> employeeKpiScore = multiEmployeesKpiScoreList.get(i);
                                ArrayList<BarEntry> group = new ArrayList<>();

                                String kpiTitle = null;

                                // Pad the list with null items if necessary
                                while (employeeKpiScore.size() < maxSize) {
                                    employeeKpiScore.add(new EmployeeKpiScore());
                                }

                                if (i == 0) {
                                    for (int c=0; c < employeeList.size(); c++){
                                        if (employeeID == employeeList.get(c).getId()){
                                            groupLabels.add(employeeList.get(c).getName());
                                        }
                                    }
                                    groupLabels.add(employee2.getName());
                                }

                                for (int k = 0; k < maxSize; k++) {
                                    groups.get(k).add(new BarEntry(xCounter, employeeKpiScore.get(k).getScore()));
                                    // group.add(new BarEntry(k , employeeKpiScore.get(k).getScore())); // Use 'k' as x-value for the BarEntry
                                    // kpiTitle = employeeKpiScore.get(k).getKpi_title();
                                    xCounter = xCounter+3;
                                }

                                // BarDataSet barDataSet = new BarDataSet(group, kpiTitle);
                                // barDataSet.setColors(colors);
                                // barData.addDataSet(barDataSet);

                                xCounter = xInitialCounter;
                                xInitialCounter++;
                            }


                            // Ensure there are at least 2 BarDataSets
                            if (barData.getDataSetCount() < 2) {
                                barData.addDataSet(new BarDataSet(new ArrayList<>(), "")); // Add an empty dataset if needed
                            }

                            // Create BarData and set data sets
                            BarData barData1 = new BarData();

                            // Create BarDataSet for each group
                            List<BarDataSet> dataSets = new ArrayList<>();
                            for (int i = 0; i < groups.size(); i++) {
                                BarDataSet barDataSet = new BarDataSet(groups.get(i), kpiLabels.get(i));
                                // CommonMethods commonMethods = new CommonMethods();
                                // ArrayList<Integer> colors = commonMethods.generateRandomColors(groups.get(i).size());
                                barDataSet.setColor(colors.get(i));
                                dataSets.add(barDataSet);
                                barData1.addDataSet(barDataSet);
                            }

                            float groupSpace = 0.3f; // space between groups of bars
                            float barSpace = 0.02f; // space between individual bars within a group
                            float barWidth = 0.15f; // width of each bar

                            barData1.setBarWidth(barWidth);
                            barChart.setData(barData1);

                            barChart.groupBars(0, groupSpace, barSpace); // Grouped bars with space between groups
                            barChart.invalidate();

                            // Set custom labels for the x-axis
                            XAxis xAxis = barChart.getXAxis();
                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                            xAxis.setLabelCount(groupLabels.size());
                            xAxis.setValueFormatter(new ValueFormatter() {
                                @Override
                                public String getFormattedValue(float value) {
                                    int index = (int) value;
                                    if (index >= 0 && index < groupLabels.size()) {
                                        String label = groupLabels.get(index);
                                        return label != null ? label : "Unknown";
                                    } else {
                                        return "";
                                    }
                                }
                            });
                        }
                    },
                    errorMessage -> {
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
            );
        }catch (Exception ex){
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


//    private void updateBarChart() {
//        pieChart.setVisibility(View.GONE);
//        // sessionLayout.setVisibility(View.GONE);
//
//        isComparison = true;
//        barChart.setVisibility(View.VISIBLE);
//        employeeLayout.setVisibility(View.VISIBLE);
//
//        // Define the labels for each group
//        List<String> groupLabels = new ArrayList<>();
//
//        EmployeeKpiScoreService employeeKpiScoreService = new EmployeeKpiScoreService(getContext());
//
//        employeeKpiScoreService.getEmployeeKpiScoreMultiSession(
//                employeeID,
//                9,
//                10,
//                employeeKpiScoreMultiSessions -> {
//                    BarData barData = new BarData();
//                    employeeKpiScoreMultiSessionList = employeeKpiScoreMultiSessions;
//                    List<ArrayList<BarEntry>> groups = new ArrayList<>(employeeKpiScoreMultiSessionList.size());
//                    if (employeeKpiScoreMultiSessionList.size() > 0) {
//                        int xCounter = 0;
//                        int xInitialCounter = 1;
//                        for (int i = 0; i < employeeKpiScoreMultiSessionList.size(); i++) {
//                            EmployeeKpiScoreMultiSession employeeKpiScoreMultiSession = employeeKpiScoreMultiSessionList.get(i);
//                            ArrayList<BarEntry> group = new ArrayList<>();
//                            List<EmployeeKpiScore> scores = employeeKpiScoreMultiSession.getScores();
//                            groupLabels.add(employeeKpiScoreMultiSession.getSession().getTitle());
//                            String kpiTitle = null;
//                            for (int k = 0; k < scores.size(); k++) {
//                                group.add(new BarEntry(xCounter, scores.get(k).getScore()));
//                                xCounter = xCounter + 3;
//                                kpiTitle = scores.get(k).getKpi_title();
//                            }
//                            xCounter = xInitialCounter;
//                            xInitialCounter++;
//                            BarDataSet barDataSet = new BarDataSet(group, kpiTitle);
//                            CommonMethods commonMethods = new CommonMethods();
//                            ArrayList<Integer> colors = commonMethods.generateRandomColors(employeeKpiScoreMultiSessionList.size());
//                            barDataSet.setColor(colors.get(i));
//                            barData.addDataSet(barDataSet);
//                        }
//
//                        // Ensure there are at least 2 BarDataSets
//                        if (barData.getDataSetCount() < 2) {
//                            barData.addDataSet(new BarDataSet(new ArrayList<>(), "")); // Add an empty dataset if needed
//                        }
//
//                        float groupSpace = 0.2f; // space between groups of bars
//                        float barSpace = 0.02f; // space between individual bars within a group
//                        float barWidth = 0.15f; // width of each bar
//
//                        barData.setBarWidth(barWidth);
//                        barChart.setData(barData);
//
//                        barChart.groupBars(0, groupSpace, barSpace); // Grouped bars with space between groups
//                        barChart.invalidate();
//
//                        // Set custom labels for the x-axis
//                        XAxis xAxis = barChart.getXAxis();
//                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//                        xAxis.setLabelCount(groupLabels.size());
//                        xAxis.setValueFormatter(new ValueFormatter() {
//                            @Override
//                            public String getFormattedValue(float value) {
//                                int index = (int) value;
//                                if (index >= 0 && index < groupLabels.size()) {
//                                    return groupLabels.get(index);
//                                } else {
//                                    return "";
//                                }
//                            }
//                        });
//                    }
//                },
//                errorMessage -> {
//                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
//                });
//    }

    // Method to add data to the expandable list view
    private void addDataToExpandableListView(Boolean isComparison) {
        List<String> groupList = new ArrayList<>();
        HashMap<String, List<String>> childList = new HashMap<>();

        if (employeeSpinner.getAdapter().isEmpty() || sessionSpinner.getAdapter().isEmpty() || courseSpinner.getAdapter().isEmpty()){
            return;
        }

        try {
            // for single employee
            if (!isComparison){
                employeeCoursePerformanceService.getEmployeeCoursePerformance(
                        employeeID,
                        session.getId(),
                        course.getId(),
                        employeeCourseScore -> {
                            // Correctly adding group names
                            groupList.add(employeeCourseScore.getEmployee().getName()+", "+session.getTitle()+", "+course.getName()+", "+employeeCourseScore.getAverage()+"%");
                            List<String> childList1 = new ArrayList<>();
                            List<String> childList2 = new ArrayList<>();

                            List<EmployeeQuestionScore> employeeQuestionScores = employeeCourseScore.getEmployeeQuestionScores();
                            for (EmployeeQuestionScore e: employeeQuestionScores) {
                                childList1.add(e.getQuestion().getQuestion()+"\n"+e.getObtainedScore()+"/"+e.getTotalScore());
                            }

                            // Mapping children to groups
                            childList.put(employeeCourseScore.getEmployee().getName()+", "+session.getTitle()+", "+course.getName()+", "+employeeCourseScore.getAverage()+"%", childList1);
                            adapter.updateData(groupList, childList);
                        },
                        errorMessage -> {
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }
                );
            }

            // for comparison
            if (isComparison){
                employeeLayout.setVisibility(View.VISIBLE);
                employeeCoursePerformanceService.compareEmployeeCoursePerformance(
                        employeeID,
                        employee2.getId(),
                        session.getId(),
                        course.getId(),
                        employeeCourseScores -> {
                            // Correctly adding group names
                            groupList.add(employeeCourseScores.get(0).getEmployee().getName()+", "+session.getTitle()+", "+course.getName()+", "+ employeeCourseScores.get(0).getAverage()+"%");
                            groupList.add(employeeCourseScores.get(1).getEmployee().getName()+", "+session.getTitle()+", "+course.getName()+", "+ employeeCourseScores.get(1).getAverage()+"%");
                            List<String> childList1 = new ArrayList<>();
                            List<String> childList2 = new ArrayList<>();

                            List<EmployeeQuestionScore> employeeQuestionScores1 = employeeCourseScores.get(0).getEmployeeQuestionScores();
                            for (EmployeeQuestionScore e: employeeQuestionScores1) {
                                childList1.add(e.getQuestion().getQuestion()+"\n"+e.getObtainedScore()+"/"+e.getTotalScore());
                            }

                            List<EmployeeQuestionScore> employeeQuestionScores2 = employeeCourseScores.get(1).getEmployeeQuestionScores();
                            for (EmployeeQuestionScore e: employeeQuestionScores2) {
                                childList2.add(e.getQuestion().getQuestion()+"\n"+e.getObtainedScore()+"/"+e.getTotalScore());
                            }

                            // Mapping children to groups
                            childList.put(employeeCourseScores.get(0).getEmployee().getName()+", "+session.getTitle()+", "+course.getName()+", "+ employeeCourseScores.get(0).getAverage()+"%", childList1);
                            childList.put(employeeCourseScores.get(1).getEmployee().getName()+", "+session.getTitle()+", "+course.getName()+", "+ employeeCourseScores.get(1).getAverage()+"%", childList2);
                            adapter.updateData(groupList, childList);
                        },
                        errorMessage -> {
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }
                );
            }
        }catch (Exception ex){
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}