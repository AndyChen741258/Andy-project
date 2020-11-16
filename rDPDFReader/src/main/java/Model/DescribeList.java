package Model;

public class DescribeList {
    String location;
    String describe_text;
    String imgUrl;

    public DescribeList(String location, String describe_text,  String imgUrl){
        this.location = location;
        this.describe_text = describe_text;
        this.imgUrl = imgUrl;
    }

    public DescribeList(){
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
