package com.example.biitemployeeperformanceappraisalsystem;

import android.graphics.Color;
import android.os.Bundle;

import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;

import android.util.Log;
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
import com.example.biitemployeeperformanceappraisalsystem.adapter.CustomSpinnerAdapter;
import com.example.biitemployeeperformanceappraisalsystem.faculty.FacultyMain;
import com.example.biitemployeeperformanceappraisalsystem.helper.CommonMethods;
import com.example.biitemployeeperformanceappraisalsystem.helper.SharedPreferencesManager;
import com.example.biitemployeeperformanceappraisalsystem.hod.HodMainActivity;
import com.example.biitemployeeperformanceappraisalsystem.models.Course;
import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeCourseScore;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeCoursesPerformanceRequest;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeIdsWithSession;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeKpiScore;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeKpiScoreMultiSession;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeQuestionScore;
import com.example.biitemployeeperformanceappraisalsystem.models.MultiEmployeeCoursePerformanceRequest;
import com.example.biitemployeeperformanceappraisalsystem.models.Session;
import com.example.biitemployeeperformanceappraisalsystem.network.services.CommonData;
import com.example.biitemployeeperformanceappraisalsystem.network.services.CourseService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeCoursePerformanceService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeKpiScoreService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.SessionService;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.tabs.TabItem;
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
    SharedPreferencesManager sharedPreferencesManager;
    Boolean isComparison = false;
    Boolean isCourseComparison = false;
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
    LinearLayout sessionLayout, comparisonSessionLayout, employeeLayout, courseLayout;
    CustomSpinnerAdapter customSpinnerAdapter;
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

//        checkCoursePerformance = view.findViewById(R.id.check_course_performance);
        txtEmployeeName = view.findViewById(R.id.txt_employeee_name);
        pieChart = view.findViewById(R.id.pie_chart);
        barChart = view.findViewById(R.id.bar_chart);
        tabLayout = view.findViewById(R.id.performance_type_tab);
        employeeLayout = view.findViewById(R.id.employee_spinner_layout);
        sessionLayout = view.findViewById(R.id.session_spinner_layout);
        courseLayout = view.findViewById(R.id.course_spinner_layout);
        comparisonSessionLayout = view.findViewById(R.id.comparison_sessions_layout);
        expandableListView = view.findViewById(R.id.employee_course_questions_scores);
        sharedPreferencesManager = new SharedPreferencesManager(getContext());
        session = new Session();
        session.setId(sharedPreferencesManager.getSessionId());

        if (getActivity() instanceof FacultyMain || getActivity() instanceof HodMainActivity){
            // tabLayout.setVisibility(View.GONE);
            tabLayout.getTabAt(1).view.setVisibility(View.GONE);
            tabLayout.getTabAt(3).view.setVisibility(View.GONE);
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

        EmployeeService employeeService = new EmployeeService(getContext());
        employeeService.getEmployees(
                employees -> {
                    employeeList = employees;
                    // employeeService.populateEmployeesSpinner(employeeList, employeeSpinner);
                    updateEvaluateeSpinnerContents();

                    // employeeService.populateEmployeesSpinner(employeeList, employeeSpinner);
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
        );

        CourseService courseService = new CourseService(getContext());
        courseService.getTeacherCourses(
                employeeID,
                session.getId(),
                courses -> {
                    courseList = courses;
                    course = courseList.get(courseList.size()-1);
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

//        employeeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                employeeService.populateEmployeesSpinner(employeeList, employeeSpinner);
//                updateEvaluateeSpinnerContents();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

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
                // updateEvaluateeSpinnerContents();
                if (isComparison){
                    if (isCourseComparison)
                        updateCoursePerformanceComparisonBarChart();
                    else
                        updateGroupBarChart();
                }else {
                    if (isCourseComparison)
                        updateCoursesPerformanceBarChart();
                    else
                        updateBarChart();
                }
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
                        updateBarChart();
                        break;
                    case 1:
                        isComparison = true;
                        updateGroupBarChart();
                        break;
                    case 2:
                        isComparison = false;
                        isCourseComparison = false;
                        updateCoursesPerformanceBarChart();
                        break;
                    case 3:
                        isComparison = true;
                        isCourseComparison = true;
                        updateCoursePerformanceComparisonBarChart();
                        break;
                    default:
                        break;
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

    private void updateBarChart() {
        // pieChart.setVisibility(View.GONE);
        employeeLayout.setVisibility(View.GONE);

        // barChart.setVisibility(View.VISIBLE);
        sessionLayout.setVisibility(View.VISIBLE);
        courseLayout.setVisibility(View.GONE);

        EmployeeKpiScoreService employeeKpiScoreService = new EmployeeKpiScoreService(getContext());
        employeeKpiScoreService.getEmployeeKpiScore(
                employeeID,
                sessionList.get(sessionSpinner.getSelectedItemPosition()).getId(),
                employeeKpiScores -> {
                    employeeKpiScoreList = employeeKpiScores;
                    ArrayList<BarEntry> entries = new ArrayList<>();
                    ArrayList<String> kpiTitles = new ArrayList<>();
                    int index = 0;

                    for (EmployeeKpiScore e : employeeKpiScores) {
                        BarEntry barEntry = new BarEntry(index++, e.getScore());
                        entries.add(barEntry);
                        kpiTitles.add(e.getKpi_title()+e.getWeightage()+"%");
                    }

                    // Create the BarDataSet
                    BarDataSet dataSet = new BarDataSet(entries, "");
                    dataSet.setValueTextSize(15f);

                    // Generate colors dynamically
                    CommonMethods commonMethods = new CommonMethods();
                    ArrayList<Integer> colors = commonMethods.generateRandomColors(entries.size());
                    dataSet.setColors(colors);

                    // Create the BarData
                    BarData data = new BarData(dataSet);
                    data.setBarWidth(0.2f); // set custom bar width

                    // Set the data for the bar chart
                    barChart.setData(data);
                    barChart.setFitBars(true); // make the x-axis fit exactly all bars

                    // Set up the x-axis labels
                    XAxis xAxis = barChart.getXAxis();
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(kpiTitles));
                    xAxis.setGranularity(1f); // only intervals of 1
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                    // Refresh the chart
                    barChart.notifyDataSetChanged();
                    barChart.invalidate();
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
        );
    }

    private void updateCoursesPerformanceBarChart(){
        // pieChart.setVisibility(View.GONE);
        sessionLayout.setVisibility(View.VISIBLE);

        // barChart.setVisibility(View.VISIBLE);
        employeeLayout.setVisibility(View.GONE);
        courseLayout.setVisibility(View.GONE);

        try {
            List<Integer> coursesIds = new ArrayList<>();
            for (Course c: courseList) {
                coursesIds.add(c.getId());
            }
            EmployeeCoursesPerformanceRequest employeeCoursesPerformanceRequest = new EmployeeCoursesPerformanceRequest(employeeID, session.getId(), coursesIds);
            employeeCoursePerformanceService.getEmployeeCoursesPerformance(
                    employeeCoursesPerformanceRequest,
                    employeeCourseScores -> {
                        // employeeKpiScoreList = employeeKpiScores;
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        ArrayList<String> kpiTitles = new ArrayList<>();
                        int index = 0;

                        for (EmployeeCourseScore e : employeeCourseScores) {
                            BarEntry barEntry = new BarEntry(index++, (float) e.getAverage());
                            entries.add(barEntry);
                            kpiTitles.add(e.getCourse().getName());
                        }

                        // Create the BarDataSet
                        BarDataSet dataSet = new BarDataSet(entries, "");
                        dataSet.setValueTextSize(15f);

                        // Generate colors dynamically
                        CommonMethods commonMethods = new CommonMethods();
                        ArrayList<Integer> colors = commonMethods.generateRandomColors(entries.size());
                        dataSet.setColors(colors);

                        // Create the BarData
                        BarData data = new BarData(dataSet);
                        data.setBarWidth(0.2f); // set custom bar width

                        // Set the data for the bar chart
                        barChart.setData(data);
                        barChart.setFitBars(true); // make the x-axis fit exactly all bars

                        // Set up the x-axis labels
                        XAxis xAxis = barChart.getXAxis();
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(kpiTitles));
                        xAxis.setGranularity(1f); // only intervals of 1
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                        // Refresh the chart
                        barChart.notifyDataSetChanged();
                        barChart.invalidate();
                    },
                    errorMessage -> {
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
            );
        }catch (Exception ex){
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void updateCoursePerformanceComparisonBarChart(){
        // pieChart.setVisibility(View.GONE);
        sessionLayout.setVisibility(View.VISIBLE);

        // barChart.setVisibility(View.VISIBLE);
        employeeLayout.setVisibility(View.VISIBLE);
        courseLayout.setVisibility(View.VISIBLE);
        try {
            // Define the labels for each group
            List<String> groupLabels = new ArrayList<>();
            ArrayList<Integer> employeeIds = new ArrayList<>();

            employeeIds.add(employeeID);
            // Log.i("",customSpinnerAdapter.getSelectedEmployeeIds().get(0).toString());
            employeeIds.addAll(customSpinnerAdapter.getSelectedEmployeeIds());
            employeeIds.remove(new int[]{0});
            // employeeIds.add(employee2.getId());

            MultiEmployeeCoursePerformanceRequest multiEmployeeCoursePerformanceRequest = new MultiEmployeeCoursePerformanceRequest(course.getId(), session.getId(), employeeIds);

            employeeCoursePerformanceService.getMultiEmployeeCoursePerformance(
                    multiEmployeeCoursePerformanceRequest,
                    employeeCourseScores -> {
                        // TODO
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        ArrayList<String> kpiTitles = new ArrayList<>();
                        int index = 0;

                        for (EmployeeCourseScore e : employeeCourseScores) {
                            BarEntry barEntry = new BarEntry(index++, (float) e.getAverage());
                            entries.add(barEntry);
                            kpiTitles.add(e.getEmployee().getName());
                        }

                        // Create the BarDataSet
                        BarDataSet dataSet = new BarDataSet(entries, "");
                        dataSet.setValueTextSize(15f);

                        // Generate colors dynamically
                        CommonMethods commonMethods = new CommonMethods();
                        ArrayList<Integer> colors = commonMethods.generateRandomColors(entries.size());
                        dataSet.setColors(colors);

                        // Create the BarData
                        BarData data = new BarData(dataSet);
                        data.setBarWidth(0.2f); // set custom bar width

                        // Set the data for the bar chart
                        barChart.setData(data);
                        barChart.setFitBars(true); // make the x-axis fit exactly all bars

                        // Set up the x-axis labels
                        XAxis xAxis = barChart.getXAxis();
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(kpiTitles));
                        xAxis.setGranularity(1f); // only intervals of 1
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                        // Refresh the chart
                        barChart.notifyDataSetChanged();
                        barChart.invalidate();
                    },
                    errorMessage -> {
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
            );
        }catch (Exception ex){
            Log.e("","", ex);
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateGroupBarChart() {
        // pieChart.setVisibility(View.GONE);
        sessionLayout.setVisibility(View.VISIBLE);

        // barChart.setVisibility(View.VISIBLE);
        employeeLayout.setVisibility(View.VISIBLE);
        courseLayout.setVisibility(View.GONE);

//        if (employeeSpinner.getAdapter().isEmpty() || sessionSpinner.getAdapter().isEmpty() || courseSpinner.getAdapter().isEmpty()){
//            return;
//        }

        try {
            // Define the labels for each group
            List<String> groupLabels = new ArrayList<>();
            ArrayList<Integer> employeeIds = new ArrayList<>();

            employeeIds.add(employeeID);
            // Log.i("",customSpinnerAdapter.getSelectedEmployeeIds().get(0).toString());
            employeeIds.addAll(customSpinnerAdapter.getSelectedEmployeeIds());
            employeeIds.remove(new int[]{0});
            // employeeIds.add(employee2.getId());

            EmployeeKpiScoreService employeeKpiScoreService = new EmployeeKpiScoreService(getContext());
            EmployeeIdsWithSession employeeIdsWithSession = new EmployeeIdsWithSession();
            employeeIdsWithSession.setEmployeeIds(employeeIds);
            employeeIdsWithSession.setSession_id(session.getId());

            employeeKpiScoreService.compareEmployeeKpiScore(
                    employeeIdsWithSession,
                    employeeKpiScores -> {
                        // TODO
                        try {
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
                                        for (int j=0; j<customSpinnerAdapter.getSelectedEmployeeIds().size();j++){
                                            groupLabels.add(employeeList.get(j).getName());
                                        }
                                        // groupLabels.add(employee2.getName());
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

                                // Ensure there are at least 2 BarDataSets
                                if (barData1.getDataSetCount() < 2) {
                                    barData1.addDataSet(new BarDataSet(new ArrayList<>(), "")); // Add an empty dataset if needed
                                }
                                // Ensure there are at least 2 BarDataSets
                                if (barData1.getDataSetCount() < 1) {
                                    barData1.addDataSet(new BarDataSet(new ArrayList<>(), "")); // Add an empty dataset if needed
                                    barData1.addDataSet(new BarDataSet(new ArrayList<>(), "")); // Add an empty dataset if needed
                                }

                                float groupSpace = 0.5f; // space between groups of bars
                                float barSpace = 0.02f; // space between individual bars within a group
                                float barWidth = 0.2f; // width of each bar

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
                        }catch (Exception ex){
                            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    },
                    errorMessage -> {
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
            );
        }catch (Exception ex){
            Log.e("","", ex);
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateEvaluateeSpinnerContents() {
        // Create a new list to hold the updated items for the evaluatee spinner
         List<Employee> updatedEvaluateeList = new ArrayList<>();

        // Add the default "Select All" item to the list
        Employee employee = new Employee();
        employee.setName("Select All");
        updatedEvaluateeList.add(employee);

        // Add all employees to the list initially
         updatedEvaluateeList.addAll(getUpdatedEmployeeList(employeeID));

        // Update the evaluatee spinner with the updated list
        customSpinnerAdapter = new CustomSpinnerAdapter(getContext(), R.layout.custom_spinner_item_layout, updatedEvaluateeList);
        employeeSpinner.setAdapter(customSpinnerAdapter);

        customSpinnerAdapter.setOnItemSelectionChangedListener(new CustomSpinnerAdapter.OnItemSelectionChangedListener() {
            @Override
            public void onItemSelectionChanged(List<Integer> selectedEmployeeIds) {
                if (isCourseComparison)
                    updateCoursePerformanceComparisonBarChart();
                else
                    updateGroupBarChart();
            }
        });

    }

    private List<Employee> getUpdatedEmployeeList(int evaluatorId) {
        List<Employee> updatedEmployeeList = new ArrayList<>();
        for (Employee employee : employeeList) {
            if (employee.getId() != evaluatorId) {
                updatedEmployeeList.add(employee);
            }
        }
        return updatedEmployeeList;
    }
}