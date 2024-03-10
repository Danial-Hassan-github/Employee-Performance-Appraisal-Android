package com.example.biitemployeeperformanceappraisalsystem.helper;

import android.content.Context;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.biitemployeeperformanceappraisalsystem.models.Session;
import com.example.biitemployeeperformanceappraisalsystem.network.services.SessionService;

import java.util.List;
import java.util.function.Consumer;

public class SessionHelper {

    public static void handleSessions(Context context, List<Session> sessionList, Spinner sessionSpinner,
                                      AdapterView.OnItemSelectedListener listener,
                                      Consumer<List<Session>> onSuccess, Consumer<String> onFailure) {

        SessionService sessionService = new SessionService(context);

        sessionService.getSessions(sessions -> {
                    onSuccess.accept(sessions);
                    sessionService.populateSpinner(sessions, sessionSpinner);

                    sessionSpinner.setOnItemSelectedListener(listener);
                },
                onFailure::accept);
    }
}

