package com.example.biitemployeeperformanceappraisalsystem.network.services;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

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
        Call<List<Session>> sessionCall = sessionServiceListener.GetSession();
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
