package voon.truongvan.english_for_all_level;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import java.io.IOException;

import voon.truongvan.english_for_all_level.control.BaseActivity;
import voon.truongvan.english_for_all_level.control.ClockControl;
import voon.truongvan.english_for_all_level.controller.QuestionHelper;
import voon.truongvan.english_for_all_level.model.Question;
import voon.truongvan.english_for_all_level.util.Utils;

/**
 * Created by voqua on 3/7/2016.
 */
public class RankingActivity extends BaseActivity {
    private ClockControl clockControl;
    private int arrayOfIndex[];
    private int currentQuestionIndex;
    private int numOfQuestion;
    private Question currentQuestion;

    private int[] achievementScore = new int[]{
            5,
            10,
            15,
            20,
            30,
            50
    };

    private int[] answerIds = new int[]{
            R.id.answer_a,
            R.id.answer_b,
            R.id.answer_c,
            R.id.answer_d,
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
        setContentView(R.layout.ranking_layout);
        appTitle.setText(getString(R.string.ranking));
        clockControl = ((ClockControl) findViewById(R.id.clock));
        clockControl.setOnEnded(new Runnable() {
            public void run() {
                RankingActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        onTimeUp();
                    }
                });
            }
        });
        arrayOfIndex = Utils.getRandomArray(100);
        showNextQuestion();
    }

    public void onShowLeaderBoard(View view) {
        showLeaderboard(getString(R.string.leaderboard_hight_score));
    }

    private void onCorrect() {
        showNextQuestion();
    }

    private void showGameOver() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog rankingDialog = builder.setTitle(R.string.incorrect)
                .setIcon(R.drawable.leaderboardall)
                .setMessage(getString(R.string.your_score_message, numOfQuestion - 1))
                .setPositiveButton(getString(R.string.finish), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                }).setNegativeButton(R.string.play_again_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        numOfQuestion = 0;
                        showNextQuestion();
                    }
                }).create();
        rankingDialog.setCancelable(false);
        rankingDialog.show();

    }

    private void onIncorrect() {
        clockControl.setVisibility(View.INVISIBLE);
        GoogleApiClient apiClient = getApiClient();
        if(apiClient.isConnected()){
            Games.Leaderboards.submitScore(apiClient, getString(R.string.leaderboard_hight_score), numOfQuestion - 1);
            for (int i = 0; i < achievementScore.length; i++) {
                if (numOfQuestion > achievementScore[i]) {
                    Games.Achievements.unlock(apiClient, getString(achievementId[i]));
                }
            }
        }

        showGameOver();
    }

    public void onAnswerButtonClick(View view){
        clockControl.stop();
        int id = view.getId();
        for(int i=0;i<answerIds.length;i++){
            if(id == answerIds[i]){
                submit(i);
                return;
            }
        }
    }

    private void submit(int userAnswer) {
        if (userAnswer >= 0) {
            Button selectedButton = (Button) findViewById(answerIds[userAnswer]);
            if (currentQuestion.checkCorrectAnswer(userAnswer)) {
                selectedButton.setBackgroundResource(R.drawable.ranking_answer_correct);
                onCorrect();
                return;
            } else {
                selectedButton.setBackgroundResource(R.drawable.ranking_answer_correct);
            }
        }
        onIncorrect();
    }

    public void finish() {
        clockControl.stop();
        super.finish();
    }

    public void onShowAchievement(View view) {
        showAchievement();
    }

    public void onTimeUp() {
        showMessage(getString(R.string.time_up_message));
        submit(-1);
        clockControl.setVisibility(View.INVISIBLE);
    }

    private synchronized void showQuestion(int questionId) throws IOException {
        Question question = QuestionHelper.getQuestion(this, questionId);
        showCategoryQuestion(question, (TextView) findViewById(R.id.textCategory));
        showAnswers(question);
        setQuestionText(question, (TextView) findViewById(R.id.textQuestion));
        currentQuestion = question;
    }

    private void setQuestionText(Question question, TextView tvQuestion) {
        tvQuestion.setText(Html.fromHtml(question.getQuestion().replace("<u>", "").replace("</u>", "")
                .trim()));
    }

    private void showAnswers(Question question) {
        for (int i = 0; i < answerIds.length; i++) {
            Button button = (Button) findViewById(answerIds[i]);
            button.setText(Html.fromHtml(QuestionHelper.convertToColor(question.getAnswer(i), "#f4f86d")));
            button.setBackgroundResource(R.drawable.ranking_answer_normal);
            if (question.hasAnswer(i)) {
                button.setVisibility(View.VISIBLE);
            } else {
                button.setVisibility(View.GONE);
            }
        }
    }

    private void showCategoryQuestion(Question question, TextView categoryButton) {
        if (question.getCategory() == null || question.getCategory().length() == 0) {
            categoryButton.setVisibility(View.GONE);
        } else {
            categoryButton.setVisibility(View.VISIBLE);
            categoryButton.setText(question.getCategory());
        }
    }

    private void showNextQuestion() {
        clockControl.start();
        clockControl.setVisibility(View.VISIBLE);
        currentQuestionIndex++;

        if (currentQuestionIndex >= arrayOfIndex.length) {
            currentQuestionIndex = 0;
            arrayOfIndex = Utils.getRandomArray(100);
        }

        appTitle.setText(getString(R.string.question_index_format, ++numOfQuestion));
        try {
            showQuestion(arrayOfIndex[currentQuestionIndex]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}