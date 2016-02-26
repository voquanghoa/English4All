package voon.truongvan.english_for_all_level.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Arrays;

import voon.truongvan.english_for_all_level.R;
import voon.truongvan.english_for_all_level.controller.QuestionHelper;
import voon.truongvan.english_for_all_level.model.Question;
import voon.truongvan.english_for_all_level.model.TestContent;
import voon.truongvan.english_for_all_level.util.ViewUtils;

/**
 * Created by Vo Quang Hoa on 12/22/2015.
 */
public class QuestionAnswerAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {
    private Context context;
    private TestContent test;
    private boolean showAnswer = false;
    private int[] userSelection;
    private boolean showQuestionIndex;
    private int[] radioButtonId = new int[]{
            R.id.answer_a,
            R.id.answer_b,
            R.id.answer_c,
            R.id.answer_d
    };

    public QuestionAnswerAdapter(Context context, TestContent test) {
        this.context = context;
        this.test = test;
        this.showQuestionIndex = true;
        initUserSelect();
    }

    private void initUserSelect(){
        this.userSelection = new int[test.getQuestions().size()];
        Arrays.fill(userSelection, -1);
    }

    public void resetUserSelection(){
        Arrays.fill(userSelection,-1);
    }
    public void notifyDataSetChanged() {
        if(this.userSelection.length<this.getCount()){
            initUserSelect();
        }
        super.notifyDataSetChanged();
    }

    public int getTotal(){
        return this.userSelection.length;
    }

    public int getCorrects(){
        int correct =0;
        for(int i=0;i<getTotal();i++){
            if(userSelection[i] == test.getQuestions().get(i).getCorrectAnswer()){
                correct ++;
            }
        }
        return correct;
    }

    public String getResultAsString(String timeDuration) {
        return String.format(this.context.getString(R.string.dialog_result_content), getCorrects(), getTotal()) +
                timeDuration;
    }

    public boolean isShowAnswer() {
        return showAnswer;
    }

    public void setShowAnswer(boolean showAnswer){
        this.showAnswer = showAnswer;
        this.notifyDataSetChanged();
    }

    public int getCount() {
        return test.getQuestions().size();
    }

    public Object getItem(int position) {
        return test.getQuestions().get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Question question = test.getQuestions().get(position);
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.question_answer_layout, parent, false);
        }

        Button categoryButton = (Button)convertView.findViewById(R.id.category_button);
        TextView tvQuestion = (TextView)convertView.findViewById(R.id.question);
        RadioGroup radioGroup = (RadioGroup)convertView.findViewById(R.id.answer_group);
        RadioButton[] radioButtons = getRadioButtons(convertView);

        setRadioOnChanged(radioButtons, false);
        setRadioValue(userSelection[position], radioGroup);
        setRadioTag(radioGroup, radioButtons, position);

        radioGroup.setEnabled(!showAnswer);
        setRadioButtonEnable(radioButtons);
        setRadioButtonShow(radioButtons, question);
        showCategoryQuestion(question, categoryButton);
        setQuestionText(question, tvQuestion, position);
        setAnswerText(radioButtons, question);
        setRadioOnChanged(radioButtons, true);
        return convertView;
    }

    private RadioButton[] getRadioButtons(View convertView) {
        return new RadioButton[]{
                    (RadioButton)convertView.findViewById(R.id.answer_a),
                    (RadioButton)convertView.findViewById(R.id.answer_b),
                    (RadioButton)convertView.findViewById(R.id.answer_c),
                    (RadioButton)convertView.findViewById(R.id.answer_d)
            };
    }

    private void setRadioValue(int i, RadioGroup radioGroup) {
        if(i ==-1){
            radioGroup.check(-1);
        }else{
            radioGroup.check(radioButtonId[i]);
        }
    }

    private void setRadioTag(RadioGroup radioGroups, RadioButton[] radioButtons, int questionId){
        radioGroups.setTag(questionId);
        for (RadioButton radioButton : radioButtons) {
            radioButton.setTag(questionId);
        }
    }

    private void setRadioOnChanged(RadioButton[] radioButtons, boolean isSet){
        for (RadioButton radioButton : radioButtons) {
            radioButton.setOnCheckedChangeListener(isSet ? this : null);
        }
    }

    private void setAnswerText(RadioButton[] radioButtons,Question question){
        for(int i=0;i<radioButtons.length;i++){
            RadioButton radioButton = radioButtons[i];
            String answer = question.getAnswer(i);
            if(showAnswer){
                radioButton.setText(Html.fromHtml(QuestionHelper.convertToColor(answer,
                        question.checkCorrectAnswer(i) ? "blue" : "black")));
            }else {
                radioButton.setText(Html.fromHtml(answer));
            }

            if(answer==null || answer.length()==0){
                radioButton.setVisibility(View.GONE);
            }else{
                radioButton.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setShowQuestionIndex(boolean isShow){
        showQuestionIndex = isShow;
    }

    private void setQuestionText(Question question, TextView tvQuestion, int position) {
        tvQuestion.setText(Html.fromHtml(showQuestionIndex?((1 + position) + ". "):"" + question.getQuestion()
                .replace("<u>", "").replace("</u>", "")
                .trim()));
    }

    private void showCategoryQuestion(Question question, Button categoryButton) {
        if(question.getCategory()==null || question.getCategory().length()==0){
            categoryButton.setVisibility(View.GONE);
        }else{
            categoryButton.setVisibility(View.VISIBLE);
            categoryButton.setText(question.getCategory());
        }
    }

    private void setRadioButtonEnable(RadioButton[] radioButtons){
        for(RadioButton radioButton: radioButtons){
            radioButton.setEnabled(!showAnswer);
        }
    }

    private void setRadioButtonShow(RadioButton[] radioButtons, Question question){
        for(int i=0;i<radioButtons.length;i++){
            ViewUtils.setViewVisibility(radioButtons[i], question.getAnswer(i).length() > 0);
        }
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.isChecked()){
            int questionIndex = Integer.decode(buttonView.getTag().toString()).intValue();
            int viewId = buttonView.getId();

            for(int id=0;id<radioButtonId.length;id++) {
                if (viewId == radioButtonId[id]) {
                    userSelection[questionIndex] = id;
                }
            }
        }
    }
}
