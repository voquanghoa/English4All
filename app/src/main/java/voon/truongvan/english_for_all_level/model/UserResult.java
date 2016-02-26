package voon.truongvan.english_for_all_level.model;

/**
 * Created by Vo Quang Hoa on 12/26/2015.
 */
public class UserResult {
    private String fileName;
    private int correct;
    private int total;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getDisplay(){
        return correct+"/"+total;
    }
}
