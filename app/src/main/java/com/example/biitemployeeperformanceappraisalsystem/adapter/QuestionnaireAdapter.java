package com.example.biitemployeeperformanceappraisalsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.models.Question;
import com.example.biitemployeeperformanceappraisalsystem.network.services.QuestionnaireService;

import java.util.List;

public class QuestionnaireAdapter extends ArrayAdapter<Question> {
    QuestionnaireService questionnaireService;
    private LayoutInflater inflater;
    private int resourceId;
    private List<Question> questions;
    private OnQuestionActionListener actionListener;

    public QuestionnaireAdapter(Context context, int resourceId, List<Question> questions, OnQuestionActionListener actionListener) {
        super(context, resourceId, questions);
        this.inflater = LayoutInflater.from(context);
        this.resourceId = resourceId;
        this.questions = questions;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        Question question = getItem(position);
        questionnaireService = new QuestionnaireService(getContext());

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.questtionnaire_list_item_layout, parent, false);
        }

        // Lookup view for data population
        TextView questionTextView = convertView.findViewById(R.id.text_question);
        ImageView btnEdit = convertView.findViewById(R.id.btn_edit_question);
        ImageView btnDelete = convertView.findViewById(R.id.btn_delete_question);

        // Populate the data into the template view using the data object
        questionTextView.setText(question.getQuestion());

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (actionListener != null) {
                    actionListener.onEditQuestion(question, position);
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionnaireService.deleteQuestion(
                        question.getId(),
                        question1 -> {
                            questions.remove(position);
                            notifyDataSetChanged();
                        },
                        errorMessage -> {
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }
                );
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }

    public interface OnQuestionActionListener {
        void onEditQuestion(Question question, int position);
    }
}
