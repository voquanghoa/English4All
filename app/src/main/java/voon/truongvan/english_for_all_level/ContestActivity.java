package voon.truongvan.english_for_all_level;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import java.io.IOException;

import voon.truongvan.english_for_all_level.adapter.QuestionAnswerAdapter;
import voon.truongvan.english_for_all_level.control.BaseActivity;
import voon.truongvan.english_for_all_level.controller.QuestionHelper;
import voon.truongvan.english_for_all_level.controller.UserResultController;
import voon.truongvan.english_for_all_level.model.Question;
import voon.truongvan.english_for_all_level.model.TestContent;
import voon.truongvan.english_for_all_level.util.Utils;

/**
 * Created by voqua on 2/26/2016.
 */
public class ContestActivity extends BaseActivity {
    private QuestionAnswerAdapter questionAnswerAdapter;
    private TestContent testContent;
    private int arrayOfIndex[];
    private int currentQuestionIndex;
    private int numOfQuestion;
    private int[] achievementScore = new int[]{
            5,
            10,
            15,
            20,
            30,
            50
    };

    private int[] achievementId = new int[]{
            R.string.achievement_win_5_questions_in_a_row,
            R.string.achievement_win_10_questions_in_a_row,
            R.string.achievement_win_15_questions_in_a_row,
            R.string.achievement_win_20_questions_in_a_row,
            R.string.achievement_win_30_questions_in_a_row,
            R.string.achievement_win_50_questions_in_a_row,
    };


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contest_content_layout);
        testContent = new TestContent();
        questionAnswerAdapter = new QuestionAnswerAdapter(this,testContent);
        questionAnswerAdapter.setShowQuestionIndex(false);

        ((ListView)findViewById(R.id.question_list_view)).setAdapter(questionAnswerAdapter);

        currentQuestionIndex = -1;
        arrayOfIndex = Utils.getRandomArray(100);
        numOfQuestion = 0;
        appTitle.setLeftTitleText("");
        showNextQuestion();
    }

    private void showNextQuestion(){
        currentQuestionIndex++;

        if(currentQuestionIndex >= arrayOfIndex.length){
            currentQuestionIndex=0;
            arrayOfIndex = Utils.getRandomArray(100);
        }

        appTitle.setCenterTitleText("Question "+(++numOfQuestion));
        try {
            showQuestion(arrayOfIndex[currentQuestionIndex]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showQuestion(int questionId) throws IOException {
        Question question = QuestionHelper.getQuestion(this, questionId);
        testContent.getQuestions().clear();
        testContent.getQuestions().add(question);
        questionAnswerAdapter.resetUserSelection();
        questionAnswerAdapter.notifyDataSetChanged();
    }

    public void onSubmit(View view){
        if(questionAnswerAdapter.isShowAnswer()){
            this.finish();
        }else if(questionAnswerAdapter.getCorrects()==1){
            showNextQuestion();
        }else{
            numOfQuestion = 12;
            questionAnswerAdapter.setShowAnswer(true);

            GoogleApiClient apiClient = getApiClient();
            Games.Leaderboards.submitScore(apiClient,
                    getString(R.string.leaderboard_hight_score), numOfQuestion-1);
            for(int i=0;i<achievementScore.length;i++){
                if(numOfQuestion>achievementScore[i]){
                    Games.Achievements.unlock(apiClient, getString(achievementId[i]));
                }
            }
        }
    }
}
