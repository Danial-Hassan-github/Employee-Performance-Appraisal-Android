package com.example.biitemployeeperformanceappraisalsystem.adapter;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.models.OptionsWeightage;
import com.example.biitemployeeperformanceappraisalsystem.models.Question;
import com.example.biitemployeeperformanceappraisalsystem.network.services.QuestionnaireService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EvaluationQuestionnaireAdapter extends ArrayAdapter<Question> {
    private LayoutInflater inflater;
    QuestionnaireService questionnaireService;
    private int resourceId;
    View btnSubmit;
    List<OptionsWeightage> optionsWeightageList;
    View parentView;
    // AppCompatButton btnSubmit;
    private boolean allQuestionsAnswered = false;
    private HashMap<Integer, Integer> selectedOptions = new HashMap<>();
    public EvaluationQuestionnaireAdapter(Context context, int resourceId, List<Question> questions, View parentView) {
        super(context, resourceId, questions);
        this.inflater = LayoutInflater.from(context);
        this.resourceId = resourceId;
        this.parentView = parentView;
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
            btnSubmit = parentView.findViewById(R.id.btn_submit);
        }

        // Lookup view for data population
        TextView questionTextView = convertView.findViewById(R.id.text_question);
        RadioGroup optionsRadioGroup = convertView.findViewById(R.id.options_radio_group);
        // convertView.getRootView().findViewById(R.id.btn_submit);
        // View parentView = inflater.inflate(R.layout.fragment_evaluation_questionnaire,null,false);
        // btnSubmit = parentView.findViewById(R.id.btn_submit);
        // AppCompatButton btnSubmit = convertView.getRootView().findViewById(R.id.btn_submit);

        questionTextView.setText(question.getQuestion());

        if (optionsWeightageList == null) {
            questionnaireService.getOptionsWeightages(
                    optionsWeightages -> {
                        optionsWeightageList = optionsWeightages;
                        // Dynamically create and add radio buttons
                        for (OptionsWeightage option : optionsWeightageList) {
                            RadioButton radioButton = new RadioButton(getContext());
                            radioButton.setText(option.getName());
                            optionsRadioGroup.addView(radioButton);
                        }
                    },
                    errorMessage -> {
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT);
                    }
            );
        }

        optionsRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int selectedOptionIndex = group.indexOfChild(group.findViewById(checkedId));
            selectedOptions.put(position, optionsWeightageList.get(selectedOptionIndex).getWeightage());
            Toast.makeText(getContext(), selectedOptionIndex+"", Toast.LENGTH_SHORT).show();
            // int count = getCount();
            if (selectedOptions.size() == getCount()){
                btnSubmit.setEnabled(true);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }

//    private void checkAllQuestionsAnswered() {
//        allQuestionsAnswered = selectedOptions.size() == getCount();
//        btnSubmit.setEnabled(allQuestionsAnswered);
//    }

    public List<Pair<Integer, Integer>> getSelectedAnswers() {
        List<Pair<Integer, Integer>> selectedAnswers = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : selectedOptions.entrySet()) {
            int questionId = getItem(entry.getKey()).getId();
            selectedAnswers.add(new Pair<>(questionId, entry.getValue()));
        }
        return selectedAnswers;
    }

}
