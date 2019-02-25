package com.naer.pdfreader;

public class ScreenShot extends DialogActivity{



    public static void test(){

    }




    // 得到指定Activity的截屏，儲存到png
   /*  public  Bitmap takeScreenShot(){

        //View是你需要截圖的View
       /* View view = getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        //得到狀態列高度
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        System.out.println(statusBarHeight);

        //得到螢幕長和高　
        int width = getWindowManager().getDefaultDisplay().getWidth();
        int height =getWindowManager().getDefaultDisplay().getHeight();
        //去掉標題列
        //Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    //儲存到sdcard
    public static void savePic(Bitmap b,String strFileName){
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(strFileName);
            if (null != fos)
            {
                b.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void shoot(Activity a){
        ScreenShot.savePic(ScreenShot.takeScreenShot(a), "sdcard/xx.png");
    }*/
}
