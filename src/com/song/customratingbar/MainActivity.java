package com.song.customratingbar;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.song.customratingbar.CustomRatingBar.onCustomRatingBarListener;

public class MainActivity extends Activity implements onCustomRatingBarListener {
    
    private DisplayMetrics displayMetrics;
    private float density;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //第一种星星的写法
        ((CustomRatingBar) findViewById(R.id.ratingbar)).setonCustomRatingBarListener(this);
        
        //第二种星星的写法
        StarBar starBar = (StarBar) findViewById(R.id.starbar);
        starBar.setupImageRes(R.drawable.thumb_0, R.drawable.thumb_1, R.drawable.pgs_empty, R.drawable.pgs_full);
        starBar.setSelectedIndex(3);
        // starBar.getSelectedIndex(); 通过此方法获得当前星数
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        density = displayMetrics.density;
        Log.d("TAG", density + "");
        Log.d("TAG", displayMetrics.widthPixels + ":" + displayMetrics.heightPixels);
    }
    
    @Override
    public void oncustomRatingBarListener(int starNum) {
        Log.d("TAG", "--->oncustomRatingBarListener:" + starNum);
        
    }
    
}
