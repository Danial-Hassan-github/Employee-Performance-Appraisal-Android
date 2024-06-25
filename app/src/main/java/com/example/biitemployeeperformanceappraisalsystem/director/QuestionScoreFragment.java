package com.example.biitemployeeperformanceappraisalsystem.director;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.biitemployeeperformanceappraisalsystem.R;
import com.example.biitemployeeperformanceappraisalsystem.models.EmployeeQuestionsScores;
import com.example.biitemployeeperformanceappraisalsystem.models.QuestionScore;
import com.example.biitemployeeperformanceappraisalsystem.network.services.EmployeeQuestionScoreService;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionScoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionScoreFragment extends Fragment {
    int employeeId1;
    int employeeId2;
    int sessionId;
    int evaluationTypeId;
    int courseId;
    List<QuestionScore> employeeQuestionScoreList;
    ListView employeeScoreListView;
    public QuestionScoreFragment() {
        // Required empty public constructor
    }

    public QuestionScoreFragment(int employeeId1, int employeeId2, int sessionId, int evaluationTypeId, int courseId){
        this.employeeId1 = employeeId1;
        this.employeeId2 = employeeId2;
        this.sessionId = sessionId;
        this.evaluationTypeId = evaluationTypeId;
        this.courseId = courseId;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuestionScoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestionScoreFragment newInstance(String param1, String param2) {
        QuestionScoreFragment fragment = new QuestionScoreFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question_score, container, false);

        employeeScoreListView = view.findViewById(R.id.employee_questions_scores);

        EmployeeQuestionScoreService employeeQuestionScoreService = new EmployeeQuestionScoreService(getContext());
                employeeQuestionScoreService.getEmployeeQuestionScore(
                        employeeId1,
                        employeeId2,
                        sessionId,
                        evaluationTypeId,
                        courseId,
                        employeeQuestionScores -> {
                            // Handle the retrieved employee question scores
                            employeeQuestionScoreList = employeeQuestionScores;
                            String[] scoresData = new String[employeeQuestionScoreList.size()];

                            // Populate the array with the data from employeeScoreList
                            for (int i = 0; i < employeeQuestionScoreList.size(); i++) {
                                QuestionScore score = employeeQuestionScoreList.get(i);
                                // Assuming you have some method to format EmployeeQuestionScore to String
                                String scoreString = score != null ? score.getQuestion().getQuestion().toString() + "\n" + score.getObtainedScore() +"/" + score.getTotalScore() : ""; // Handle null case
                                scoresData[i] = scoreString;
                            }
                            // Set the adapter for the ListView
                            employeeScoreListView.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, scoresData));
                        },
                        errorMessage -> {
                            // Handle the error message
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        });
        return view;
    }
}