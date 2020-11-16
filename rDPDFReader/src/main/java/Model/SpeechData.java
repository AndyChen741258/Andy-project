package Model;

public class SpeechData {
    String describe_text;
    Double score;
    String studentSays;

    public SpeechData(String describe_text, String studentSays, Double score){
        this.describe_text = describe_text;
        this.studentSays = studentSays;
        this.score = score;
    }

    public SpeechData(){
    }

    public String getDescribe_text() {
        return describe_text;
    }

    public void setDescribe_text(String describe_text) {
        this.describe_text = describe_text;
    }

    public String getStudentSays() {
        return studentSays;
    }

    public void setStudentSays(String describe_text) {
        this.studentSays = studentSays;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

}
