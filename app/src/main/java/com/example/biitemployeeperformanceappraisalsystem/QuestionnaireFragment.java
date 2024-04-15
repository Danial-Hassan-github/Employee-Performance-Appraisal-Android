package com.example.biitemployeeperformanceappraisalsystem;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.adapter.QuestionnaireAdapter;
import com.example.biitemployeeperformanceappraisalsystem.models.Question;
import com.example.biitemployeeperformanceappraisalsystem.models.QuestionnaireType;
import com.example.biitemployeeperformanceappraisalsystem.network.services.QuestionnaireService;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionnaireFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionnaireFragment extends Fragment {

    int questionnaireTypeSpinnerSelectedItemId;
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

        QuestionnaireService questionnaire=new QuestionnaireService(view.getContext());

//        questionnaire.getConfidentialQuestions(
//                // onSuccess callback
//                questions -> {
//                    questionsList = questions;
//                    // Create ArrayAdapter and set it to the ListView
//                    QuestionAdapter adapter =  new QuestionAdapter(view.getContext(),R.layout.questtionnaire_list_item_layout,questions);
//                    questionnaireListView.setAdapter(adapter);
//                },
//                // onFailure callback
//                errorMessage -> {
//                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
//                });

        questionnaire.getQuestionnaireType(
                questionnaireTypeList -> {
                    questionnaireTypes = questionnaireTypeList;
                    questionnaire.populateSpinner(questionnaireTypes,questionnaireTypeSpinner);
                },
                // onFailure callback
                errorMessage -> {
                    // Handle failure
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                }
        );

        questionnaireTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                questionnaireTypeSpinnerSelectedItemId = questionnaireTypes.get(position).getId();
                questionnaire.getQuestionnaireByType(
                        questionnaireTypes.get(position).getId(),
                        // onSuccess callback
                        questions -> {
                            questionsList = questions;
                            // Create ArrayAdapter and set it to the ListView
                            QuestionnaireAdapter adapter =  new QuestionnaireAdapter(view.getContext(),R.layout.questtionnaire_list_item_layout,questions);
                            questionnaireListView.setAdapter(adapter);
                        },
                        // onFailure callback
                        errorMessage -> {
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }
                        );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Modal code
        // Inside your activity or fragment
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(R.layout.question_modal_layout);


        AlertDialog dialog = builder.create();

        Button showModalButton = view.findViewById(R.id.btn_add_question);
        showModalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflate the layout for the dialog
                View dialogView = getLayoutInflater().inflate(R.layout.question_modal_layout, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(dialogView);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle Save button click
                        AlertDialog alertDialog = (AlertDialog) dialog;
                        TextView questionTextView = alertDialog.findViewById(R.id.text_question);
                        Question question = new Question();
                        question.setQuestion(questionTextView.toString().trim());
                        question.setId(questionnaireTypeSpinnerSelectedItemId);
                        questionnaire.postQuestion(
                                question,
                                question1 -> {
                                    questionsList.add(question1);
                                },
                                errorMessage -> {
                                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                                }
                        );

                        // Get the description text and save the task
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        // Inflate the layout for this fragment
        return view;
    }
}