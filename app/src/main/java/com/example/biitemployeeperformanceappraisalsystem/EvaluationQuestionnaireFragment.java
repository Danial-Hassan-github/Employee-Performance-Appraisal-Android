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
import com.example.biitemployeeperformanceappraisalsystem.director.DirectorMainActivity;
import com.example.biitemployeeperformanceappraisalsystem.faculty.FacultyMain;
import com.example.biitemployeeperformanceappraisalsystem.helper.SharedPreferencesManager;
import com.example.biitemployeeperformanceappraisalsystem.hod.HodMainActivity;
import com.example.biitemployeeperformanceappraisalsystem.models.ConfidentialEvaluation;
import com.example.biitemployeeperformanceappraisalsystem.models.DegreeExitEvaluation;
import com.example.biitemployeeperformanceappraisalsystem.models.DirectorEvaluation;
import com.example.biitemployeeperformanceappraisalsystem.models.PeerEvaluation;
import com.example.biitemployeeperformanceappraisalsystem.models.Question;
import com.example.biitemployeeperformanceappraisalsystem.models.SeniorTeacherEvaluation;
import com.example.biitemployeeperformanceappraisalsystem.models.StudentEvaluation;
import com.example.biitemployeeperformanceappraisalsystem.models.SupervisorEvaluation;
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

    public EvaluationQuestionnaireFragment(int evaluateeID, String evaluationType, int fragmentContainerId){
        this.evaluateeID = evaluateeID;
        this.evaluationType = evaluationType;
        this.fragmentContainerId = fragmentContainerId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferencesManager = new SharedPreferencesManager(getContext());
        evaluationService = new EvaluationService(getContext());
        parentActivity = getActivity();
//        if (parentActivity instanceof StudentMainActivity){
//
//        }else {
//            evaluatorID = sharedPreferencesManager.getEmployeeUserObject().getEmployee().getId();
//        }
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
                    EvaluationQuestionnaireAdapter adapter =  new EvaluationQuestionnaireAdapter(view.getContext(),R.layout.evaluation_questionnaire_list_item_layout,questions, view);
                    questionnaireListView.setAdapter(adapter);
                },
                // onFailure callback
                errorMessage -> {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                });

        btnSubmit.setOnClickListener(v -> {
            List<Pair<Integer, Integer>> selectedAnswers = ((EvaluationQuestionnaireAdapter) questionnaireListView.getAdapter()).getSelectedAnswers();

            List<StudentEvaluation> studentEvaluations = new ArrayList<>();
            List<DegreeExitEvaluation> degreeExitEvaluations = new ArrayList<>();
            List<PeerEvaluation> peerEvaluations = new ArrayList<>();
            List<SeniorTeacherEvaluation> seniorTeacherEvaluations = new ArrayList<>();
            List<ConfidentialEvaluation> confidentialEvaluations = new ArrayList<>();
            List<SupervisorEvaluation> supervisorEvaluations = new ArrayList<>();
            List<DirectorEvaluation> directorEvaluations = new ArrayList<>();

            if (parentActivity instanceof StudentMainActivity){
                if (evaluationType.equalsIgnoreCase("confidential")) {
                    for (Pair<Integer, Integer> pair : selectedAnswers) {
                        ConfidentialEvaluation confidentialEvaluation = new ConfidentialEvaluation();
                        confidentialEvaluation.setStudent_id(sharedPreferencesManager.getStudentUserObject().getId()); // Set your student ID
                        confidentialEvaluation.setSession_id(sessionID); // Set your session ID
                        confidentialEvaluation.setTeacher_id(evaluateeID); // Set the teacher ID (evaluatee ID) from the fragment constructor
                        confidentialEvaluation.setQuestion_id(pair.first); // Set the question ID from the selected answers
                        confidentialEvaluation.setScore(pair.second); // Set the score from the selected answers
                        confidentialEvaluations.add(confidentialEvaluation);
                        Toast.makeText(getContext(), confidentialEvaluation.getStudent_id() + "\n" + confidentialEvaluation.getSession_id(), Toast.LENGTH_SHORT).show();
                    }
                    evaluationService.postConfidentialEvaluation(confidentialEvaluations);
                    StudentCoursesFragment studentCoursesFragment = new StudentCoursesFragment();
                    replaceFragment(parentActivity.getSupportFragmentManager(),studentCoursesFragment,fragmentContainerId);
                }
                else if (evaluationType.equalsIgnoreCase("degree exit")){
                    for (Pair<Integer, Integer> pair : selectedAnswers) {
                        DegreeExitEvaluation degreeExitEvaluation = new DegreeExitEvaluation();
                        degreeExitEvaluation.setStudent_id(sharedPreferencesManager.getStudentUserObject().getId()); // Set your student ID
                        degreeExitEvaluation.setSession_id(sessionID); // Set your session ID
                        degreeExitEvaluation.setSupervisor_id(evaluateeID); // Set the teacher ID (evaluatee ID) from the fragment constructor
                        degreeExitEvaluation.setQuestion_id(pair.first); // Set the question ID from the selected answers
                        degreeExitEvaluation.setScore(pair.second); // Set the score from the selected answers
                        degreeExitEvaluations.add(degreeExitEvaluation);
                        Toast.makeText(getContext(), degreeExitEvaluation.getStudent_id() + "\n" + degreeExitEvaluation.getSession_id(), Toast.LENGTH_SHORT).show();
                    }
                    evaluationService.postDegreeExitEvaluations(degreeExitEvaluations);
                    StudentCoursesFragment studentCoursesFragment = new StudentCoursesFragment();
                    replaceFragment(parentActivity.getSupportFragmentManager(),studentCoursesFragment,fragmentContainerId);
                }
                else {
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
                }
            }
            else if (parentActivity instanceof FacultyMain || parentActivity instanceof HodMainActivity) {
                if (evaluationType.equalsIgnoreCase("peer")){
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
                else if (evaluationType.equalsIgnoreCase("senior")){
                    for (Pair<Integer, Integer> pair : selectedAnswers) {
                        SeniorTeacherEvaluation seniorTeacherEvaluation = new SeniorTeacherEvaluation();
                        seniorTeacherEvaluation.setSeniorTeacherId(sharedPreferencesManager.getEmployeeUserObject().getEmployee().getId()); // Set your student ID
                        seniorTeacherEvaluation.setCourseId(courseID);
                        seniorTeacherEvaluation.setSessionId(sessionID); // Set your session ID
                        seniorTeacherEvaluation.setJuniorTeacherId(evaluateeID); // Set the teacher ID (evaluatee ID) from the fragment constructor
                        seniorTeacherEvaluation.setQuestionId(pair.first); // Set the question ID from the selected answers
                        seniorTeacherEvaluation.setScore(pair.second); // Set the score from the selected answers
                        seniorTeacherEvaluations.add(seniorTeacherEvaluation);
                        Toast.makeText(getContext(), seniorTeacherEvaluation.getSeniorTeacherId() + "\n" + seniorTeacherEvaluation.getSessionId(), Toast.LENGTH_SHORT).show();
                    }
                    evaluationService.postSeniorTeacherEvaluations(seniorTeacherEvaluations);
                    EvaluateeListFragment fragment = new EvaluateeListFragment(fragmentContainerId);
                    replaceFragment(parentActivity.getSupportFragmentManager(),fragment,fragmentContainerId);
                }
                else if (evaluationType.equalsIgnoreCase("supervisor")){
                    for (Pair<Integer, Integer> pair : selectedAnswers) {
                        SupervisorEvaluation supervisorEvaluation = new SupervisorEvaluation();
                        supervisorEvaluation.setSupervisor_id(sharedPreferencesManager.getEmployeeUserObject().getEmployee().getId()); // Set your student ID
                        supervisorEvaluation.setSession_id(sessionID); // Set your session ID
                        supervisorEvaluation.setSubordinate_id(evaluateeID); // Set the teacher ID (evaluatee ID) from the fragment constructor
                        supervisorEvaluation.setQuestion_id(pair.first); // Set the question ID from the selected answers
                        supervisorEvaluation.setScore(pair.second); // Set the score from the selected answers
                        supervisorEvaluations.add(supervisorEvaluation);
                        Toast.makeText(getContext(), supervisorEvaluation.getSupervisor_id() + "\n" + supervisorEvaluation.getSession_id(), Toast.LENGTH_SHORT).show();
                    }
                    evaluationService.postSupervisorEvaluation(supervisorEvaluations);
                    EvaluateeListFragment fragment = new EvaluateeListFragment(fragmentContainerId);
                    replaceFragment(parentActivity.getSupportFragmentManager(),fragment,fragmentContainerId);
                }
            }
            else if (parentActivity instanceof DirectorMainActivity) {
                for (Pair<Integer, Integer> pair : selectedAnswers) {
                    DirectorEvaluation directorEvaluation = new DirectorEvaluation();
                    directorEvaluation.setEvaluator_id(sharedPreferencesManager.getEmployeeUserObject().getEmployee().getId()); // Set your student ID
                    directorEvaluation.setSession_id(sessionID); // Set your session ID
                    directorEvaluation.setEvaluatee_id(evaluateeID); // Set the teacher ID (evaluatee ID) from the fragment constructor
                    directorEvaluation.setQuestion_id(pair.first); // Set the question ID from the selected answers
                    directorEvaluation.setScore(pair.second); // Set the score from the selected answers
                    directorEvaluations.add(directorEvaluation);
                    Toast.makeText(getContext(), directorEvaluation.getEvaluator_id() + "\n" + directorEvaluation.getSession_id(), Toast.LENGTH_SHORT).show();
                }
                evaluationService.postDirectorEvaluation(directorEvaluations);
                EmployeeListFragment fragment = new EmployeeListFragment();
                replaceFragment(parentActivity.getSupportFragmentManager(),fragment,fragmentContainerId);
            }
            Toast.makeText(getContext(), "Evaluated Successfully", Toast.LENGTH_SHORT).show();
        });


        Toast.makeText(getContext(),"course id"+courseID+"\nevaluatee id"+evaluateeID,Toast.LENGTH_SHORT).show();
        // Inflate the layout for this fragment
        return view;
    }
}