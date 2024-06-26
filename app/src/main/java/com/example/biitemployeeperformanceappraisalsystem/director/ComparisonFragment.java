package com.example.biitemployeeperformanceappraisalsystem.director;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.ScoresFragment;
import com.example.biitemployeeperformanceappraisalsystem.adapter.CustomCourseSpinnerAdapter;
import com.example.biitemployeeperformanceappraisalsystem.adapter.CustomSpinnerAdapter;
import com.example.biitemployeeperformanceappraisalsystem.helper.CommonMethods;
import com.example.biitemployeeperformanceappraisalsystem.helper.SharedPreferencesManager;
import com.example.biitemployeeperformanceappraisalsystem.models.ApiRequestModels.QuestionsScoresRequest;
import com.example.biitemployeeperformanceappraisalsystem.models.Course;
import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.models.CourseScore;
import com.example.biitemployeeperformanceappraisalsystem.models.ApiRequestModels.EmployeeIdsWithSession;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeCourseScore;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeKpiScore;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeSubKpiScore;
import com.example.biitemployeeperformanceappraisalsystem.models.KPI;
import com.example.biitemployeeperformanceappraisalsystem.models.KpiScore;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeKpiScoreMultiSession;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeQuestionsScores;
import com.example.biitemployeeperformanceappraisalsystem.models.MultiEmployeeCoursePerformanceRequest;
import com.example.biitemployeeperformanceappraisalsystem.models.QuestionScore;
import com.example.biitemployeeperformanceappraisalsystem.models.QuestionnaireType;
import com.example.biitemployeeperformanceappraisalsystem.models.Session;
import com.example.biitemployeeperformanceappraisalsystem.models.SubKpi;
import com.example.biitemployeeperformanceappraisalsystem.models.SubKpiScore;
import com.example.biitemployeeperformanceappraisalsystem.network.services.CourseService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeCoursePerformanceService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeKpiScoreService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeQuestionScoreService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeSubKpiScoreService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.KpiService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.QuestionnaireService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.SessionService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.SubKpiService;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
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
    Boolean isSingleSubKpi = false;
    Boolean isYearlyKpi = false;
    KPI kpi;
    SubKpi subKpi;
    Session session;
    Course course;
    EmployeeService employeeService;
    CourseService courseService;
    SessionService sessionService;
    SubKpiService subKpiService;
    KpiService kpiService;
    QuestionnaireService questionnaireService;
    List<String> yearList;
    String year = "";
    List<KPI> kpiList;
    List<SubKpi> subKpiList;
    List<KpiScore> kpiScoreList;
    List<EmployeeQuestionsScores> employeeQuestionsScoresList;
    List<EmployeeKpiScore> multiEmployeesKpiScoreList;
    List<EmployeeKpiScoreMultiSession> employeeKpiScoreMultiSessionList;
    EmployeeCoursePerformanceService employeeCoursePerformanceService;
    List<Employee> employeeList;
    List<Course> courseList;
    List<Session> sessionList;
    List<QuestionnaireType> questionnaireTypeList;
    LinearLayout sessionLayout, comparisonSessionLayout, employeeLayout, courseLayout, questionnaireTypeLayout, subKpiLayout, kpiLayout, yearLayout;
    CustomSpinnerAdapter customSpinnerAdapter;
    CustomCourseSpinnerAdapter customCourseSpinnerAdapter;
    BarChart barChart;
    TabLayout tabLayout;
    Spinner sessionSpinner,fromSessionSpinner,toSessionSpinner,questionnaireTypeSpinner,courseSpinner, employeeSpinner, subkpiSpinner, kpiSpinner, yearSpinner;
    List<EmployeeCourseScore> employeeCourseScoresList;

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
        subKpiLayout = view.findViewById(R.id.subkpi_spinner_layout);
        kpiLayout = view.findViewById(R.id.kpi_spinner_layout);
        yearLayout = view.findViewById(R.id.year_spinner_layout);
        questionnaireTypeLayout = view.findViewById(R.id.questionnaire_type_spinner_layout);
        subkpiSpinner = view.findViewById(R.id.spinner_subkpi);
        employeeSpinner = view.findViewById(R.id.spinner_employee);
        courseSpinner = view.findViewById(R.id.spinner_course);
        sessionSpinner = view.findViewById(R.id.spinner_session);
        kpiSpinner = view.findViewById(R.id.spinner_kpi);
        yearSpinner = view.findViewById(R.id.spinner_year);
        questionnaireTypeSpinner = view.findViewById(R.id.spinner_questionnaire_type);

        // Initializing required instances
        sharedPreferencesManager = new SharedPreferencesManager(getContext());
        employeeCoursePerformanceService = new EmployeeCoursePerformanceService(getContext());
        employeeService = new EmployeeService(getContext());
        courseService = new CourseService(getContext());
        sessionService = new SessionService(view.getContext());
        kpiService = new KpiService(getContext());
        questionnaireService = new QuestionnaireService(getContext());
        subKpiService = new SubKpiService(getContext());

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
                        updateKpiComparisonBarChart();
                        break;
                    case 1:
                        isSubKpi = true;
                        updateSubKpiComparisonBarChart();
                        break;
                    case 2:
                        isCourse = true;
                        courseLayout.setVisibility(View.VISIBLE);
                        updateCourseComparisonBarChart();
                        break;
                    case 3:
                        isQuestion = true;
                        updateQuestionComparisonBarChart();
                        break;
                    case 4:
                        subKpiLayout.setVisibility(View.VISIBLE);
                        updateSingleSubKpiComparisonBarChart();
                        isSingleSubKpi = true;
                        break;
                    case 5:
                        isYearlyKpi = true;
                        sessionLayout.setVisibility(View.GONE);
                        kpiLayout.setVisibility(View.VISIBLE);
                        yearLayout.setVisibility(View.VISIBLE);
                        updateYearlyKpiComparisonBarChart();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position){
                    case 0:
                        isKpi = false;
                        break;
                    case 1:
                        isSubKpi = false;
                        break;
                    case 2:
                        isCourse = false;
                        courseLayout.setVisibility(View.GONE);
                        break;
                    case 3:
                        questionnaireTypeLayout.setVisibility(View.GONE);
                        isQuestion = false;
                        break;
                    case 4:
                        subKpiLayout.setVisibility(View.GONE);
                        isSingleSubKpi = false;
                        break;
                    case 5:
                        isYearlyKpi = false;
                        sessionLayout.setVisibility(View.VISIBLE);
                        kpiLayout.setVisibility(View.GONE);
                        yearLayout.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        questionnaireService.getQuestionnaireType(
                questionnaireTypes -> {
                    questionnaireTypeList = questionnaireTypes;
                    questionnaireService.populateSpinner(questionnaireTypeList, questionnaireTypeSpinner);
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
        );

        employeeService.getEmployees(
                employees -> {
                    employeeList = new ArrayList<>();
                    Employee employee = new Employee();
                    employee.setName("Select All");
                    employeeList.add(employee);
                    employeeList.addAll(employees);

                    // populate the custom employee spinner
                    customSpinnerAdapter = new CustomSpinnerAdapter(getContext(), R.layout.custom_spinner_item_layout, employeeList);
                    employeeSpinner.setAdapter(customSpinnerAdapter);

                    customSpinnerAdapter.setOnItemSelectionChangedListener(new CustomSpinnerAdapter.OnItemSelectionChangedListener() {
                        @Override
                        public void onItemSelectionChanged(List<Integer> selectedEmployeeIds) {
                            updateChart();
                        }
                    });

                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
        );

        courseService.getCourses(
                courses -> {
                    courseList = new ArrayList<>();
                    Course c = new Course("Select All", null);
                    courseList.add(c);
                    courseList.addAll(courses);
//                    course = courseList.get(courseList.size()-1);
//                    List<String> coursesName = new ArrayList<>();
//                    for (Course c: courseList) {
//                        coursesName.add(c.getName());
//                    }
                    // populate the custom employee spinner
                    customCourseSpinnerAdapter = new CustomCourseSpinnerAdapter(getContext(), R.layout.custom_spinner_item_layout, courseList);
                    courseSpinner.setAdapter(customCourseSpinnerAdapter);

                    customCourseSpinnerAdapter.setOnItemSelectionChangedListener(new CustomCourseSpinnerAdapter.OnItemSelectionChangedListener() {
                        @Override
                        public void onItemSelectionChanged(List<Integer> selectedCourseIds) {
                            updateChart();
                        }
                    });
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
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

        sessionService.getYears(
                years -> {
                    yearList = years;
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, yearList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    yearSpinner.setAdapter(adapter);
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                }
        );

        subKpiService.getSubKPIs(
                sharedPreferencesManager.getSessionId(),
                subKpis -> {
                    if (subKpis != null) {
                        subKpiList = subKpis;
                        String subKpiTitles[] = subKpiService.getSubKpiTitles(subKpiList);
                        subKpiService.populateSpinner(subKpiList,subkpiSpinner);

//                        if (subKpiTitles != null) {
//                        } else {
//                            Toast.makeText(getContext(), "No SubKPI titles available", Toast.LENGTH_SHORT).show();
//                        }
                    } else {
                        Toast.makeText(getContext(), "Failed to fetch SubKPIs", Toast.LENGTH_SHORT).show();
                    }
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
        );

        kpiService.getKpis(
                kpis -> {
                    kpiList = kpis;
                    kpiService.populateSpinner(kpiList, kpiSpinner);
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
        );

        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                course = courseList.get(position);
                updateChart();
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
                updateChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case where nothing is selected
            }
        });

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year = yearList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

        subkpiSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subKpi = subKpiList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        kpiSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kpi = kpiList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

    private void updateSubKpiComparisonBarChart(){
        try {
            // Define the labels for each group
            List<String> groupLabels = new ArrayList<>();
            ArrayList<Integer> employeeIds = new ArrayList<>();
            // employeeIds.add(employeeID);
            // Log.i("",customSpinnerAdapter.getSelectedEmployeeIds().get(0).toString());
            employeeIds.addAll(customSpinnerAdapter.getSelectedEmployeeIds());
            // employeeIds.remove(new int[]{0});

            // employeeIds.add(employee2.getId());

            EmployeeSubKpiScoreService employeeSubKpiScoreService = new EmployeeSubKpiScoreService(getContext());
            EmployeeIdsWithSession employeeIdsWithSession = new EmployeeIdsWithSession();
            employeeIdsWithSession.setEmployeeIds(employeeIds);
            employeeIdsWithSession.setSession_id(session.getId());

            employeeSubKpiScoreService.getSubKpiMultiEmployeePerformance(
                    employeeIds,
                    session.getId(),
                    employeeSubKpiScores -> {
                        // TODO
                        try {
                            List<EmployeeSubKpiScore> multiEmployeesSubKpiScoreList = employeeSubKpiScores;
                            List<String> labels = new ArrayList<>();
                            // Determine the maximum size of the groups
                            int maxSize = 0;
                            for (EmployeeSubKpiScore scores : multiEmployeesSubKpiScoreList) {
                                int newSize = Math.max(maxSize, scores.getSubKpiPerformances().size());
                                if (newSize > maxSize){
                                    labels.clear();
                                    for (SubKpiScore s:scores.getSubKpiPerformances()) {
                                        labels.add(s.getName());
                                    }
                                }
                                maxSize = Math.max(maxSize, scores.getSubKpiPerformances().size());
                            }

                            if (multiEmployeesSubKpiScoreList.size() > 0) {
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

                                for (int i = 0; i < multiEmployeesSubKpiScoreList.size(); i++) {
                                    EmployeeSubKpiScore subKpiScore = multiEmployeesSubKpiScoreList.get(i);
                                    ArrayList<BarEntry> group = new ArrayList<>();

                                    String kpiTitle = null;

                                    // Pad the list with null items if necessary
                                    while (subKpiScore.getSubKpiPerformances().size() < maxSize) {
                                        subKpiScore.getSubKpiPerformances().add(new SubKpiScore());
                                    }

                                    if (i == 0) {
//                                        for (int c=0; c < employeeList.size(); c++){
//                                            if (employeeID == employeeList.get(c).getId()){
//                                                groupLabels.add(employeeList.get(c).getName());
//                                            }
//                                        }
                                        for (int j=0; j<customSpinnerAdapter.getSelectedEmployeeIds().size();j++){
                                            groupLabels.add(multiEmployeesKpiScoreList.get(j).getEmployee().getName());
                                        }
                                        // groupLabels.add(employee2.getName());
                                    }

                                    for (int k = 0; k < maxSize; k++) {
                                        groups.get(k).add(new BarEntry(xCounter, subKpiScore.getSubKpiPerformances().get(k).getScore()));
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
                                    BarDataSet barDataSet = new BarDataSet(groups.get(i), labels.get(i));
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
                            Log.e("","", ex);
                            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    },
                    errorMessage -> {
                        Log.e("", errorMessage);
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
            );
        }catch (Exception ex){
            Log.e("","", ex);
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateSingleSubKpiComparisonBarChart() {
        try {
            // Define the labels for each group
            List<String> groupLabels = new ArrayList<>();
            ArrayList<Integer> employeeIds = new ArrayList<>();
            employeeIds.addAll(customSpinnerAdapter.getSelectedEmployeeIds());

            EmployeeSubKpiScoreService employeeSubKpiScoreService = new EmployeeSubKpiScoreService(getContext());
            EmployeeIdsWithSession employeeIdsWithSession = new EmployeeIdsWithSession();
            employeeIdsWithSession.setEmployeeIds(employeeIds);
            employeeIdsWithSession.setSession_id(session.getId());

            employeeSubKpiScoreService.getSubKpiMultiEmployeePerformance(
                    employeeIds,
                    session.getId(),
                    employeeSubKpiScores -> {
                        try {
                            List<EmployeeSubKpiScore> multiEmployeesSubKpiScoreList = employeeSubKpiScores;
                            List<String> labels = new ArrayList<>();
                            int maxSize = 0;

                            // Collect unique sub-KPI names and determine the maximum size of sub-KPI performances
                            for (EmployeeSubKpiScore scores : multiEmployeesSubKpiScoreList) {
                                for (SubKpiScore s : scores.getSubKpiPerformances()) {
                                    if (!labels.contains(s.getName())) {
                                        labels.add(s.getName());
                                    }
                                }
                                maxSize = Math.max(maxSize, scores.getSubKpiPerformances().size());
                            }

                            if (multiEmployeesSubKpiScoreList.size() > 0) {
                                ArrayList<Integer> colors = null;
                                CommonMethods commonMethods = new CommonMethods();
                                colors = commonMethods.generateRandomColors(maxSize);
                                BarData barData = new BarData();

                                List<BarEntry> barEntries = new ArrayList<>();
                                int xCounter = 0;

                                for (int i = 0; i < multiEmployeesSubKpiScoreList.size(); i++) {
                                    EmployeeSubKpiScore subKpiScore = multiEmployeesSubKpiScoreList.get(i);
                                    String employeeName = subKpiScore.getEmployee().getName();
                                    groupLabels.add(employeeName);

                                    boolean found = false;
                                    for (SubKpiScore score : subKpiScore.getSubKpiPerformances()) {
                                        if (subKpi.getId() == score.getSubKpi_id()) {
                                            barEntries.add(new BarEntry(xCounter, score.getScore()));
                                            found = true;
                                            break;
                                        }
                                    }
                                    if (!found) {
                                        barEntries.add(new BarEntry(xCounter, 0f)); // Default to 0 if no performance found
                                    }
                                    xCounter++;
                                }

                                BarDataSet barDataSet = new BarDataSet(barEntries, "Sub-KPI Scores");
                                barDataSet.setColors(colors);
                                barData.addDataSet(barDataSet);

                                float barWidth = 0.2f; // width of each bar

                                barData.setBarWidth(barWidth);
                                barChart.setData(barData);
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
                        } catch (Exception ex) {
                            Log.e("", "", ex);
                            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    },
                    errorMessage -> {
                        Log.e("", errorMessage);
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
            );
        } catch (Exception ex) {
            Log.e("", "", ex);
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    public void updateCourseComparisonBarChart(){
        // pieChart.setVisibility(View.GONE);
        // sessionLayout.setVisibility(View.VISIBLE);
        ArrayList<Integer> employeeIds = new ArrayList<>();
        ArrayList<Integer> courseIds = new ArrayList<>();


        // barChart.setVisibility(View.VISIBLE);
        // employeeLayout.setVisibility(View.VISIBLE);
        // courseLayout.setVisibility(View.VISIBLE);
        try {
            // Define the labels for each group
            List<String> groupLabels = new ArrayList<>();
            // Log.i("",customSpinnerAdapter.getSelectedEmployeeIds().get(0).toString());
            employeeIds.addAll(customSpinnerAdapter.getSelectedEmployeeIds());
            // employeeIds.remove(new int[]{0});

            courseIds.addAll(customCourseSpinnerAdapter.getSelectedCourseIds());

            // employeeIds.add(employee2.getId());

            MultiEmployeeCoursePerformanceRequest multiEmployeeCoursePerformanceRequest = new MultiEmployeeCoursePerformanceRequest(courseIds, session.getId(), employeeIds);

            employeeCoursePerformanceService.getMultiEmployeeCoursePerformance(
                    multiEmployeeCoursePerformanceRequest,
                    employeeCourseScores -> {
                        try {
                            employeeCourseScoresList = employeeCourseScores;
                            List<String> labels = new ArrayList<>();
                            int maxSize = 0;

                            // Determine the maximum size of the groups and labels
                            for (EmployeeCourseScore scores : employeeCourseScoresList) {
                                int newSize = scores.getPerformance().size();
                                int count = 1;
                                if (newSize > maxSize) {
                                    maxSize = newSize;
                                    labels.clear();
                                    for (CourseScore s : scores.getPerformance()) {
                                        labels.add(s.getCourse().getName());
                                        count++;
                                    }
                                }
                            }

                            if (employeeCourseScoresList.size() > 0) {
                                CommonMethods commonMethods = new CommonMethods();
                                ArrayList<Integer> colors = commonMethods.generateRandomColors(maxSize);

                                List<List<BarEntry>> groups = new ArrayList<>();

                                for (int i = 0; i < maxSize; i++) {
                                    groups.add(new ArrayList<>());
                                }

                                int xCounter = 0;
                                int xInitialCounter = 1;

                                for (int i = 0; i < employeeCourseScoresList.size(); i++) {
                                    List<CourseScore> courseScores = employeeCourseScoresList.get(i).getPerformance();

                                    // Pad the list with empty QuestionScore if necessary
                                    while (courseScores.size() < maxSize) {
                                        courseScores.add(new CourseScore());
                                    }

                                    if (i == 0) {
                                        for (int j=0; j<customSpinnerAdapter.getSelectedEmployeeIds().size();j++){
                                            groupLabels.add(employeeCourseScoresList.get(j).getEmployee().getName());
                                        }
                                    }

                                    for (int k = 0; k < maxSize; k++) {
                                        groups.get(k).add(new BarEntry(xCounter, (float) courseScores.get(k).getAverage()));
                                        xCounter += 3;
                                    }

                                    xCounter = xInitialCounter;
                                    xInitialCounter++;
                                }

                                BarData barData = new BarData();

                                for (int i = 0; i < groups.size(); i++) {
                                    BarDataSet barDataSet = new BarDataSet(groups.get(i), labels.get(i));
                                    barDataSet.setColor(colors.get(i));
                                    barData.addDataSet(barDataSet);
                                }

                                // Ensure at least two datasets
                                while (barData.getDataSetCount() < 2) {
                                    barData.addDataSet(new BarDataSet(new ArrayList<>(), ""));
                                }

                                float groupSpace = 0.5f;
                                float barSpace = 0.02f;
                                float barWidth = 0.2f;

                                barData.setBarWidth(barWidth);
                                barChart.setData(barData);
                                barChart.groupBars(0, groupSpace, barSpace);
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
                                            return groupLabels.get(index);
                                        } else {
                                            return "";
                                        }
                                    }
                                });
                            }
                        } catch (Exception ex) {
                            Log.e("Error","...", ex);
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

    private void updateKpiComparisonBarChart() {
        // sessionLayout.setVisibility(View.VISIBLE);

        // barChart.setVisibility(View.VISIBLE);
        // employeeLayout.setVisibility(View.VISIBLE);
        // courseLayout.setVisibility(View.GONE);

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
            // employeeIds.remove(new int[]{0});
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
                            for (EmployeeKpiScore scores : multiEmployeesKpiScoreList) {
                                int newSize = Math.max(maxSize, scores.getKpiScores().size());
                                if (newSize > maxSize){
                                    kpiLabels.clear();
                                    for (KpiScore s:scores.getKpiScores()) {
                                        kpiLabels.add(s.getKpi_title());
                                    }
                                }
                                maxSize = Math.max(maxSize, scores.getKpiScores().size());
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
                                    EmployeeKpiScore kpiScore = multiEmployeesKpiScoreList.get(i);
                                    ArrayList<BarEntry> group = new ArrayList<>();

                                    String kpiTitle = null;

                                    // Pad the list with null items if necessary
                                    while (kpiScore.getKpiScores().size() < maxSize) {
                                        kpiScore.getKpiScores().add(new KpiScore());
                                    }

                                    if (i == 0) {
//                                        for (int c=0; c < employeeList.size(); c++){
//                                            if (employeeID == employeeList.get(c).getId()){
//                                                groupLabels.add(employeeList.get(c).getName());
//                                            }
//                                        }
                                        for (int j=0; j<customSpinnerAdapter.getSelectedEmployeeIds().size();j++){
                                            groupLabels.add(multiEmployeesKpiScoreList.get(j).getEmployee().getName());
                                        }
                                        // groupLabels.add(employee2.getName());
                                    }

                                    for (int k = 0; k < maxSize; k++) {
                                        groups.get(k).add(new BarEntry(xCounter, kpiScore.getKpiScores().get(k).getScore()));
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
                            Log.e("","", ex);
                            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    },
                    errorMessage -> {
                        Log.e("", errorMessage);
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
            );
        }catch (Exception ex){
            Log.e("","", ex);
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateYearlyKpiComparisonBarChart() {
        // sessionLayout.setVisibility(View.VISIBLE);

        // barChart.setVisibility(View.VISIBLE);
        // employeeLayout.setVisibility(View.VISIBLE);
        // courseLayout.setVisibility(View.GONE);

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
            // employeeIds.remove(new int[]{0});
            // employeeIds.add(employee2.getId());

            EmployeeKpiScoreService employeeKpiScoreService = new EmployeeKpiScoreService(getContext());
            EmployeeIdsWithSession employeeIdsWithSession = new EmployeeIdsWithSession();
            employeeIdsWithSession.setEmployeeIds(employeeIds);
            employeeIdsWithSession.setSession_id(session.getId());

            employeeKpiScoreService.compareYearlyEmployeeKpiScore(
                    employeeIds,
                    year,
                    kpi.getId(),
                    employeeKpiScores -> {
                        // TODO
                        try {
                            multiEmployeesKpiScoreList = employeeKpiScores;
                            List<String> labels = new ArrayList<>();
                            // Determine the maximum size of the groups
                            int maxSize = 0;
                            for (EmployeeKpiScore scores : multiEmployeesKpiScoreList) {
                                int newSize = Math.max(maxSize, scores.getKpiScores().size());
                                if (newSize > maxSize){
                                    labels.clear();
                                    for (KpiScore s:scores.getKpiScores()) {
                                        labels.add(s.getSession_title());
                                    }
                                }
                                maxSize = Math.max(maxSize, scores.getKpiScores().size());
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
                                    EmployeeKpiScore kpiScore = multiEmployeesKpiScoreList.get(i);
                                    ArrayList<BarEntry> group = new ArrayList<>();

                                    String kpiTitle = null;

                                    // Pad the list with null items if necessary
                                    while (kpiScore.getKpiScores().size() < maxSize) {
                                        kpiScore.getKpiScores().add(new KpiScore());
                                    }

                                    if (i == 0) {
//                                        for (int c=0; c < employeeList.size(); c++){
//                                            if (employeeID == employeeList.get(c).getId()){
//                                                groupLabels.add(employeeList.get(c).getName());
//                                            }
//                                        }
                                        for (int j=0; j<customSpinnerAdapter.getSelectedEmployeeIds().size();j++){
                                            groupLabels.add(multiEmployeesKpiScoreList.get(j).getEmployee().getName());
                                        }
                                        // groupLabels.add(employee2.getName());
                                    }

                                    for (int k = 0; k < maxSize; k++) {
                                        groups.get(k).add(new BarEntry(xCounter, kpiScore.getKpiScores().get(k).getScore()));
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
                                    BarDataSet barDataSet = new BarDataSet(groups.get(i), labels.get(i));
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
                            Log.e("","", ex);
                            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    },
                    errorMessage -> {
                        Log.e("", errorMessage);
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
            );
        }catch (Exception ex){
            Log.e("","", ex);
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateQuestionComparisonBarChart() {
        // Ensure the spinners have data
//    if (employeeSpinner.getAdapter().isEmpty() || sessionSpinner.getAdapter().isEmpty() || courseSpinner.getAdapter().isEmpty()){
//        return;
//    }

        try {
            questionnaireTypeLayout.setVisibility(View.VISIBLE);
            List<String> groupLabels = new ArrayList<>();
            ArrayList<Integer> employeeIds = new ArrayList<>();

            // Get selected employee IDs and remove default value if necessary
            employeeIds.addAll(customSpinnerAdapter.getSelectedEmployeeIds());
//        if (employeeIds.contains(0)) {
//            employeeIds.remove((Integer) 0);
//        }

            int evaluationTypeId = questionnaireTypeList.get(questionnaireTypeSpinner.getSelectedItemPosition()).getId();

            EmployeeQuestionScoreService employeeQuestionScoreService = new EmployeeQuestionScoreService(getContext());
            QuestionsScoresRequest questionsScoresRequest = new QuestionsScoresRequest();
            questionsScoresRequest.setEmployeeIDs(employeeIds);
            questionsScoresRequest.setSessionID(session.getId());
            questionsScoresRequest.setEvaluationTypeID(evaluationTypeId);

            employeeQuestionScoreService.getMultiEmployeeQuestionsScores(
                    questionsScoresRequest,
                    employeeQuestionsScores -> {
                        try {
                            employeeQuestionsScoresList = employeeQuestionsScores;
                            List<String> labels = new ArrayList<>();
                            int maxSize = 0;

                            // Determine the maximum size of the groups and labels
                            for (EmployeeQuestionsScores scores : employeeQuestionsScoresList) {
                                int newSize = scores.getQuestionScores().size();
                                int count = 1;
                                if (newSize > maxSize) {
                                    maxSize = newSize;
                                    labels.clear();
                                    for (QuestionScore s : scores.getQuestionScores()) {
                                        labels.add("Q" + count);
                                        count++;
                                    }
                                }
                            }

                            if (employeeQuestionsScoresList.size() > 0) {
                                CommonMethods commonMethods = new CommonMethods();
                                ArrayList<Integer> colors = commonMethods.generateRandomColors(maxSize);

                                List<List<BarEntry>> groups = new ArrayList<>();

                                for (int i = 0; i < maxSize; i++) {
                                    groups.add(new ArrayList<>());
                                }

                                int xCounter = 0;
                                int xInitialCounter = 1;

                                for (int i = 0; i < employeeQuestionsScoresList.size(); i++) {
                                    List<QuestionScore> questionScores = employeeQuestionsScoresList.get(i).getQuestionScores();

                                    // Pad the list with empty QuestionScore if necessary
                                    while (questionScores.size() < maxSize) {
                                        questionScores.add(new QuestionScore());
                                    }

                                    if (i == 0) {
                                        for (int j = 0; j < customSpinnerAdapter.getSelectedEmployeeIds().size(); j++) {
                                            groupLabels.add(employeeQuestionsScoresList.get(j).getEmployee().getName());
                                        }
                                    }

                                    for (int k = 0; k < maxSize; k++) {
                                        groups.get(k).add(new BarEntry(xCounter, (float) questionScores.get(k).getAverage()));
                                        xCounter += 3;
                                    }

                                    xCounter = xInitialCounter;
                                    xInitialCounter++;
                                }

                                BarData barData = new BarData();

                                for (int i = 0; i < groups.size(); i++) {
                                    BarDataSet barDataSet = new BarDataSet(groups.get(i), labels.get(i));
                                    barDataSet.setColor(colors.get(i));
                                    barData.addDataSet(barDataSet);
                                }

                                // Ensure at least two datasets
                                while (barData.getDataSetCount() < 2) {
                                    barData.addDataSet(new BarDataSet(new ArrayList<>(), ""));
                                }

                                float groupSpace = 0.5f;
                                float barSpace = 0.02f;
                                float barWidth = 0.2f;

                                barData.setBarWidth(barWidth);
                                barChart.setData(barData);
                                barChart.groupBars(0, groupSpace, barSpace);
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
                                            return groupLabels.get(index);
                                        } else {
                                            return "";
                                        }
                                    }
                                });

                                // Set up the click listener for the bars
                                barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                                    @Override
                                    public void onValueSelected(Entry e, Highlight h) {
                                        int xIndex = (int) e.getX() / 3; // Adjust based on how the xCounter was incremented
                                        int employeeIndex = xIndex; // Adjust based on your logic

                                        if (employeeIndex >= 0 && employeeIndex < employeeQuestionsScoresList.size()) {
                                            EmployeeQuestionsScores selectedScore = employeeQuestionsScoresList.get(employeeIndex);
                                            Fragment newFragment = new ScoresFragment();

                                            Bundle bundle = new Bundle();
                                            bundle.putString("employeeName", selectedScore.getEmployee().getName());
                                            bundle.putInt("employeeId", selectedScore.getEmployee().getId());
                                            newFragment.setArguments(bundle);

                                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                            transaction.replace(R.id.fragment_container, newFragment);
                                            transaction.addToBackStack(null);
                                            transaction.commit();
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected() {
                                        // Do nothing
                                    }
                                });

                            }
                        } catch (Exception ex) {
                            Log.e("Error", "...", ex);
                            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    },
                    errorMessage -> {
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
            );
        } catch (Exception ex) {
            Log.e("", "", ex);
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void updateChart(){
        if (isKpi){
            updateKpiComparisonBarChart();
        } else if (isSubKpi) {
            updateSubKpiComparisonBarChart();
        } else if (isCourse) {
            updateCourseComparisonBarChart();
        } else if (isQuestion) {
            updateQuestionComparisonBarChart();
        } else if (isSingleSubKpi) {
            updateSingleSubKpiComparisonBarChart();
        } else if (isYearlyKpi) {
            updateYearlyKpiComparisonBarChart();
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