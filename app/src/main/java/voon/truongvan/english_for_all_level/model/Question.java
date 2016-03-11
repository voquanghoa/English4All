package voon.truongvan.english_for_all_level.model;

import voon.truongvan.english_for_all_level.util.Utils;

/**
 * Created by Vo Quang Hoa on 12/22/2015.
 */
public class Question {
    private String question;
    private String answerA;
    private String answerB;
    private String answerC;
    private String answerD;
    private int correctAnswer;
    private String category;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswerA() {
        return answerA;
    }

    public void setAnswerA(String answerA) {
        this.answerA = answerA;
    }

    public String getAnswerB() {
        return answerB;
    }

    public void setAnswerB(String answerB) {
        this.answerB = answerB;
    }

    public String getAnswerC() {
        return answerC;
    }

    public void setAnswerC(String answerC) {
        this.answerC = answerC;
    }

    public String getAnswerD() {
        return answerD;
    }

    public void setAnswerD(String answerD) {
        this.answerD = answerD;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getCorrectAnswerAsString() {
        return getAnswer(correctAnswer).trim();
    }

    public boolean checkCorrectAnswer(int index) {
        return correctAnswer == index;
    }

    public String getCategory() {
        return category;
    }

    public boolean hasCategory(){
        return category!=null && category.length()>0;
    }

    public boolean isCategory(){
        return this.category!=null && this.category.length()>0;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAnswer(int index){
        switch (index){
            case 0:
                return this.getAnswerA();
            case 1:
                return this.getAnswerB();
            case 2:
                return this.getAnswerC();
            case 3:
                return this.getAnswerD();
            default:
                Utils.Log("Get answer with index "+index);
                return "";
        }
    }

    public boolean hasAnswer(int i){
        String answer = getAnswer(i);
        return answer!=null && answer.length()>0;
    }
}
