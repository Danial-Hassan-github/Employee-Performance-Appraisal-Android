package com.example.biitemployeeperformanceappraisalsystem;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.adapter.QuestionAdapter;
import com.example.biitemployeeperformanceappraisalsystem.adapter.QuestionnaireAdapter;
import com.example.biitemployeeperformanceappraisalsystem.models.Question;
import com.example.biitemployeeperformanceappraisalsystem.models.QuestionnaireType;
import com.example.biitemployeeperformanceappraisalsystem.network.Questionnaire;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EvaluationQuestionnaireFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EvaluationQuestionnaireFragment extends Fragment {
    private List<Question> questionsList;
    ListView questionnaireListView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_evaluation_questionnaire, container, false);

        questionnaireListView=view.findViewById(R.id.evaluation_questionnaire_list_view);

        Questionnaire questionnaire=new Questionnaire(view.getContext());

        questionnaire.getConfidentialQuestions(
                // onSuccess callback
                questions -> {
                    questionsList = questions;
                    // Create ArrayAdapter and set it to the ListView
                    QuestionnaireAdapter adapter =  new QuestionnaireAdapter(view.getContext(),R.layout.evaluation_questionnaire_list_item_layout,questions);
                    questionnaireListView.setAdapter(adapter);
                },
                // onFailure callback
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                });

        // Inflate the layout for this fragment
        return view;
    }
}