package com.example.biitemployeeperformanceappraisalsystem;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
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

public class QuestionnaireFragment extends Fragment implements QuestionnaireAdapter.OnQuestionActionListener {

    int questionnaireTypeSpinnerSelectedItemId;
    private List<Question> questionsList;
    private List<QuestionnaireType> questionnaireTypes;
    ListView questionnaireListView;
    Spinner questionnaireTypeSpinner;
    private QuestionnaireService questionnaire;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_questionnaire, container, false);

        questionnaireListView = view.findViewById(R.id.questtionnaire_list_view);
        questionnaireTypeSpinner = view.findViewById(R.id.quetionnaire_type_spinner);
        questionnaire = new QuestionnaireService(view.getContext());

        questionnaire.getQuestionnaireType(
                questionnaireTypeList -> {
                    questionnaireTypes = questionnaireTypeList;
                    questionnaire.populateSpinner(questionnaireTypes, questionnaireTypeSpinner);
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                }
        );

        questionnaireTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                questionnaireTypeSpinnerSelectedItemId = questionnaireTypes.get(position).getId();
                questionnaire.getQuestionnaireByType(
                        questionnaireTypes.get(position).getId(),
                        questions -> {
                            questionsList = questions;
                            QuestionnaireAdapter adapter = new QuestionnaireAdapter(view.getContext(), R.layout.questtionnaire_list_item_layout, questions, QuestionnaireFragment.this);
                            questionnaireListView.setAdapter(adapter);
                        },
                        errorMessage -> {
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }
                );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button showModalButton = view.findViewById(R.id.btn_add_question);
        showModalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQuestionDialog(null, -1);
            }
        });

        return view;
    }

    private void showQuestionDialog(@Nullable Question question, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.question_modal_layout, null);
        builder.setView(dialogView);

        TextView questionTextView = dialogView.findViewById(R.id.text_question);
        if (question != null) {
            questionTextView.setText(question.getQuestion());
        }

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog alertDialog = (AlertDialog) dialog;
                String questionText = questionTextView.getText().toString().trim();
                if (question == null) {
                    // Adding a new question
                    Question newQuestion = new Question();
                    newQuestion.setQuestion(questionText);
                    newQuestion.setType_id(questionnaireTypeSpinnerSelectedItemId);
                    newQuestion.setDeleted(false);
                    questionnaire.postQuestion(
                            newQuestion,
                            postedQuestion -> {
                                questionsList.add(postedQuestion);
                                ((QuestionnaireAdapter) questionnaireListView.getAdapter()).notifyDataSetChanged();
                            },
                            errorMessage -> {
                                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                            }
                    );
                } else {
                    // Updating an existing question
                    question.setQuestion(questionText);
                    questionnaire.putQuestion(
                            question,
                            updatedQuestion -> {
                                questionsList.set(position, updatedQuestion);
                                ((QuestionnaireAdapter) questionnaireListView.getAdapter()).notifyDataSetChanged();
                            },
                            errorMessage -> {
                                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                            }
                    );
                }
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    @Override
    public void onEditQuestion(Question question, int position) {
        showQuestionDialog(question, position);
    }
}
