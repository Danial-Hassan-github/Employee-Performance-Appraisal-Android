package com.example.biitemployeeperformanceappraisalsystem.director;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.adapter.CustomSpinnerAdapter;
import com.example.biitemployeeperformanceappraisalsystem.helper.CommonMethods;
import com.example.biitemployeeperformanceappraisalsystem.helper.SharedPreferencesManager;
import com.example.biitemployeeperformanceappraisalsystem.models.Course;
import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeCourseScore;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeCoursesPerformanceRequest;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeIdsWithSession;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeKpiScore;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeKpiScoreMultiSession;
import com.example.biitemployeeperformanceappraisalsystem.models.MultiEmployeeCoursePerformanceRequest;
import com.example.biitemployeeperformanceappraisalsystem.models.Session;
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
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ComparisonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ComparisonFragment extends Fragment {
    SharedPreferencesManager sharedPreferencesManager;
    Boolean isKpi = true;
    Boolean isCourse = false;
    Boolean isSubKpi = false;
    Boolean isQuestion = false;
    Session session;
    Course course;
    EmployeeService employeeService;
    CourseService courseService;
    SessionService sessionService;
    List<EmployeeKpiScore> employeeKpiScoreList;
    List<List<EmployeeKpiScore>> multiEmployeesKpiScoreList;
    List<EmployeeKpiScoreMultiSession> employeeKpiScoreMultiSessionList;
    EmployeeCoursePerformanceService employeeCoursePerformanceService;
    List<Employee> employeeList;
    List<Course> courseList;
    List<Session> sessionList;
    LinearLayout sessionLayout, comparisonSessionLayout, employeeLayout, courseLayout;
    CustomSpinnerAdapter customSpinnerAdapter;
    BarChart barChart;
    TabLayout tabLayout;
    Spinner sessionSpinner,fromSessionSpinner,toSessionSpinner,courseSpinner, employeeSpinner;

    // TODO: Rename and change types and number of parameters
    public static ComparisonFragment newInstance(String param1, String param2) {
        ComparisonFragment fragment = new ComparisonFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comparison, container, false);

        // Initializing layout items
        barChart = view.findViewById(R.id.bar_chart);
        tabLayout = view.findViewById(R.id.comparison_type_tab);
        employeeLayout = view.findViewById(R.id.employee_spinner_layout);
        sessionLayout = view.findViewById(R.id.session_spinner_layout);
        courseLayout = view.findViewById(R.id.course_spinner_layout);
        employeeSpinner = view.findViewById(R.id.spinner_employee);
        courseSpinner = view.findViewById(R.id.spinner_course);
        sessionSpinner = view.findViewById(R.id.spinner_session);

        // Initializing required instances
        sharedPreferencesManager = new SharedPreferencesManager(getContext());
        employeeCoursePerformanceService = new EmployeeCoursePerformanceService(getContext());
        employeeService = new EmployeeService(getContext());
        courseService = new CourseService(getContext());
        sessionService = new SessionService(view.getContext());

        // Setting up session so if session spinner does not populate then there will be current session id being used
        session = new Session();
        session.setId(sharedPreferencesManager.getSessionId());

        // To hide bar chart label
        barChart.getDescription().setTextColor(Color.TRANSPARENT);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position){
                    case 0:
                        isKpi = true;
                        isCourse = isSubKpi = isQuestion = false;
                        updateKpiComparisonBarChart();
                        break;
                    case 1:
                        isSubKpi = true;
                        isCourse = isKpi = isQuestion = false;
                        break;
                    case 2:
                        isCourse = true;
                        isSubKpi = isKpi = isQuestion = false;
                        // updateCoursesPerformanceBarChart();
                        updateCourseComparisonBarChart();
                        break;
                    case 3:
                        isQuestion = true;
                        isCourse = isKpi = isSubKpi = false;
                        // updateCoursePerformanceComparisonBarChart();
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

        employeeService.getEmployees(
                employees -> {
                    employeeList = employees;
                    Employee employee = new Employee();
                    employee.setName("Select All");
                    employeeList.add(employee);

                    // populate the custom employee spinner
                    customSpinnerAdapter = new CustomSpinnerAdapter(getContext(), R.layout.custom_spinner_item_layout, employeeList);
                    employeeSpinner.setAdapter(customSpinnerAdapter);

                    customSpinnerAdapter.setOnItemSelectionChangedListener(new CustomSpinnerAdapter.OnItemSelectionChangedListener() {
                        @Override
                        public void onItemSelectionChanged(List<Integer> selectedEmployeeIds) {
                            if (isKpi) {
                                updateKpiComparisonBarChart();
                            } else if (isSubKpi) {

                            } else if (isCourse) {
                                updateCourseComparisonBarChart();
                            } else if (isQuestion) {

                            }
                        }
                    });

                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
        );

        courseService.getCourses(
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

        sessionService.getSessions(sessions -> {
                    // Handle the list of sessions here
                    sessionList = sessions;
                    // Populate the spinner with session titles
                    sessionService.populateSpinner(sessions,sessionSpinner);
                },
                // onFailure callback
                errorMessage -> {
                    // Handle failure
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                });

        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                course = courseList.get(position);
//                if (isComparison){
//                    if (isCourseComparison)
//                        updateCoursePerformanceComparisonBarChart();
//                    else
//                        updateGroupBarChart();
//                }else {
//                    if (isCourseComparison)
//                        updateCoursesPerformanceBarChart();
//                    else
//                        updateBarChart();
//                }
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
                if (isKpi) {
                    updateKpiComparisonBarChart();
                } else if (isSubKpi) {

                } else if (isCourse) {
                    updateCourseComparisonBarChart();
                } else if (isQuestion) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case where nothing is selected
            }
        });

        return view;
    }

//    private void updateBarChart() {
//        // pieChart.setVisibility(View.GONE);
//        employeeLayout.setVisibility(View.GONE);
//
//        // barChart.setVisibility(View.VISIBLE);
//        sessionLayout.setVisibility(View.VISIBLE);
//        courseLayout.setVisibility(View.GONE);
//
//        EmployeeKpiScoreService employeeKpiScoreService = new EmployeeKpiScoreService(getContext());
//        employeeKpiScoreService.getEmployeeKpiScore(
//                employeeID,
//                sessionList.get(sessionSpinner.getSelectedItemPosition()).getId(),
//                employeeKpiScores -> {
//                    employeeKpiScoreList = employeeKpiScores;
//                    ArrayList<BarEntry> entries = new ArrayList<>();
//                    ArrayList<String> kpiTitles = new ArrayList<>();
//                    int index = 0;
//
//                    for (EmployeeKpiScore e : employeeKpiScores) {
//                        BarEntry barEntry = new BarEntry(index++, e.getScore());
//                        entries.add(barEntry);
//                        kpiTitles.add(e.getKpi_title()+e.getWeightage()+"%");
//                    }
//
//                    // Create the BarDataSet
//                    BarDataSet dataSet = new BarDataSet(entries, "");
//                    dataSet.setValueTextSize(15f);
//
//                    // Generate colors dynamically
//                    CommonMethods commonMethods = new CommonMethods();
//                    ArrayList<Integer> colors = commonMethods.generateRandomColors(entries.size());
//                    dataSet.setColors(colors);
//
//                    // Create the BarData
//                    BarData data = new BarData(dataSet);
//                    data.setBarWidth(0.2f); // set custom bar width
//
//                    // Set the data for the bar chart
//                    barChart.setData(data);
//                    barChart.setFitBars(true); // make the x-axis fit exactly all bars
//
//                    // Set up the x-axis labels
//                    XAxis xAxis = barChart.getXAxis();
//                    xAxis.setValueFormatter(new IndexAxisValueFormatter(kpiTitles));
//                    xAxis.setGranularity(1f); // only intervals of 1
//                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//
//                    // Refresh the chart
//                    barChart.notifyDataSetChanged();
//                    barChart.invalidate();
//                },
//                errorMessage -> {
//                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
//                }
//        );
//    }

//    private void updateCoursesPerformanceBarChart(){
//        // pieChart.setVisibility(View.GONE);
//        sessionLayout.setVisibility(View.VISIBLE);
//
//        // barChart.setVisibility(View.VISIBLE);
//        employeeLayout.setVisibility(View.GONE);
//        courseLayout.setVisibility(View.GONE);
//
//        try {
//            List<Integer> coursesIds = new ArrayList<>();
//            for (Course c: courseList) {
//                coursesIds.add(c.getId());
//            }
//            EmployeeCoursesPerformanceRequest employeeCoursesPerformanceRequest = new EmployeeCoursesPerformanceRequest(employeeID, session.getId(), coursesIds);
//            employeeCoursePerformanceService.getEmployeeCoursesPerformance(
//                    employeeCoursesPerformanceRequest,
//                    employeeCourseScores -> {
//                        // employeeKpiScoreList = employeeKpiScores;
//                        ArrayList<BarEntry> entries = new ArrayList<>();
//                        ArrayList<String> kpiTitles = new ArrayList<>();
//                        int index = 0;
//
//                        for (EmployeeCourseScore e : employeeCourseScores) {
//                            BarEntry barEntry = new BarEntry(index++, (float) e.getAverage());
//                            entries.add(barEntry);
//                            kpiTitles.add(e.getCourse().getName());
//                        }
//
//                        // Create the BarDataSet
//                        BarDataSet dataSet = new BarDataSet(entries, "");
//                        dataSet.setValueTextSize(15f);
//
//                        // Generate colors dynamically
//                        CommonMethods commonMethods = new CommonMethods();
//                        ArrayList<Integer> colors = commonMethods.generateRandomColors(entries.size());
//                        dataSet.setColors(colors);
//
//                        // Create the BarData
//                        BarData data = new BarData(dataSet);
//                        data.setBarWidth(0.2f); // set custom bar width
//
//                        // Set the data for the bar chart
//                        barChart.setData(data);
//                        barChart.setFitBars(true); // make the x-axis fit exactly all bars
//
//                        // Set up the x-axis labels
//                        XAxis xAxis = barChart.getXAxis();
//                        xAxis.setValueFormatter(new IndexAxisValueFormatter(kpiTitles));
//                        xAxis.setGranularity(1f); // only intervals of 1
//                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//
//                        // Refresh the chart
//                        barChart.notifyDataSetChanged();
//                        barChart.invalidate();
//                    },
//                    errorMessage -> {
//                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
//                    }
//            );
//        }catch (Exception ex){
//            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }

    public void updateCourseComparisonBarChart(){
        // pieChart.setVisibility(View.GONE);
        sessionLayout.setVisibility(View.VISIBLE);

        // barChart.setVisibility(View.VISIBLE);
        // employeeLayout.setVisibility(View.VISIBLE);
        courseLayout.setVisibility(View.VISIBLE);
        try {
            // Define the labels for each group
            List<String> groupLabels = new ArrayList<>();
            ArrayList<Integer> employeeIds = new ArrayList<>();
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

    private void updateKpiComparisonBarChart() {
        sessionLayout.setVisibility(View.VISIBLE);

        // barChart.setVisibility(View.VISIBLE);
        // employeeLayout.setVisibility(View.VISIBLE);
        courseLayout.setVisibility(View.GONE);

//        if (employeeSpinner.getAdapter().isEmpty() || sessionSpinner.getAdapter().isEmpty() || courseSpinner.getAdapter().isEmpty()){
//            return;
//        }

        try {
            // Define the labels for each group
            List<String> groupLabels = new ArrayList<>();
            ArrayList<Integer> employeeIds = new ArrayList<>();

            // employeeIds.add(employeeID);
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
//                                        for (int c=0; c < employeeList.size(); c++){
//                                            if (employeeID == employeeList.get(c).getId()){
//                                                groupLabels.add(employeeList.get(c).getName());
//                                            }
//                                        }
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

    private void updateEmployeeSpinnerContents() {
        // Create a new list to hold the updated items for the evaluatee spinner
        List<Employee> updatedEmployeeList = new ArrayList<>();

        // Add the default "Select All" item to the list


    }

    private List<Employee> getUpdatedEmployeeList() {
        List<Employee> updatedEmployeeList = new ArrayList<>();
        for (Employee employee : employeeList) {
            updatedEmployeeList.add(employee);
        }
        return updatedEmployeeList;
    }

}