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
import com.example.biitemployeeperformanceappraisalsystem.adapter.TaskAdapter;
import com.example.biitemployeeperformanceappraisalsystem.models.Question;
import com.example.biitemployeeperformanceappraisalsystem.models.QuestionnaireType;
import com.example.biitemployeeperformanceappraisalsystem.network.CommonData;
import com.example.biitemployeeperformanceappraisalsystem.network.Questionnaire;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionnaireFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionnaireFragment extends Fragment {

    private List<Question> questionsList;
    private List<QuestionnaireType> questionnaireTypes;
    ListView questionnaireListView;
    Spinner questionnaireTypeSpinner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_questionnaire, container, false);

        questionnaireListView=view.findViewById(R.id.questtionnaire_list_view);
        questionnaireTypeSpinner=view.findViewById(R.id.quetionnaire_type_spinner);

        Questionnaire questionnaire=new Questionnaire(view.getContext());

        questionnaire.getConfidentialQuestions(
                // onSuccess callback
                questions -> {
                    questionsList = questions;
                    // Create ArrayAdapter and set it to the ListView
                    QuestionAdapter adapter =  new QuestionAdapter(view.getContext(),R.layout.questtionnaire_list_item_layout,questions);
                    questionnaireListView.setAdapter(adapter);
                },
                // onFailure callback
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                });

        questionnaire.getQuestionnaireType(
                questionnaireTypeList -> {
                    // Handle the list of sessions here
                    questionnaireTypes = questionnaireTypeList;
                    // Populate the spinner with questionnaire Type titles
                    questionnaire.populateSpinner(questionnaireTypes,questionnaireTypeSpinner);
                },
                // onFailure callback
                errorMessage -> {
                    // Handle failure
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                }
        );

        // Inflate the layout for this fragment
        return view;
    }
}