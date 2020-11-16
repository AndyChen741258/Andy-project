package Model;

public class StoreTheEditData {
    String originalPhotoUri; //原始照片uri
    float playerA_x; //腳色A的對話框X位置
    float playerA_y; //腳色A的對話框Y位置
    float playerB_x; //腳色B的對話框X位置
    float playerB_y; //腳色B的對話框Y位置
    String playerA_text; //腳色A的對話文字內容
    String playerB_text; //腳色A的對話文字內容
    String playerA_record; //腳色A的錄音
    String playerB_record; //腳色A的錄音
    int playerA_playtime; //聽取腳色A台詞的錄音次數
    int playerB_playtime; //聽取腳色B台詞的錄音次數
    String editFinishPhotoUri; //編輯完成的照片uri


    public StoreTheEditData(String originalPhotoUri, float playerA_x, float playerA_y, float playerB_x, float playerB_y,
                            String playerA_text, String playerB_text, String playerA_record, String playerB_record,
                            int playerA_playtime, int playerB_playtime, String editFinishPhotoUri){
        this.originalPhotoUri = originalPhotoUri;
        this.playerA_x = playerA_x;
        this.playerA_y = playerA_y;
        this.playerB_x = playerB_x;
        this.playerB_y = playerB_y;
        this.playerA_text = playerA_text;
        this.playerB_text = playerB_text;
        this.playerA_record = playerA_record;
        this.playerB_record = playerB_record;
        this.playerA_playtime = playerA_playtime;
        this.playerB_playtime = playerB_playtime;
        this.editFinishPhotoUri = editFinishPhotoUri;
    }

    public StoreTheEditData(){}

    public String getOriginalPhotoUri() {
        return originalPhotoUri;
    }

    public void setOriginalPhotoUri(String originalPhotoUri) {
        this.originalPhotoUri = originalPhotoUri;
    }

    public float getPlayerA_x(){
        return playerA_x;
    }

    public void setPlayerA_x(float playerA_x){
        this.playerA_x = playerA_x;
    }

    public float getPlayerA_y(){
        return playerA_y;
    }

    public void setPlayerA_y(float playerA_y){
        this.playerA_y = playerA_y;
    }

    public float getPlayerB_x(){
        return playerB_x;
    }

    public void setPlayerB_x(float playerB_x){
        this.playerB_x = playerB_x;
    }

    public float getPlayerB_y(){
        return playerB_y;
    }

    public void setPlayerB_y(float playerB_y){
        this.playerB_y = playerB_y;
    }

    public String getPlayerA_text(){
        return playerA_text;
    }

    public void setPlayerA_text(String playerA_text){
        this.playerA_text = playerA_text;
    }

    public String getPlayerB_text(){
        return playerB_text;
    }

    public void setPlayerB_text(String playerB_text){
        this.playerB_text = playerB_text;
    }

    public String getPlayerA_record(){
        return playerA_record;
    }

    public void setPlayerA_record(String playerA_record){
        this.playerA_record = playerA_record;
    }

    public String getPlayerB_record(){
        return playerB_record;
    }

    public void setPlayerB_record(String playerB_record){
        this.playerB_record = playerB_record;
    }

    public int getPlayerA_playtime(){
        return playerA_playtime;
    }

    public void setPlayerA_playtime(int playerA_playtime){
        this.playerA_playtime = playerA_playtime;
    }

    public int getPlayerB_playtime(){
        return playerB_playtime;
    }

    public void setPlayerB_playtime(int playerB_playtime){
        this.playerB_playtime = playerB_playtime;
    }

    public String getEditFinishPhotoUri(){
        return editFinishPhotoUri;
    }

    public void setEditFinishPhotoUri(String editFinishPhotoUri){
        this.editFinishPhotoUri = editFinishPhotoUri;
    }



}
