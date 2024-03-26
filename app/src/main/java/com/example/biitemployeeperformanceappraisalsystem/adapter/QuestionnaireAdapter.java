package com.example.biitemployeeperformanceappraisalsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.models.OptionsWeightage;
import com.example.biitemployeeperformanceappraisalsystem.models.Question;
import com.example.biitemployeeperformanceappraisalsystem.network.services.QuestionnaireService;

import java.util.List;

public class QuestionnaireAdapter extends ArrayAdapter<Question> {
    private LayoutInflater inflater;
    QuestionnaireService questionnaireService;
    private int resourceId;
    public QuestionnaireAdapter(Context context, int resourceId, List<Question> questions) {
        super(context, resourceId, questions);
        this.inflater = LayoutInflater.from(context);
        this.resourceId = resourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        Question question = getItem(position);
        questionnaireService = new QuestionnaireService(getContext());

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.evaluation_questionnaire_list_item_layout, parent, false);
        }

        // Lookup view for data population
        TextView questionTextView = convertView.findViewById(R.id.text_question);
        RadioGroup optionsRadioGroup = convertView.findViewById(R.id.options_radio_group);

        questionTextView.setText(question.getQuestion());

        questionnaireService.getOptionsWeightages(
                optionsWeightages -> {
                    // Dynamically create and add radio buttons
                    for (OptionsWeightage option : optionsWeightages) {
                        RadioButton radioButton = new RadioButton(getContext());
                        radioButton.setText(option.getName());
                        optionsRadioGroup.addView(radioButton);
                    }
                },
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT);
                }
        );
//        radioButton1.setText(question.getOptionsWeightage().get(0).getName());
//        radioButton2.setText(question.getOptionsWeightage().get(1).getName());
//        radioButton3.setText(question.getOptionsWeightage().get(2).getName());
//        radioButton4.setText(question.getOptionsWeightage().get(3).getName());

        // Return the completed view to render on screen
        return convertView;
    }
}
