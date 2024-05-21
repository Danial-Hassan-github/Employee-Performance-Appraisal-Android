package com.example.biitemployeeperformanceappraisalsystem.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.biitemployeeperformanceappraisalsystem.models.Employee;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeDetails;
import com.example.biitemployeeperformanceappraisalsystem.models.Student;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class SharedPreferencesManager {
    private static final String PREF_NAME = "MyAppPref";
    private static final String KEY_SESSION_ID = "sessionId";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_TYPE = "userType";
    private static final String KEY_USER_OBJECT = "userObject";
    private static final String KEY_IS_CONFIDENTIAL = "isConfidential";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;
    private Gson gson;

    // Constructor
    public SharedPreferencesManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
        gson = new Gson();
    }

    public void setKeyIsConfidential(Boolean isConfidential){
        editor.putBoolean(KEY_IS_CONFIDENTIAL, isConfidential);
        editor.apply();
    }

    public Boolean isConfidential(){
        return pref.getBoolean(KEY_IS_CONFIDENTIAL, false);
    }

    // Save student user details
    public void saveStudentUserDetails(Student student) {
        String userJson = gson.toJson(student);
        editor.putString(KEY_USER_OBJECT, userJson);
        editor.apply();
    }

    // Save employee user details
    public void saveEmployeeUserDetails(EmployeeDetails employee) {
        String userJson = gson.toJson(employee);
        editor.putString(KEY_USER_OBJECT, userJson);
        editor.apply();
    }

    public void saveSessionId(int sessionId){
        editor.putInt("sessionId", sessionId);
        editor.apply();
    }

    // Get session ID
    public int getSessionId() {
        return pref.getInt(KEY_SESSION_ID, 0);
    }
    // Get user ID
    public String getUserId() {
        return pref.getString(KEY_USER_ID, null);
    }

    // Get user type
    public String getUserType() {
        return pref.getString(KEY_USER_TYPE, null);
    }

    // Get student user object
    public Student getStudentUserObject() {
        String userJson = pref.getString(KEY_USER_OBJECT, null);
        if (userJson != null) {
            Type type = new TypeToken<Student>() {}.getType();
            return gson.fromJson(userJson, type);
        }
        return null;
    }

    // Get employee user object
    public EmployeeDetails getEmployeeUserObject() {
        String userJson = pref.getString(KEY_USER_OBJECT, null);
        if (userJson != null) {
            Type type = new TypeToken<EmployeeDetails>() {}.getType();
            return gson.fromJson(userJson, type);
        }
        return null;
    }

    // Clear session details (logout)
    public void logoutUser() {
        editor.clear();
        editor.apply();
    }
}
