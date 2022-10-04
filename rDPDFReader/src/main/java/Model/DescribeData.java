package Model;

public class DescribeData {
    String student_number;
    String location;
    String describe_text;
    String imgUrl;

    public DescribeData(String student_number, String location, String describe_text, String imgUrl){

        this.student_number = student_number;
        this.location = location;
        this.describe_text = describe_text;
        this.imgUrl = imgUrl;

    }

    public DescribeData(){
    }

    public String getStudent_number() {
        return student_number;
    }

    public void setStudent_number(String student_number) {
        this.student_number = student_number;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescribe_text() {
        return describe_text;
    }

    public void setDescribe_text(String describe_text) {
        this.describe_text = describe_text;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

}
