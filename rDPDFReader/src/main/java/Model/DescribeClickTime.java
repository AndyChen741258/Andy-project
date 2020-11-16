package Model;

public class DescribeClickTime {
    int vocabulary;
    int phrase;
    int sentence;
    int see_other;

    public DescribeClickTime(int vocabulary, int phrase, int sentence, int see_other){
        this.vocabulary = vocabulary;
        this.phrase = phrase;
        this.sentence = sentence;
        this.see_other = see_other;
    }

    public DescribeClickTime(){}

    public int getVocabulary(){
        return vocabulary;
    }

    public void setVocabulary(int vocabulary){
        this.vocabulary = vocabulary;
    }

    public int getphrase(){
        return phrase;
    }

    public void setphrase(int phrase){
        this.phrase = phrase;
    }

    public int getSentence(){
        return sentence;
    }

    public void setSentence(int sentence){
        this.sentence = sentence;
    }

    public int getSee_other(){
        return see_other;
    }

    public void setSee_other(int see_other){
        this.see_other = see_other;
    }

}
