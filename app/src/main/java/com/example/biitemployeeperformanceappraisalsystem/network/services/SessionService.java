package com.example.biitemployeeperformanceappraisalsystem.network.services;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.helper.SharedPreferencesManager;
import com.example.biitemployeeperformanceappraisalsystem.models.Session;
import com.example.biitemployeeperformanceappraisalsystem.network.RetrofitClient;
import com.example.biitemployeeperformanceappraisalsystem.network.interfaces.SessionServiceListener;

import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SessionService {
    SessionServiceListener sessionServiceListener;
    Context context;

    public SessionService(Context context) {
        sessionServiceListener = RetrofitClient.getRetrofitInstance().create(SessionServiceListener.class);
        this.context = context;
    }

    public void getSessions(final Consumer<List<Session>> onSuccess, final Consumer<String> onFailure) {
        Call<List<Session>> sessionCall = sessionServiceListener.getSessions();
        sessionCall.enqueue(new Callback<List<Session>>() {
            @Override
            public void onResponse(Call<List<Session>> call, Response<List<Session>> response) {
                if (response.isSuccessful()) {
                    onSuccess.accept(response.body());
                } else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Session>> call, Throwable t) {
                onFailure.accept("Something went wrong while fetching sessions");
            }
        });
    }

    public void getYears(final Consumer<List<String>> onSuccess, final Consumer<String> onFailure) {
        Call<List<String>> sessionCall = sessionServiceListener.getYears();
        sessionCall.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()) {
                    onSuccess.accept(response.body());
                } else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                onFailure.accept("Something went wrong while fetching sessions");
            }
        });
    }

    public void getCurrentSession(Consumer<Session> onSuccess, Consumer<String> onFailure) {
        Call<Session> sessionCall = sessionServiceListener.getCurrentSession();
        sessionCall.enqueue(new Callback<Session>() {
            @Override
            public void onResponse(Call<Session> call, Response<Session> response) {
                if (response.isSuccessful()) {
                    Session session = response.body();
                    if (session != null) {
                        // Save session ID
                        SharedPreferencesManager sharedPreferencesManager = new SharedPreferencesManager(context.getApplicationContext());
                        sharedPreferencesManager.saveSessionId(session.getId());
                        // Execute onSuccess callback with the retrieved session
                        onSuccess.accept(session);
                    } else {
                        onFailure.accept("Session object is null");
                    }
                } else {
                    onFailure.accept("Failed to fetch session: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Session> call, Throwable t) {
                onFailure.accept("Something went wrong while fetching current session: " + t.getMessage());
            }
        });
    }


    public void postSession(Session session){
        Call<Session> sessionCall = sessionServiceListener.postSession(session);
        sessionCall.enqueue(new Callback<Session>() {
            @Override
            public void onResponse(Call<Session> call, Response<Session> response) {
                if (response.isSuccessful()){
                    Session session = response.body();
                    SharedPreferencesManager sharedPreferencesManager =new SharedPreferencesManager(context.getApplicationContext());
                    sharedPreferencesManager.saveSessionId(session.getId());
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Session> call, Throwable t) {
                Toast.makeText(context, "Something went wrong while Adding new session", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void populateSpinner(List<Session> sessionList, Spinner spinner) {
        if (sessionList != null && !sessionList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, getSessionTitles(sessionList));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        } else {
            Toast.makeText(context, "Session list is empty", Toast.LENGTH_LONG).show();
        }
    }

    public String[] getSessionTitles(List<Session> sessionList) {
        String[] titles = new String[sessionList.size()];
        for (int i = 0; i < sessionList.size(); i++) {
            titles[i] = sessionList.get(i).getTitle();
        }
        return titles;
    }
}
