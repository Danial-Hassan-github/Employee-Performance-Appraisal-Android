package com.example.biitemployeeperformanceappraisalsystem.director;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.models.Session;
import com.example.biitemployeeperformanceappraisalsystem.network.CommonData;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EvaluationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EvaluationFragment extends Fragment {

    List<Session> sessionList;
    Spinner sessionSpinner;
    private AppCompatButton btnPeerEvaluation, btnConfidentialEvaluation;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_evaluation, container, false);

        btnPeerEvaluation = view.findViewById(R.id.btn_peerEvaluation);
        btnConfidentialEvaluation = view.findViewById(R.id.btn_confidentialEvaluation);
        sessionSpinner = view.findViewById(R.id.spinner_session);
        listView = view.findViewById(R.id.list_view);

        DirectorMainActivity directorMainActivity = (DirectorMainActivity) getActivity();
        btnPeerEvaluation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directorMainActivity.replaceFragment(new PeerEvaluationSettingFragment());
            }
        });

        btnConfidentialEvaluation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directorMainActivity.replaceFragment(new ConfidentialEvaluationSettingFragment());
            }
        });

        CommonData data = new CommonData(view.getContext());
        data.getSessions(sessions -> {
                    // Handle the list of sessions here
                    sessionList = sessions;
                    // Populate the spinner with session titles
                    CommonData commonData = new CommonData(view.getContext());
                    commonData.populateSpinner(sessions, sessionSpinner);

                    // Set an item selected listener for the session spinner
                    sessionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            // Get the selected session
                            Session selectedSession = sessionList.get(position);
                            // Use the ID of the selected session
                            int sessionId = selectedSession.getId();
                            // Perform actions with the session ID
                            Toast.makeText(getContext(), sessionId+"", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // Handle case where nothing is selected
                        }
                    });
                },
                // onFailure callback
                errorMessage -> {
                    // Handle failure
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                });

        //Setting data in listview
        ArrayList<String> evaluations = new ArrayList<String>();
        evaluations.add("Peer\nStart:xyz\nEnd\nxyz");
        evaluations.add("Confidential\nStart:xyz\nEnd\nxyz");
        ArrayAdapter<String> evaluationTimeDetails = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, evaluations);
        listView.setAdapter(evaluationTimeDetails);
        // Inflate the layout for this fragment
        return view;
    }
}
