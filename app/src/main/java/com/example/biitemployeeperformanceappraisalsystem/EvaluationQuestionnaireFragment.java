package com.example.biitemployeeperformanceappraisalsystem;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.adapter.QuestionnaireAdapter;
import com.example.biitemployeeperformanceappraisalsystem.helper.SharedPreferencesManager;
import com.example.biitemployeeperformanceappraisalsystem.models.Question;
import com.example.biitemployeeperformanceappraisalsystem.models.StudentEvaluation;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EvaluationService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.QuestionnaireService;
import com.example.biitemployeeperformanceappraisalsystem.student.CourseTeacherFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EvaluationQuestionnaireFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EvaluationQuestionnaireFragment extends Fragment {
    int evaluatorID, evaluateeID, courseID;
    private List<Question> questionsList;
    ListView questionnaireListView;
    Button btnSubmit;
    SharedPreferencesManager sharedPreferencesManager;
    EvaluationService evaluationService;
    boolean allQuestionsAnswered = false;
    public EvaluationQuestionnaireFragment(int evaluateeID, int courseID){
        this.evaluateeID=evaluateeID;
        this.courseID=courseID;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_evaluation_questionnaire, container, false);
        sharedPreferencesManager = new SharedPreferencesManager(getContext());
        evaluationService = new EvaluationService(getContext());

        questionnaireListView=view.findViewById(R.id.evaluation_questionnaire_list_view);
        btnSubmit=view.findViewById(R.id.btn_submit);

        QuestionnaireService questionnaire=new QuestionnaireService(view.getContext());

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

        btnSubmit.setOnClickListener(v -> {
            List<Pair<Integer, Integer>> selectedAnswers = ((QuestionnaireAdapter) questionnaireListView.getAdapter()).getSelectedAnswers();

            List<StudentEvaluation> studentEvaluations = new ArrayList<>();
            for (Pair<Integer, Integer> pair : selectedAnswers) {
                StudentEvaluation studentEvaluation = new StudentEvaluation();
                studentEvaluation.setStudent_id(sharedPreferencesManager.getStudentUserObject().getId()); // Set your student ID
                studentEvaluation.setSession_id(sharedPreferencesManager.getSessionId()); // Set your session ID
                studentEvaluation.setTeacher_id(evaluateeID); // Set the teacher ID (evaluatee ID) from the fragment constructor
                studentEvaluation.setQuestion_id(pair.first); // Set the question ID from the selected answers
                studentEvaluation.setScore(pair.second); // Set the score from the selected answers
                studentEvaluation.setCourse_id(courseID); // Set the course ID from the fragment constructor
                studentEvaluations.add(studentEvaluation);
                Toast.makeText(getContext(), studentEvaluation.getStudent_id() + "\n" + studentEvaluation.getSession_id() + "\n" + studentEvaluation.getCourse_id(), Toast.LENGTH_SHORT);
            }
            evaluationService.postStudentEvaluations(studentEvaluations);
            Toast.makeText(getContext(), "Teacher Evaluated", Toast.LENGTH_SHORT).show();

            // Now you have a list of StudentEvaluation objects with the necessary values set
            // You can proceed to use this list as needed
        });


        Toast.makeText(getContext(),"course id"+courseID+"\nevaluatee id"+evaluateeID,Toast.LENGTH_SHORT).show();
        // Inflate the layout for this fragment
        return view;
    }
}