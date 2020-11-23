package com.naer.pdfreader;

import java.util.ArrayList;
import java.util.List;

public class KeyList {
    public List<KeyDatabase> keyDatabaseList = new ArrayList<>();

    public KeyList() {  //定位顯示訊息你在哪
       //keyDatabaseList.add(new KeyDatabase(24.9679895,121.1877413,30,"c40bb1b556544e5592eefa8557485f02","Laboratory"));
       //keyDatabaseList.add(new KeyDatabase(24.967341,121.187608,15,"92071a1c3cd24ec8a494a02ab61c8dc9","engineer"));//工五館
        keyDatabaseList.add(new KeyDatabase(24.936182,121.163906,50, R.raw.engineerbuilding_hiowhp_ac400b4c08c1,"playground"));//我家
        keyDatabaseList.add(new KeyDatabase(24.9496935,121.2057405,50, R.raw.engineerbuilding_hiowhp_ac400b4c08c1,"swimming pool"));//平興游泳池
        keyDatabaseList.add(new KeyDatabase(24.9498411,121.2062200,25, R.raw.engineerbuilding_hiowhp_ac400b4c08c1,"library"));//平興圖書館
        keyDatabaseList.add(new KeyDatabase(24.9501966,121.2056203,50, R.raw.engineerbuilding_hiowhp_ac400b4c08c1,"playground"));//平興操場
       keyDatabaseList.add(new KeyDatabase(24.967128,121.187708,50, R.raw.engineerbuilding_hiowhp_ac400b4c08c1,"laboratory"));//實驗室
//        keyDatabaseList.add(new KeyDatabase(24.967128,121.187708,100, R.raw.engineerbuilding_hiowhp_ac400b4c08c1,"playground"));//實驗室地點
//        keyDatabaseList.add(new KeyDatabase(24.916564,121.249508,5000, R.raw.engineerbuilding_hiowhp_ac400b4c08c1,"classroom"));//東安國中教室
       //keyDatabaseList.add(new KeyDatabase(24.917761,121.249042,5000, R.raw.engineerbuilding_hiowhp_ac400b4c08c1,"playground"));//東安國中操場 半徑100公尺**
       //keyDatabaseList.add(new KeyDatabase(24.916772,121.249998,20, R.raw.engineerbuilding_hiowhp_ac400b4c08c1,"classroom"));//東安國中情境教室 半徑20公尺
       //keyDatabaseList.add(new KeyDatabase(24.795796,120.976551,100,"bbc72747ec6548faa0e412aeff604d47","playground"));//新竹家
       //keyDatabaseList.add(new KeyDatabase(24.967086,121.187176,20,"c40bb1b556544e5592eefa8557485f02","laboratory"));//工程五館二樓教室
       //keyDatabaseList.add(new KeyDatabase(24.967128,121.187708,20,"c40bb1b556544e5592eefa8557485f02","laboratory"));//會議室A402
    }
    public List<KeyDatabase> getKeyDatabaseList() {
        return keyDatabaseList;
    }
}
