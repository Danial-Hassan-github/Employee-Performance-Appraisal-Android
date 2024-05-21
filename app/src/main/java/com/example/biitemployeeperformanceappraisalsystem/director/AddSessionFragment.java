package com.example.biitemployeeperformanceappraisalsystem.director;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.models.Session;
import com.example.biitemployeeperformanceappraisalsystem.network.services.SessionService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddSessionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddSessionFragment extends Fragment {

    EditText editTextSessionTitle;
    Button btnSave;
    SessionService sessionService;
    public static AddSessionFragment newInstance(String param1, String param2) {
        AddSessionFragment fragment = new AddSessionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionService = new SessionService(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_session, container, false);
        editTextSessionTitle = view.findViewById(R.id.edit_text_session_title);
        btnSave = view.findViewById(R.id.btn_save_session);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtSessionTitle = editTextSessionTitle.getText().toString();
                Session session = new Session();
                session.setTitle(txtSessionTitle);
                sessionService.postSession(session);
            }
        });
        return view;
    }
}