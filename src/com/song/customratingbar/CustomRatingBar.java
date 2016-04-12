package com.song.customratingbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RatingBar;

/**
 * @author chensongsong 继承原生的RatingBar重写核心的ondraw方法和ontouch方法
 */
public class CustomRatingBar extends RatingBar {
    
    private Paint paint;
    private int starNumble = 0;
    private Options options;
    private Bitmap bitmap;
    private int starWitch;
    private int starHeigh;
    private int lineHeigh;
    private int star_space;
    private onCustomRatingBarListener mCustomRatingBarListener;
    
    public CustomRatingBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    
    public CustomRatingBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public CustomRatingBar(Context context) {
        this(context, null);
    }
    
    /**
     * 资源只有一份，所以就没有做适配，按照dp值写死了
     */
    private void init() {
        paint = new Paint();
        starWitch = DensityUtil.dip2px(getContext(), 18.6f);
        starHeigh = DensityUtil.dip2px(getContext(), 18.0f);
        lineHeigh = DensityUtil.dip2px(getContext(), 7.7f);
        star_space = DensityUtil.dip2px(getContext(), 10.0f);
    }
    
    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(starWitch * 6 + star_space * 5, starHeigh);
    }
    
    @SuppressLint("DrawAllocation")
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        // super.onDraw(canvas);
        switch (starNumble) {
            case 0 :
            case -1 :
                drawBitmaps(0, 0, R.drawable.star_gray, canvas);
                drawBitmaps(1, 5, R.drawable.rectangle_gray, canvas);
                break;
            case 1 :
                drawBitmaps(1, 1, R.drawable.star_task, canvas);
                drawBitmaps(2, 5, R.drawable.rectangle_gray, canvas);
                
                break;
            case 2 :
                drawBitmaps(1, 1, R.drawable.rectangle_yellow, canvas);
                drawBitmaps(2, 2, R.drawable.star_task, canvas);
                drawBitmaps(3, 5, R.drawable.rectangle_gray, canvas);
                
                break;
            case 3 :
                drawBitmaps(1, 2, R.drawable.rectangle_yellow, canvas);
                drawBitmaps(3, 3, R.drawable.star_task, canvas);
                drawBitmaps(4, 5, R.drawable.rectangle_gray, canvas);
                break;
            case 4 :
                drawBitmaps(1, 3, R.drawable.rectangle_yellow, canvas);
                drawBitmaps(4, 4, R.drawable.star_task, canvas);
                drawBitmaps(5, 5, R.drawable.rectangle_gray, canvas);
                
                break;
            case 5 :
                drawBitmaps(1, 4, R.drawable.rectangle_yellow, canvas);
                drawBitmaps(5, 5, R.drawable.star_task, canvas);
                break;
            
            default :
                drawBitmaps(1, 4, R.drawable.rectangle_yellow, canvas);
                drawBitmaps(5, 5, R.drawable.star_task, canvas);
                break;
        }
        Log.d("TAG", "--->" + starNumble);
        if (mCustomRatingBarListener != null) {
            mCustomRatingBarListener.oncustomRatingBarListener(starNumble);
        }
    }
    
    /**
     * @param m
     * @param n
     * @param canvas
     */
    private void drawBitmaps(int m, int n, int id, Canvas canvas) {
        for (int i = m; i <= n; i++) {
            drawBitmap(canvas, id, m, i);
        }
    }
    
    private void drawBitmap(Canvas canvas, int id, int m, int n) {
        options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.star_task, options);
        options.inSampleSize = options.outWidth / starWitch;
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeResource(getResources(), id, options);
        switch (id) {
            case R.drawable.rectangle_gray :
            case R.drawable.rectangle_yellow :
                canvas.drawBitmap(bitmap, n * (starWitch + star_space), lineHeigh, paint);
                break;
            case R.drawable.star_gray :
            case R.drawable.star_task :
                canvas.drawBitmap(bitmap, n * (starWitch + star_space), 0, paint);
                break;
            
            default :
                break;
        }
    }
    
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        if (x > (starWitch * 6 + star_space * 5) || x < 0) {
            return false;
        }
        int num = (int) Math.floor(x / (starWitch + star_space));
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                if (num != starNumble) {
                    starNumble = num;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE :
                if (num != starNumble) {
                    starNumble = num;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_CANCEL :
            case MotionEvent.ACTION_UP :
                break;
            default :
                break;
        }
        return super.onTouchEvent(event);
    }
    
    /**
     * 给自定义RatingBar设置监听器
     * 
     * @param onCustomRatingBarListener
     */
    public void setonCustomRatingBarListener(onCustomRatingBarListener onCustomRatingBarListener) {
        this.mCustomRatingBarListener = onCustomRatingBarListener;
    }
    
    public interface onCustomRatingBarListener {
        void oncustomRatingBarListener(int starNum);
    }
    
}
