package Model;

public class TimeOfSee {
    String theDrama_name;
    int listen_1_a;
    int listen_1_b;
    int listen_2_a;
    int listen_2_b;
    int listen_3_a;
    int listen_3_b;
    int listen_4_a;
    int listen_4_b;

    public TimeOfSee(String theDrama_name, int listen_1_a, int listen_1_b, int listen_2_a, int listen_2_b,
                     int listen_3_a, int listen_3_b, int listen_4_a, int listen_4_b){
        this.theDrama_name = theDrama_name;
        this.listen_1_a = listen_1_a;
        this.listen_1_b = listen_1_b;
        this.listen_2_a = listen_2_a;
        this.listen_2_b = listen_2_b;
        this.listen_3_a = listen_3_a;
        this.listen_3_b = listen_3_b;
        this.listen_4_a = listen_4_a;
        this.listen_4_b = listen_4_b;
    }

    public TimeOfSee(){

    }

    public String getTheDrama_name(){ return theDrama_name; }

    public void setTheDrama_name(String theDrama_name){ this.theDrama_name = theDrama_name; }

    public int getListen_1_a(){ return listen_1_a; }

    public void setListen_1_a(int listen_1_a){ this.listen_1_a = listen_1_a; }

    public int getListen_1_b(){ return listen_1_b; }

    public void setListen_1_b(int listen_1_a){ this.listen_1_b = listen_1_b; }

    public int getListen_2_a(){ return listen_2_a; }

    public void setListen_2_a(int listen_2_a){ this.listen_2_a = listen_2_a; }

    public int getListen_2_b(){ return listen_2_b; }

    public void setListen_2_b(int listen_2_a){ this.listen_2_b = listen_2_b; }

    public int getListen_3_a(){ return listen_3_a; }

    public void setListen_3_a(int listen_3_a){ this.listen_3_a = listen_3_a; }

    public int getListen_3_b(){ return listen_3_b; }

    public void setListen_3_b(int listen_3_a){ this.listen_3_b = listen_3_b; }

    public int getListen_4_a(){ return listen_4_a; }

    public void setListen_4_a(int listen_4_a){ this.listen_4_a = listen_4_a; }

    public int getListen_4_b(){ return listen_4_b; }

    public void setListen_4_b(int listen_4_a){ this.listen_4_b = listen_4_b; }

}

