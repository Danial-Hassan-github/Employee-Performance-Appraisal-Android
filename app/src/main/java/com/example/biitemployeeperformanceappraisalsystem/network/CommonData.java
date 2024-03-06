package com.example.biitemployeeperformanceappraisalsystem.network;

import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.director.DirectorMainActivity;
import com.example.biitemployeeperformanceappraisalsystem.hod.HodMainActivity;
import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeDetails;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeDetailsScore;
import com.example.biitemployeeperformanceappraisalsystem.models.Session;
import com.example.biitemployeeperformanceappraisalsystem.models.Student;
import com.example.biitemployeeperformanceappraisalsystem.models.TaskDetails;
import com.example.biitemployeeperformanceappraisalsystem.student.StudentMainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommonData {
    ApiNetwork apiNetwork;
    RetrofitClient retrofitClient;
    Context context;

    public CommonData(Context context) {
        retrofitClient = new RetrofitClient();
        apiNetwork = retrofitClient.getRetrofitInstance().create(ApiNetwork.class);
        this.context = context;
    }

    public void getEmployees(final Consumer<List<EmployeeDetails>> onSuccess, final Consumer<String> onFailure) {
        Call<List<EmployeeDetails>> employees = apiNetwork.getEmployees();
        employees.enqueue(new Callback<List<EmployeeDetails>>() {
            @Override
            public void onResponse(Call<List<EmployeeDetails>> call, Response<List<EmployeeDetails>> response) {
                if (response.isSuccessful()) {
                    onSuccess.accept(response.body());
                } else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<EmployeeDetails>> call, Throwable t) {
                onFailure.accept("Something went wrong while fetching employees");
            }
        });
    }

    public void GetEmployeesWithKpiScores(final Consumer<List<EmployeeDetailsScore>> onSuccess, final Consumer<String> onFailure) {
        Call<List<EmployeeDetailsScore>> employeeDetailsScoreList = apiNetwork.GetEmployeesWithKpiScores();
        employeeDetailsScoreList.enqueue(new Callback<List<EmployeeDetailsScore>>() {
            @Override
            public void onResponse(Call<List<EmployeeDetailsScore>> call, Response<List<EmployeeDetailsScore>> response) {
                if (response.isSuccessful()) {
                    onSuccess.accept(response.body());
                } else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<EmployeeDetailsScore>> call, Throwable t) {
                onFailure.accept("Something went wrong while fetching employees");
            }
        });
    }

    public void getTasks(final Consumer<List<TaskDetails>> onSuccess, final Consumer<String> onFailure) {
        Call<List<TaskDetails>> employees = apiNetwork.getTasks();
        employees.enqueue(new Callback<List<TaskDetails>>() {
            @Override
            public void onResponse(Call<List<TaskDetails>> call, Response<List<TaskDetails>> response) {
                if (response.isSuccessful()) {
                    onSuccess.accept(response.body());
                } else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<TaskDetails>> call, Throwable t) {
                onFailure.accept("Something went wrong while fetching tasks");
            }
        });
    }

    public List<String> generateNames() {
        List<String> names = new ArrayList<>();
        names.add("--Select Employee--");
        names.add("Munir Ahmed");
        names.add("Jamil Sarwar");
        names.add("Nadia");
        names.add("Naseer Ahmed Sajid");
        names.add("Qasim Shehzad");
        names.add("Muhammad Ihsan");
        names.add("Shahid Abid");
        names.add("Umar Farooq");
        names.add("Zahid Ahmad");
        names.add("Amir Rasheed");
        return names;
    }

    public List<String> generateEmployeeTypes() {
        List<String> employeeTypes = new ArrayList<>();
        employeeTypes.add("--Select Role--");
        employeeTypes.add("Senior Teacher");
        employeeTypes.add("Junior Teacher");
        employeeTypes.add("Engineer");
        employeeTypes.add("Salesperson");
        employeeTypes.add("Accountant");
        employeeTypes.add("Human Resources");
        employeeTypes.add("Consultant");
        employeeTypes.add("Analyst");
        employeeTypes.add("Designer");
        employeeTypes.add("Technician");
        employeeTypes.add("Administrator");
        return employeeTypes;
    }

    public List<String> generateDesignations() {
        List<String> designations = new ArrayList<>();
        designations.add("--Select Role--");
        designations.add("Teacher");
        designations.add("Supervisor");
        designations.add("Engineer");
        designations.add("Associate");
        designations.add("Analyst");
        designations.add("Specialist");
        designations.add("Coordinator");
        designations.add("Consultant");
        designations.add("Administrator");
        designations.add("Director");
        return designations;
    }

    private static final String[] COURSE_NAMES = {
            "Introduction to Computer Science",
            "Data Structures and Algorithms",
            "Computer Networks",
            "Database Management Systems",
            "Operating Systems",
            "Software Engineering",
            "Artificial Intelligence",
            "Machine Learning",
            "Web Development",
            "Cybersecurity",
            "Computer Graphics",
            "Computer Organization and Architecture",
            "Computer Vision",
            "Mobile Application Development",
    };

    public List<String> generateCourseNames() {
        List<String> courses = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 15; i++) {
            int index = random.nextInt(COURSE_NAMES.length);
            courses.add(COURSE_NAMES[index]);
        }
        return courses;
    }

    public List<String> getSubKPITypes() {
        List<String> subKPITypes = new ArrayList<>();
        subKPITypes.add("--Select SubKPi--");
        subKPITypes.add("Peer Evaluation");
        subKPITypes.add("CHR");
        subKPITypes.add("Student Evaluation");
        // Add more sub-KPI types if needed
        return subKPITypes;
    }

    public List<String> generateDepartments() {
        List<String> departments = new ArrayList<>();
        departments.add("--Select Department");
        departments.add("CS");
        departments.add("Marketing");
        departments.add("Human Resources");
        departments.add("Finance");
        departments.add("Information Technology");
        departments.add("Operations");
        departments.add("Sales");
        departments.add("Customer Service");
        departments.add("Research and Development");
        departments.add("Administration");
        return departments;
    }

    public void getSessionPerformance() {

    }

    public void getSessionsPerformance() {

    }
}
