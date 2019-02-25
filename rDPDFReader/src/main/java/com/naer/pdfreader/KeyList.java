package com.naer.pdfreader;

import java.util.ArrayList;
import java.util.List;

public class KeyList {
    public List<KeyDatabase> keyDatabaseList = new ArrayList<>();
    public KeyList() {
       // keyDatabaseList.add(new KeyDatabase(24.9679895,121.1877413,30,"a48942c5f5df478897966133cbc4c1c4","Laboratory"));
       // keyDatabaseList.add(new KeyDatabase(24.967080,121.187649,200,"a48942c5f5df478897966133cbc4c1c4","laboratory"));//工五館
       // keyDatabaseList.add(new KeyDatabase(24.937910,121.381464,30,"a48942c5f5df478897966133cbc4c1c4","laboratory"));//實驗室
       // keyDatabaseList.add(new KeyDatabase(24.968435,121.189234,200,"a48942c5f5df478897966133cbc4c1c4","playground"));
       // keyDatabaseList.add(new KeyDatabase(24.938045,121.381824,30,"a48942c5f5df478897966133cbc4c1c4","place a"));
        keyDatabaseList.add(new KeyDatabase(24.967080,121.187649,200,"beaacbae5a35459895cd0f68d7b82f52","laboratory"));
    }

    public List<KeyDatabase> getKeyDatabaseList() {
        return keyDatabaseList;
    }
}
