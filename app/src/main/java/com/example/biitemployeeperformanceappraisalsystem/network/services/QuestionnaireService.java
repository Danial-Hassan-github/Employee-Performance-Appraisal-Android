package com.example.biitemployeeperformanceappraisalsystem.network.services;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.models.OptionsWeightage;
import com.example.biitemployeeperformanceappraisalsystem.models.Question;
import com.example.biitemployeeperformanceappraisalsystem.models.QuestionnaireType;
import com.example.biitemployeeperformanceappraisalsystem.network.RetrofitClient;
import com.example.biitemployeeperformanceappraisalsystem.network.interfaces.QuestionnaireServiceListener;

import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionnaireService {
    QuestionnaireServiceListener questionnaireServiceListener;
    Context context;
    public QuestionnaireService(Context context){
        questionnaireServiceListener= RetrofitClient.getRetrofitInstance().create(QuestionnaireServiceListener.class);
        this.context=context;
    }

    public void postQuestion(Question question, final Consumer<Question> onSuccess, final Consumer<String> onFailure){
        Call<Question> questionCall = questionnaireServiceListener.postQuestion(question);
        questionCall.enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Call<Question> call, Response<Question> response) {
                if (response.isSuccessful()){
                    onSuccess.accept(response.body());
                }else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<Question> call, Throwable t) {
                onFailure.accept("Something went wrong while adding question");
            }
        });
    }

    public void getEvaluationQuestionnaire(String questionnaireType, final Consumer<List<Question>> onSuccess, final Consumer<String> onFailure){
        Call<List<Question>> questions = questionnaireServiceListener.getQuestionnaireByType(questionnaireType);
        questions.enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if (response.isSuccessful()) {
                    onSuccess.accept(response.body());
                } else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                onFailure.accept("Something went wrong while fetching questionnaire");
            }
        });
    }

    public void getQuestionnaireByType(int questionnaireTypeId, final Consumer<List<Question>> onSuccess, final Consumer<String> onFailure){
        Call<List<Question>> questions = questionnaireServiceListener.getQuestionnaireByTypeID(questionnaireTypeId);
        questions.enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if (response.isSuccessful()) {
                    onSuccess.accept(response.body());
                } else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                onFailure.accept("Something went wrong while fetching questionnaire");
            }
        });
    }

    public void getQuestionnaireType(final Consumer<List<QuestionnaireType>> onSuccess, final Consumer<String> onFailure){
        Call<List<QuestionnaireType>> questionnaireTypes = questionnaireServiceListener.getQuestionnaireTypes();
        questionnaireTypes.enqueue(new Callback<List<QuestionnaireType>>() {
            @Override
            public void onResponse(Call<List<QuestionnaireType>> call, Response<List<QuestionnaireType>> response) {
                if (response.isSuccessful()) {
                    onSuccess.accept(response.body());
                } else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<QuestionnaireType>> call, Throwable t) {
                onFailure.accept("Something went wrong while fetching employees");
            }
        });
    }

    public void getOptionsWeightages(final Consumer<List<OptionsWeightage>> onSuccess, final Consumer<String> onFailure){
        Call<List<OptionsWeightage>> optionsWeightage = questionnaireServiceListener.getOptionsWeightage();
        optionsWeightage.enqueue(new Callback<List<OptionsWeightage>>() {
            @Override
            public void onResponse(Call<List<OptionsWeightage>> call, Response<List<OptionsWeightage>> response) {
                if (response.isSuccessful()) {
                    onSuccess.accept(response.body());
                } else {
                    onFailure.accept(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<OptionsWeightage>> call, Throwable t) {
                onFailure.accept("Something went wrong while fetching employees");
            }
        });
    }

    public void populateSpinner(List<QuestionnaireType> questionnaireTypeList, Spinner spinner) {
        if (questionnaireTypeList != null && !questionnaireTypeList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, getQuestionnairTypeTitles(questionnaireTypeList));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        } else {
            Toast.makeText(context, "Questionnaire type list is empty", Toast.LENGTH_LONG).show();
        }
    }

    public String[] getQuestionnairTypeTitles(List<QuestionnaireType> questionnaireTypeList) {
        String[] titles = new String[questionnaireTypeList.size()];
        for (int i = 0; i < questionnaireTypeList.size(); i++) {
            titles[i] = questionnaireTypeList.get(i).getName();
        }
        return titles;
    }
}
