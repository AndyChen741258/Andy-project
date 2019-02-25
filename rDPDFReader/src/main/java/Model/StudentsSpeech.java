package Model;

public class StudentsSpeech {
    public String Question;
    public String Answer;
    public String Correct;
    public float SpeechConfidence;
    public double Similarity;

    public StudentsSpeech(String question, String answer, float speechConfidence,String correct) {
        Question = question;
        Answer = answer;
        SpeechConfidence = speechConfidence;
        Correct = correct;
    }

    public StudentsSpeech(String question, String answer, double similarity,String correct) {
        Question = question;
        Answer = answer;
        Similarity = similarity;
        Correct = correct;
    }
}
