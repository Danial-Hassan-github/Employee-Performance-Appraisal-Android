package com.example.biitemployeeperformanceappraisalsystem.network;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeDetails;
import com.example.biitemployeeperformanceappraisalsystem.models.Question;
import com.example.biitemployeeperformanceappraisalsystem.models.QuestionnaireType;
import com.example.biitemployeeperformanceappraisalsystem.models.Session;

import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Questionnaire {
    ApiNetwork apiNetwork;
    RetrofitClient retrofitClient;
    Context context;
    public Questionnaire(Context context){
        retrofitClient = new RetrofitClient();
        apiNetwork=retrofitClient.getRetrofitInstance().create(ApiNetwork.class);
        this.context=context;
    }

    public void getConfidentialQuestions(final Consumer<List<Question>> onSuccess, final Consumer<String> onFailure){
        Call<List<Question>> employees = apiNetwork.GetConfidentialQuestions();
        employees.enqueue(new Callback<List<Question>>() {
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
                onFailure.accept("Something went wrong while fetching employees");
            }
        });
    }

    public void getQuestionnaireType(final Consumer<List<QuestionnaireType>> onSuccess, final Consumer<String> onFailure){
        Call<List<QuestionnaireType>> questionnaireTypes = apiNetwork.getQuestionnaireTypes();
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
