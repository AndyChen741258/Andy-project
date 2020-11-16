package Model;

public class DialogPracticBehavior {
    String date;
    String content_source;
    String content_number;
    String role;
    String first_word;
    String first_photo;
    int first_hint;
    String second_word;
    String second_photo;
    int second_hint;
    String third_word;
    String third_photo;
    int third_hint;
    String fourth_word;
    String fourth_photo;
    int fourth_hint;

    public DialogPracticBehavior(String date, String content_source, String content_number, String role,
                                 String first_word,  String first_photo, int first_hint,
                                 String second_word,  String second_photo, int second_hint,
                                 String third_word,  String third_photo, int third_hint,
                                 String fourth_word,  String fourth_photo, int fourth_hint){
        this.date = date;
        this.content_source = content_source;
        this.content_number = content_number;
        this.role = role;
        this.first_word = first_word;
        this.first_photo = first_photo;
        this.first_hint = first_hint;
        this.second_word = second_word;
        this.second_photo = second_photo;
        this.second_hint = second_hint;
        this.third_word = third_word;
        this.third_photo = third_photo;
        this.third_hint = third_hint;
        this.fourth_word = fourth_word;
        this.fourth_photo = fourth_photo;
        this.fourth_hint = fourth_hint;
    }

    public DialogPracticBehavior(){}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent_source() {
        return content_source;
    }

    public void setContent_source(String content_source) {
        this.content_source = content_source;
    }

    public String getContent_number() {
        return content_number;
    }

    public void setContent_number(String content_number) {
        this.content_number = content_number;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFirst_word() {
        return first_word;
    }

    public void setFirst_word(String first_word) {
        this.first_word = first_word;
    }

    public String getFirst_photo() {
        return first_photo;
    }

    public void setFirst_photo(String first_photo) {
        this.first_photo = first_photo;
    }

    public int getFirst_hint() {
        return first_hint;
    }

    public void setFirst_hint(int first_hint) {
        this.first_hint = first_hint;
    }

    public String getSecond_word() {
        return second_word;
    }

    public void setSecond_word(String second_word) {
        this.second_word = second_word;
    }

    public String getSecond_photo() {
        return second_photo;
    }

    public void setSecond_photo(String second_photo) {
        this.second_photo = second_photo;
    }

    public int getSecond_hint() {
        return second_hint;
    }

    public void setSecond_hint(int second_hint) {
        this.second_hint = second_hint;
    }

    public String getThird_word() {
        return third_word;
    }

    public void setThird_word(String third_word) {
        this.third_word = third_word;
    }

    public String getThird_photo() {
        return third_photo;
    }

    public void setThird_photo(String third_word) {
        this.third_word = third_word;
    }

    public int getThird_hint() {
        return third_hint;
    }

    public void setThird_hint(int third_hint) {
        this.third_hint = third_hint;
    }

    public String getFourth_word() {
        return fourth_word;
    }

    public void setFourth_word(String fourth_word) {
        this.fourth_word = fourth_word;
    }

    public String getFourth_photo() {
        return fourth_photo;
    }

    public void setFourth_photo(String fourth_photo) {
        this.fourth_photo = fourth_photo;
    }

    public int getFourth_hint() {
        return fourth_hint;
    }

    public void setFourth_hint(int fourth_hint) {
        this.fourth_hint = fourth_hint;
    }




}
