package com.example.biitemployeeperformanceappraisalsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.models.Question;

import java.util.List;

public class QuestionnaireAdapter extends ArrayAdapter<Question> {
    private LayoutInflater inflater;
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

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.questtionnaire_list_item_layout, parent, false);
        }

        // Lookup view for data population
        TextView questionTextView = convertView.findViewById(R.id.text_question);
        ImageView optionsButton = convertView.findViewById(R.id.btn_options);

        // Populate the data into the template view using the data object
        questionTextView.setText(question.getQuestion());

        // Return the completed view to render on screen
        return convertView;
    }
}
