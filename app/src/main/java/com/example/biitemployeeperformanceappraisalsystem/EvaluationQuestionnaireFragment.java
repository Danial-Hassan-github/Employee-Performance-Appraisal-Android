package com.example.biitemployeeperformanceappraisalsystem;

import static com.example.biitemployeeperformanceappraisalsystem.helper.FragmentUtils.replaceFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.adapter.EvaluationQuestionnaireAdapter;
import com.example.biitemployeeperformanceappraisalsystem.faculty.FacultyMain;
import com.example.biitemployeeperformanceappraisalsystem.helper.SharedPreferencesManager;
import com.example.biitemployeeperformanceappraisalsystem.hod.HodMainActivity;
import com.example.biitemployeeperformanceappraisalsystem.models.PeerEvaluation;
import com.example.biitemployeeperformanceappraisalsystem.models.Question;
import com.example.biitemployeeperformanceappraisalsystem.models.StudentEvaluation;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EvaluationService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EvaluatorService;
import com.example.biitemployeeperformanceappraisalsystem.network.services.QuestionnaireService;
import com.example.biitemployeeperformanceappraisalsystem.student.CourseTeacherFragment;
import com.example.biitemployeeperformanceappraisalsystem.student.StudentCoursesFragment;
import com.example.biitemployeeperformanceappraisalsystem.student.StudentMainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EvaluationQuestionnaireFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EvaluationQuestionnaireFragment extends Fragment {
    int evaluatorID, evaluateeID, courseID, sessionID;
    String evaluationType;
    private List<Question> questionsList;
    ListView questionnaireListView;
    Button btnSubmit;
    SharedPreferencesManager sharedPreferencesManager;
    EvaluationService evaluationService;
    StudentMainActivity studentMainActivity;
    private FragmentActivity parentActivity;
    private int fragmentContainerId;
    boolean allQuestionsAnswered = false;
    public EvaluationQuestionnaireFragment(int evaluateeID, int courseID, String evaluationType, int fragmentContainerId){
        this.evaluateeID = evaluateeID;
        this.courseID = courseID;
        this.evaluationType = evaluationType;
        this.fragmentContainerId = fragmentContainerId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferencesManager = new SharedPreferencesManager(getContext());
        evaluationService = new EvaluationService(getContext());
        parentActivity = getActivity();
        evaluatorID = sharedPreferencesManager.getEmployeeUserObject().getEmployee().getId();
        sessionID = sharedPreferencesManager.getSessionId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_evaluation_questionnaire, container, false);

        questionnaireListView=view.findViewById(R.id.evaluation_questionnaire_list_view);
        btnSubmit=view.findViewById(R.id.btn_submit);

        QuestionnaireService questionnaire=new QuestionnaireService(view.getContext());

        questionnaire.getEvaluationQuestionnaire(
                evaluationType,
                // onSuccess callback
                questions -> {
                    questionsList = questions;
                    // Create ArrayAdapter and set it to the ListView
                    EvaluationQuestionnaireAdapter adapter =  new EvaluationQuestionnaireAdapter(view.getContext(),R.layout.evaluation_questionnaire_list_item_layout,questions);
                    questionnaireListView.setAdapter(adapter);
                },
                // onFailure callback
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                });

        btnSubmit.setOnClickListener(v -> {
            List<Pair<Integer, Integer>> selectedAnswers = ((EvaluationQuestionnaireAdapter) questionnaireListView.getAdapter()).getSelectedAnswers();

            List<StudentEvaluation> studentEvaluations = new ArrayList<>();
            List<PeerEvaluation> peerEvaluations = new ArrayList<>();

            if (parentActivity instanceof StudentMainActivity){
                for (Pair<Integer, Integer> pair : selectedAnswers) {
                    StudentEvaluation studentEvaluation = new StudentEvaluation();
                    studentEvaluation.setStudent_id(sharedPreferencesManager.getStudentUserObject().getId()); // Set your student ID
                    studentEvaluation.setSession_id(sessionID); // Set your session ID
                    studentEvaluation.setTeacher_id(evaluateeID); // Set the teacher ID (evaluatee ID) from the fragment constructor
                    studentEvaluation.setQuestion_id(pair.first); // Set the question ID from the selected answers
                    studentEvaluation.setScore(pair.second); // Set the score from the selected answers
                    studentEvaluation.setCourse_id(courseID); // Set the course ID from the fragment constructor
                    studentEvaluations.add(studentEvaluation);
                    Toast.makeText(getContext(), studentEvaluation.getStudent_id() + "\n" + studentEvaluation.getSession_id() + "\n" + studentEvaluation.getCourse_id(), Toast.LENGTH_SHORT).show();
                }
                evaluationService.postStudentEvaluations(studentEvaluations);
                CourseTeacherFragment fragment = new CourseTeacherFragment(courseID);
                replaceFragment(parentActivity.getSupportFragmentManager(),fragment,fragmentContainerId);
            } else {
                for (Pair<Integer, Integer> pair : selectedAnswers) {
                    PeerEvaluation peerEvaluation = new PeerEvaluation();
                    peerEvaluation.setEvaluator_id(sharedPreferencesManager.getEmployeeUserObject().getEmployee().getId()); // Set your student ID
                    peerEvaluation.setSession_id(sessionID); // Set your session ID
                    peerEvaluation.setEvaluatee_id(evaluateeID); // Set the teacher ID (evaluatee ID) from the fragment constructor
                    peerEvaluation.setQuestion_id(pair.first); // Set the question ID from the selected answers
                    peerEvaluation.setScore(pair.second); // Set the score from the selected answers
                    peerEvaluations.add(peerEvaluation);
                    Toast.makeText(getContext(), peerEvaluation.getEvaluator_id() + "\n" + peerEvaluation.getSession_id(), Toast.LENGTH_SHORT).show();
                }
                evaluationService.postPeerEvaluations(peerEvaluations);
                EvaluateeListFragment fragment = new EvaluateeListFragment(fragmentContainerId);
                replaceFragment(parentActivity.getSupportFragmentManager(),fragment,fragmentContainerId);
            }
            // studentMainActivity=(StudentMainActivity) getActivity();
            // studentMainActivity.replaceFragment(new StudentCoursesFragment());
            // EvaluateeListFragment fragment = new EvaluateeListFragment(fragmentContainerId);
            // replaceFragment(parentActivity.getSupportFragmentManager(),fragment,fragmentContainerId);
            Toast.makeText(getContext(), "Evaluated Successfully", Toast.LENGTH_SHORT).show();

            // Now you have a list of StudentEvaluation objects with the necessary values set
            // You can proceed to use this list as needed
        });


        Toast.makeText(getContext(),"course id"+courseID+"\nevaluatee id"+evaluateeID,Toast.LENGTH_SHORT).show();
        // Inflate the layout for this fragment
        return view;
    }
}