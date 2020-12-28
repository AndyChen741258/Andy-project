package Model;

public class StoreTheEditData {
    String originalPhotoUri; //原始照片uri
    float playerA_x; //腳色A的對話框X位置
    float playerA_y; //腳色A的對話框Y位置
    float playerB_x; //腳色B的對話框X位置
    float playerB_y; //腳色B的對話框Y位置
    float player3_x; //腳色3的對話框X位置
    float player3_y; //腳色3的對話框Y位置
    float player4_x; //腳色4的對話框X位置
    float player4_y; //腳色4的對話框Y位置
    float player5_x; //腳色5的對話框X位置
    float player5_y; //腳色5的對話框Y位置
    float player6_x; //腳色6的對話框X位置
    float player6_y; //腳色6的對話框Y位置

    String playerA_text; //腳色A的對話文字內容
    String playerB_text; //腳色A的對話文字內容
    String player3_text; //腳色3的對話文字內容
    String player4_text; //腳色4的對話文字內容
    String player5_text; //腳色5的對話文字內容
    String player6_text; //腳色6的對話文字內容
    String playerA_record; //腳色A的錄音
    String playerB_record; //腳色A的錄音
    String player3_record; //腳色3的錄音
    String player4_record; //腳色4的錄音
    String player5_record; //腳色5的錄音
    String player6_record; //腳色6的錄音

    int playerA_playtime; //聽取腳色A台詞的錄音次數
    int playerB_playtime; //聽取腳色B台詞的錄音次數
    int player3_playtime; //聽取腳色3台詞的錄音次數
    int player4_playtime; //聽取腳色4台詞的錄音次數
    int player5_playtime; //聽取腳色5台詞的錄音次數
    int player6_playtime; //聽取腳色6台詞的錄音次數
    String editFinishPhotoUri; //編輯完成的照片uri


    public StoreTheEditData(String originalPhotoUri, float playerA_x, float playerA_y, float playerB_x, float playerB_y, float player3_x, float player3_y, float player4_x, float player4_y,float player5_x, float player5_y, float player6_x, float player6_y,
                            String playerA_text, String playerB_text, String playerA_record, String playerB_record,String player3_text, String player4_text, String player3_record, String player4_record, String player5_text, String player6_text, String player5_record, String player6_record,
                            int playerA_playtime, int playerB_playtime,int player3_playtime, int player4_playtime,int player5_playtime, int player6_playtime,String editFinishPhotoUri){
        this.originalPhotoUri = originalPhotoUri;
        this.playerA_x = playerA_x;
        this.playerA_y = playerA_y;
        this.playerB_x = playerB_x;
        this.playerB_y = playerB_y;
        this.player3_x = player3_x;
        this.player3_y = player3_y;
        this.player4_x = player4_x;
        this.player4_y = player4_y;
        this.player5_x = player5_x;
        this.player5_y = player5_y;
        this.player6_x = player6_x;
        this.player6_y = player6_y;
        this.playerA_text = playerA_text;
        this.playerB_text = playerB_text;
        this.player3_text = player3_text;
        this.player4_text = player4_text;
        this.player5_text = player5_text;
        this.player6_text = player6_text;
        this.playerA_record = playerA_record;
        this.playerB_record = playerB_record;
        this.player3_record = player3_record;
        this.player4_record = player4_record;
        this.player5_record = player5_record;
        this.player6_record = player6_record;
        this.playerA_playtime = playerA_playtime;
        this.playerB_playtime = playerB_playtime;
        this.player3_playtime = player3_playtime;
        this.player4_playtime = player4_playtime;
        this.player5_playtime = player5_playtime;
        this.player6_playtime = player6_playtime;
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
    public float getPlayer3_x(){
        return player3_x;
    }

    public void setPlayer3_x(float player3_x){
        this.player3_x = player3_x;
    }

    public float getPlayer3_y(){
        return player3_y;
    }

    public void setPlayer3_y(float player3_y){
        this.player3_y = player3_y;
    }

    public float getPlayer4_x(){
        return player4_x;
    }

    public void setPlayer4_x(float player4_x){
        this.player4_x = player4_x;
    }

    public float getPlayer4_y(){
        return player4_y;
    }

    public void setPlayer4_y(float player4_y){
        this.player4_y = player4_y;
    }

    public String getPlayer3_text(){
        return player3_text;
    }

    public void setPlayer3_text(String player3_text){
        this.player3_text = player3_text;
    }

    public String getPlayer4_text(){
        return player4_text;
    }

    public void setPlayer4_text(String player4_text){
        this.player4_text = player4_text;
    }

    public String getPlayer3_record(){ return player3_record; }

    public void setPlayer3_record(String player3_record){
        this.player3_record = player3_record;
    }

    public String getPlayer4_record(){
        return player4_record;
    }

    public void setPlayer4_record(String player4_record){
        this.player4_record = player4_record;
    }

    public int getPlayer3_playtime(){
        return player3_playtime;
    }

    public void setPlayer3_playtime(int player3_playtime){
        this.player3_playtime = player3_playtime;
    }

    public int getPlayer4_playtime(){
        return player4_playtime;
    }

    public void setPlayer4_playtime(int player4_playtime){
        this.player4_playtime = player4_playtime;
    }
    public float getPlayer5_x(){
        return playerA_x;
    }

    public void setPlayer5_x(float player5_x){
        this.player5_x = player5_x;
    }

    public float getPlayer5_y(){
        return player5_y;
    }

    public void setPlayer5_y(float player5_y){
        this.player5_y = player5_y;
    }

    public float getPlayer6_x(){
        return player6_x;
    }

    public void setPlayer6_x(float player6_x){
        this.player6_x = player6_x;
    }

    public float getPlayer6_y(){
        return player6_y;
    }

    public void setPlayer6_y(float player6_y){
        this.player6_y = player6_y;
    }

    public String getPlayer5_text(){
        return player5_text;
    }

    public void setPlayer5_text(String player5_text){
        this.player5_text = player5_text;
    }

    public String getPlayer6_text(){
        return player6_text;
    }

    public void setPlayer6_text(String player6_text){
        this.player6_text = player6_text;
    }

    public String getPlayer5_record(){
        return player5_record;
    }

    public void setPlayer5_record(String player5_record){
        this.player5_record = player5_record;
    }

    public String getPlayer6_record(){
        return player6_record;
    }

    public void setPlayer6_record(String player6_record){
        this.player6_record = player6_record;
    }

    public int getPlayer5_playtime(){
        return player5_playtime;
    }

    public void setPlayer5_playtime(int player5_playtime){
        this.player5_playtime = player5_playtime;
    }

    public int getPlayer6_playtime(){
        return player6_playtime;
    }

    public void setPlayer6_playtime(int player6_playtime){
        this.player6_playtime = player6_playtime;
    }

    public String getEditFinishPhotoUri(){
        return editFinishPhotoUri;
    }

    public void setEditFinishPhotoUri(String editFinishPhotoUri){
        this.editFinishPhotoUri = editFinishPhotoUri;
    }



}
