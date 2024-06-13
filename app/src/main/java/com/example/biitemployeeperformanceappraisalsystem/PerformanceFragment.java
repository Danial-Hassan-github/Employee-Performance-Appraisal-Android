package com.example.biitemployeeperformanceappraisalsystem;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.adapter.CoursePerformanceExpandableListAdapter;
import com.example.biitemployeeperformanceappraisalsystem.adapter.CustomSpinnerAdapter;
import com.example.biitemployeeperformanceappraisalsystem.helper.CommonMethods;
import com.example.biitemployeeperformanceappraisalsystem.helper.SharedPreferencesManager;
import com.example.biitemployeeperformanceappraisalsystem.models.Course;
import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.models.CourseScore;
import com.example.biitemployeeperformanceappraisalsystem.models.KpiScore;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeKpiScoreMultiSession;
import com.example.biitemployeeperformanceappraisalsystem.models.SubKpiScore;
import com.example.biitemployeeperformanceappraisalsystem.models.QuestionScore;
import com.example.biitemployeeperformanceappraisalsystem.models.QuestionnaireType;
import com.example.biitemployeeperformanceappraisalsystem.models.Session;
import com.example.biitemployeeperformanceappraisalsystem.network.services.CourseService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeCoursePerformanceService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeKpiScoreService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeQuestionScoreService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeSubKpiScoreService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.QuestionnaireService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.SessionService;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
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
    Boolean isKpi = true;
    Boolean isSubKpi = false;
    Boolean isCourse = false;
    Boolean isQuestion = false;
    int employeeID;
    Employee employee2;
    Session session;
    Course course;
    List<KpiScore> kpiScoreList;
    List<List<KpiScore>> multiEmployeesKpiScoreList;
    List<EmployeeKpiScoreMultiSession> employeeKpiScoreMultiSessionList;
    SessionService sessionService;
    CourseService courseService;
    EmployeeService employeeService;
    EmployeeCoursePerformanceService employeeCoursePerformanceService;
    EmployeeSubKpiScoreService employeeSubKpiScoreService;
    EmployeeQuestionScoreService employeeQuestionScoreService;
    QuestionnaireService questionnaireService;
    List<Employee> employeeList;
    List<QuestionnaireType> questionnaireTypeList;
    List<QuestionScore> employeeQuestionScoreList;
    List<Course> courseList;
    List<Session> sessionList;
    LinearLayout sessionLayout, comparisonSessionLayout, employeeLayout, courseLayout, questionnaireTypeLayout;
    CustomSpinnerAdapter customSpinnerAdapter;
    TextView txtEmployeeName;
    PieChart pieChart;
    BarChart barChart;
    TabLayout tabLayout;
    Spinner sessionSpinner,fromSessionSpinner,toSessionSpinner,courseSpinner, employeeSpinner, questionnaireTypeSpinner;
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
        // employeeLayout = view.findViewById(R.id.employee_spinner_layout);
        sessionLayout = view.findViewById(R.id.session_spinner_layout);
        questionnaireTypeLayout = view.findViewById(R.id.questionnaire_type_spinner_layout);
        courseLayout = view.findViewById(R.id.course_spinner_layout);
        comparisonSessionLayout = view.findViewById(R.id.comparison_sessions_layout);
        questionnaireTypeSpinner = view.findViewById(R.id.spinner_questionnaire_type);
        expandableListView = view.findViewById(R.id.employee_course_questions_scores);
        sharedPreferencesManager = new SharedPreferencesManager(getContext());
        session = new Session();
        session.setId(sharedPreferencesManager.getSessionId());
        employeeQuestionScoreService = new EmployeeQuestionScoreService(getContext());
        employeeSubKpiScoreService = new EmployeeSubKpiScoreService(getContext());
        questionnaireService = new QuestionnaireService(getContext());
        employeeService = new EmployeeService(getContext());
        courseService = new CourseService(getContext());
        sessionService = new SessionService(view.getContext());

//        if (getActivity() instanceof FacultyMain || getActivity() instanceof HodMainActivity){
//            // tabLayout.setVisibility(View.GONE);
//            tabLayout.getTabAt(1).view.setVisibility(View.GONE);
//            tabLayout.getTabAt(3).view.setVisibility(View.GONE);
//            try {
//                employeeID = sharedPreferencesManager.getEmployeeUserObject().getEmployee().getId();
//            }catch (Exception ex){
//
//            }
//            txtEmployeeName.setVisibility(View.GONE);
//        }

        adapter = new CoursePerformanceExpandableListAdapter(getContext(), new ArrayList<>(), new HashMap<>());
        expandableListView.setAdapter(adapter);

        pieChart.getDescription().setTextColor(Color.TRANSPARENT);
        barChart.getDescription().setTextColor(Color.TRANSPARENT);

        // employeeSpinner = view.findViewById(R.id.spinner_employee);
        courseSpinner = view.findViewById(R.id.spinner_course);
        sessionSpinner = view.findViewById(R.id.spinner_session);
//        fromSessionSpinner = view.findViewById(R.id.spinner_session_from);
//        toSessionSpinner = view.findViewById(R.id.spinner_session_to);

        employeeCoursePerformanceService = new EmployeeCoursePerformanceService(getContext());
        // EmployeeKpiScoreService employeeKpiScoreService = new EmployeeKpiScoreService(getContext());

//        employeeService.getEmployees(
//                employees -> {
//                    employeeList = employees;
//                    // employeeService.populateEmployeesSpinner(employeeList, employeeSpinner);
//                    for (Employee e: employeeList) {
//                        if (e.getId() == employeeID){
//                            txtEmployeeName.setText(e.getName());
//                        }
//                    }
//                    updateEvaluateeSpinnerContents();
//
//                    // employeeService.populateEmployeesSpinner(employeeList, employeeSpinner);
//                },
//                errorMessage -> {
//                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
//                }
//        );

        questionnaireService.getQuestionnaireType(
                questionnaireTypes -> {
                    questionnaireTypeList = questionnaireTypes;
                    questionnaireService.populateSpinner(questionnaireTypeList, questionnaireTypeSpinner);
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
        );

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

//        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                course = courseList.get(position);
//                if (isComparison){
//                    if (isCourseComparison)
//                        updateQuestionsPerformanceBarChart();
//                    else
//                        updateGroupBarChart();
//                }else {
//                    if (isCourseComparison)
//                        updateCoursesPerformanceBarChart();
//                    else
//                        updateKpiPerformanceChart();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        // Set an item selected listener for the session spinner
        sessionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected session
                session = sessionList.get(position);
                updateChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case where nothing is selected
            }
        });

        questionnaireTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                        updateKpiPerformanceChart();
                        break;
                    case 1:
                        updateSubKpiPerformanceChart();
                        break;
                    case 2:
                        updateCoursesPerformanceBarChart();
                        break;
                    case 3:
                        questionnaireTypeLayout.setVisibility(View.VISIBLE);
                        updateQuestionsPerformanceBarChart();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        isKpi = false;
                        break;
                    case 1:
                        isSubKpi = false;
                        break;
                    case 2:
                        isCourse = false;
                        break;
                    case 3:
                        questionnaireTypeLayout.setVisibility(View.GONE);
                        isQuestion = false;
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void updateKpiPerformanceChart() {
        // pieChart.setVisibility(View.GONE);
        // employeeLayout.setVisibility(View.GONE);

        // barChart.setVisibility(View.VISIBLE);
        // sessionLayout.setVisibility(View.VISIBLE);
        // courseLayout.setVisibility(View.GONE);

        EmployeeKpiScoreService employeeKpiScoreService = new EmployeeKpiScoreService(getContext());
        employeeKpiScoreService.getEmployeeKpiScore(
                employeeID,
                sessionList.get(sessionSpinner.getSelectedItemPosition()).getId(),
                employeeKpiScores -> {
                    kpiScoreList = employeeKpiScores;
                    ArrayList<BarEntry> entries = new ArrayList<>();
                    ArrayList<String> kpiTitles = new ArrayList<>();
                    int index = 0;

                    for (KpiScore e : employeeKpiScores) {
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
        // employeeLayout.setVisibility(View.GONE);
        // courseLayout.setVisibility(View.GONE);

        try {
//            List<Integer> coursesIds = new ArrayList<>();
//            for (Course c: courseList) {
//                coursesIds.add(c.getId());
//            }
//            EmployeeCoursesPerformanceRequest employeeCoursesPerformanceRequest = new EmployeeCoursesPerformanceRequest(employeeID, session.getId(), coursesIds);
            employeeCoursePerformanceService.getEmployeeCoursesPerformance(
                    employeeID,
                    session.getId(),
                    employeeCourseScores -> {
                        // employeeKpiScoreList = employeeKpiScores;
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        ArrayList<String> kpiTitles = new ArrayList<>();
                        int index = 0;

                        for (CourseScore e : employeeCourseScores) {
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

    public void updateQuestionsPerformanceBarChart(){
        // pieChart.setVisibility(View.GONE);
        // sessionLayout.setVisibility(View.VISIBLE);

        // barChart.setVisibility(View.VISIBLE);
        // employeeLayout.setVisibility(View.VISIBLE);
        // courseLayout.setVisibility(View.VISIBLE);
        try {
            if (sessionSpinner != null && questionnaireTypeSpinner != null) {
                SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(getContext());
                // Retrieve the selected session and employee IDs
//                int sessionId = sessionList.get(sessionSpinner.getSelectedItemPosition()).getId();
//                int employeeId = 0;
//
//                if (getActivity() instanceof DirectorMainActivity){
//                    employeeId = employeeList.get(employeeSpinner.getSelectedItemPosition()).getId();
//                }else {
//                    employeeId = sharedPreferencesManager.getEmployeeUserObject().getEmployee().getId();
//                }

                // Set the evaluation type ID (you might want to change this according to your logic)
                int evaluationTypeId = questionnaireTypeList.get(questionnaireTypeSpinner.getSelectedItemPosition()).getId();

                // Call the service to get employee question scores based on the selected session, employee, and evaluation type
                employeeQuestionScoreService = new EmployeeQuestionScoreService(getContext());
                employeeQuestionScoreService.getEmployeeQuestionScore(
                        employeeID,
                        session.getId(),
                        evaluationTypeId,
                        employeeQuestionScores -> {
                            // Handle the retrieved employee question scores
                            employeeQuestionScoreList = employeeQuestionScores;

                            // Populate the array with the data from employeeScoreList
                            for (int i = 0; i < employeeQuestionScoreList.size(); i++) {
                                QuestionScore score = employeeQuestionScoreList.get(i);

                            }
                            ArrayList<BarEntry> entries = new ArrayList<>();
                            ArrayList<String> questionTitles = new ArrayList<>();
                            int index = 0;

                            for (QuestionScore e : employeeQuestionScoreList) {
                                BarEntry barEntry = new BarEntry(index++, (float) e.getAverage());
                                entries.add(barEntry);
                                // questionTitles.add(e.getQuestion().getQuestion());
                                questionTitles.add("Q"+(index));
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

                            barChart.setMarker(new IMarker() {
                                @Override
                                public MPPointF getOffset() {
                                    return null;
                                }

                                @Override
                                public MPPointF getOffsetForDrawingAtPoint(float posX, float posY) {
                                    return null;
                                }

                                @Override
                                public void refreshContent(Entry e, Highlight highlight) {

                                }

                                @Override
                                public void draw(Canvas canvas, float posX, float posY) {

                                }
                            });

                            // Set up the x-axis labels
                            XAxis xAxis = barChart.getXAxis();
                            xAxis.setValueFormatter(new IndexAxisValueFormatter(questionTitles));
                            xAxis.setGranularity(1f); // only intervals of 1
                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                            // Refresh the chart
                            barChart.notifyDataSetChanged();
                            barChart.invalidate();
                        },
                        errorMessage -> {
                            // Handle the error message
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        });
            } else {
                // Handle the case where either spinner is null
                Toast.makeText(getContext(), "Spinners are not initialized", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception ex){
            Log.e("","",ex);
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

//    private void updateGroupBarChart() {
//        // pieChart.setVisibility(View.GONE);
//        // sessionLayout.setVisibility(View.VISIBLE);
//
//        // barChart.setVisibility(View.VISIBLE);
//        // employeeLayout.setVisibility(View.VISIBLE);
//        // courseLayout.setVisibility(View.GONE);
//
////        if (employeeSpinner.getAdapter().isEmpty() || sessionSpinner.getAdapter().isEmpty() || courseSpinner.getAdapter().isEmpty()){
////            return;
////        }
//
//        try {
//            // Define the labels for each group
//            List<String> groupLabels = new ArrayList<>();
//            ArrayList<Integer> employeeIds = new ArrayList<>();
//
//            employeeIds.add(employeeID);
//            // Log.i("",customSpinnerAdapter.getSelectedEmployeeIds().get(0).toString());
//            employeeIds.addAll(customSpinnerAdapter.getSelectedEmployeeIds());
//            employeeIds.remove(new int[]{0});
//            // employeeIds.add(employee2.getId());
//
//            EmployeeKpiScoreService employeeKpiScoreService = new EmployeeKpiScoreService(getContext());
//            EmployeeIdsWithSession employeeIdsWithSession = new EmployeeIdsWithSession();
//            employeeIdsWithSession.setEmployeeIds(employeeIds);
//            employeeIdsWithSession.setSession_id(session.getId());
//
//            employeeKpiScoreService.compareEmployeeKpiScore(
//                    employeeIdsWithSession,
//                    employeeKpiScores -> {
//                        // TODO
//                        try {
//                            multiEmployeesKpiScoreList = employeeKpiScores;
//                            List<String> kpiLabels = new ArrayList<>();
//                            // Determine the maximum size of the groups
//                            int maxSize = 0;
//                            for (List<KpiScore> scores : multiEmployeesKpiScoreList) {
//                                int newSize = Math.max(maxSize, scores.size());
//                                if (newSize > maxSize){
//                                    for (KpiScore s:scores) {
//                                        kpiLabels.add(s.getKpi_title());
//                                    }
//                                }
//                                maxSize = Math.max(maxSize, scores.size());
//                            }
//
//                            if (multiEmployeesKpiScoreList.size() > 0) {
//                                ArrayList<Integer> colors = null;
//                                CommonMethods commonMethods = new CommonMethods();
//                                colors = commonMethods.generateRandomColors(maxSize);
//                                int xCounter = 0;
//                                int xInitialCounter = 1;
//                                BarData barData = new BarData();
//
//                                List<List<BarEntry>> groups = new ArrayList<>();
//
//                                // Initialize the list of groups
//                                for (int i = 0; i < maxSize; i++) {
//                                    groups.add(new ArrayList<>());
//                                }
//
//                                for (int i = 0; i < multiEmployeesKpiScoreList.size(); i++) {
//                                    List<KpiScore> kpiScore = multiEmployeesKpiScoreList.get(i);
//                                    ArrayList<BarEntry> group = new ArrayList<>();
//
//                                    String kpiTitle = null;
//
//                                    // Pad the list with null items if necessary
//                                    while (kpiScore.size() < maxSize) {
//                                        kpiScore.add(new KpiScore());
//                                    }
//
//                                    if (i == 0) {
//                                        for (int c=0; c < employeeList.size(); c++){
//                                            if (employeeID == employeeList.get(c).getId()){
//                                                groupLabels.add(employeeList.get(c).getName());
//                                            }
//                                        }
//                                        for (int j=0; j<customSpinnerAdapter.getSelectedEmployeeIds().size();j++){
//                                            groupLabels.add(employeeList.get(j).getName());
//                                        }
//                                        // groupLabels.add(employee2.getName());
//                                    }
//
//                                    for (int k = 0; k < maxSize; k++) {
//                                        groups.get(k).add(new BarEntry(xCounter, kpiScore.get(k).getScore()));
//                                        // group.add(new BarEntry(k , employeeKpiScore.get(k).getScore())); // Use 'k' as x-value for the BarEntry
//                                        // kpiTitle = employeeKpiScore.get(k).getKpi_title();
//                                        xCounter = xCounter+3;
//                                    }
//
//                                    // BarDataSet barDataSet = new BarDataSet(group, kpiTitle);
//                                    // barDataSet.setColors(colors);
//                                    // barData.addDataSet(barDataSet);
//
//                                    xCounter = xInitialCounter;
//                                    xInitialCounter++;
//                                }
//
//                                // Create BarData and set data sets
//                                BarData barData1 = new BarData();
//
//                                // Create BarDataSet for each group
//                                List<BarDataSet> dataSets = new ArrayList<>();
//                                for (int i = 0; i < groups.size(); i++) {
//                                    BarDataSet barDataSet = new BarDataSet(groups.get(i), kpiLabels.get(i));
//                                    // CommonMethods commonMethods = new CommonMethods();
//                                    // ArrayList<Integer> colors = commonMethods.generateRandomColors(groups.get(i).size());
//                                    barDataSet.setColor(colors.get(i));
//                                    dataSets.add(barDataSet);
//                                    barData1.addDataSet(barDataSet);
//                                }
//
//                                // Ensure there are at least 2 BarDataSets
//                                if (barData1.getDataSetCount() < 2) {
//                                    barData1.addDataSet(new BarDataSet(new ArrayList<>(), "")); // Add an empty dataset if needed
//                                }
//                                // Ensure there are at least 2 BarDataSets
//                                if (barData1.getDataSetCount() < 1) {
//                                    barData1.addDataSet(new BarDataSet(new ArrayList<>(), "")); // Add an empty dataset if needed
//                                    barData1.addDataSet(new BarDataSet(new ArrayList<>(), "")); // Add an empty dataset if needed
//                                }
//
//                                float groupSpace = 0.5f; // space between groups of bars
//                                float barSpace = 0.02f; // space between individual bars within a group
//                                float barWidth = 0.2f; // width of each bar
//
//                                barData1.setBarWidth(barWidth);
//                                barChart.setData(barData1);
//
//                                barChart.groupBars(0, groupSpace, barSpace); // Grouped bars with space between groups
//                                barChart.invalidate();
//
//                                // Set custom labels for the x-axis
//                                XAxis xAxis = barChart.getXAxis();
//                                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//                                xAxis.setLabelCount(groupLabels.size());
//                                xAxis.setValueFormatter(new ValueFormatter() {
//                                    @Override
//                                    public String getFormattedValue(float value) {
//                                        int index = (int) value;
//                                        if (index >= 0 && index < groupLabels.size()) {
//                                            String label = groupLabels.get(index);
//                                            return label != null ? label : "Unknown";
//                                        } else {
//                                            return "";
//                                        }
//                                    }
//                                });
//                            }
//                        }catch (Exception ex){
//                            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    },
//                    errorMessage -> {
//                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
//                    }
//            );
//        }catch (Exception ex){
//            Log.e("","", ex);
//            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }

    private void updateSubKpiPerformanceChart() {
        try {
            employeeSubKpiScoreService.getSubKpiEmployeePerformance(
                    employeeID,
                    session.getId(),
                    employeeSubKpiScores -> {
                        // employeeKpiScoreList = employeeKpiScores;
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        ArrayList<String> titles = new ArrayList<>();
                        int index = 0;

                        for (SubKpiScore e : employeeSubKpiScores) {
                            BarEntry barEntry = new BarEntry(index++, (float) e.getScore());
                            entries.add(barEntry);
                            titles.add(e.getName());
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
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(titles));
                        xAxis.setGranularity(1f); // only intervals of 1
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                        // Refresh the chart
                        barChart.notifyDataSetChanged();
                        barChart.invalidate();
                    },
                    errorMessage -> {
                        Log.e("Error Sub Kpi:", errorMessage);
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
            );
        }catch (Exception ex){
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void updateChart(){
        if (isKpi){
            updateKpiPerformanceChart();
        } else if (isSubKpi) {
            updateSubKpiPerformanceChart();
        } else if (isCourse) {
            updateCoursesPerformanceBarChart();
        } else if (isQuestion) {
            updateQuestionsPerformanceBarChart();
        }
    }

//    private void updateEvaluateeSpinnerContents() {
//        // Create a new list to hold the updated items for the evaluatee spinner
//         List<Employee> updatedEvaluateeList = new ArrayList<>();
//
//        // Add the default "Select All" item to the list
//        Employee employee = new Employee();
//        employee.setName("Select All");
//        updatedEvaluateeList.add(employee);
//
//        // Add all employees to the list initially
//         updatedEvaluateeList.addAll(getUpdatedEmployeeList(employeeID));
//
//        // Update the evaluatee spinner with the updated list
//        customSpinnerAdapter = new CustomSpinnerAdapter(getContext(), R.layout.custom_spinner_item_layout, updatedEvaluateeList);
//        employeeSpinner.setAdapter(customSpinnerAdapter);
//
//        customSpinnerAdapter.setOnItemSelectionChangedListener(new CustomSpinnerAdapter.OnItemSelectionChangedListener() {
//            @Override
//            public void onItemSelectionChanged(List<Integer> selectedEmployeeIds) {
//                if (isCourseComparison)
//                    updateQuestionsPerformanceBarChart();
//                else
//                    updateGroupBarChart();
//            }
//        });
//
//    }
//
//    private List<Employee> getUpdatedEmployeeList(int evaluatorId) {
//        List<Employee> updatedEmployeeList = new ArrayList<>();
//        for (Employee employee : employeeList) {
//            if (employee.getId() != evaluatorId) {
//                updatedEmployeeList.add(employee);
//            }
//        }
//        return updatedEmployeeList;
//    }
}