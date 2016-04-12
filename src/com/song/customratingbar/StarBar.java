package com.song.customratingbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author chensongsong 自定义评分条保留星星对外接口
 */
public class StarBar extends View {
    private Bitmap thumb0;
    private Bitmap thumb1;
    private int itemWidth;//单个星星的宽度
    private GestureDetector mDetector;
    private float touchX = -1.0F;//触点相对评分条x坐标
    private int selectedIndex = 0;//星星颗数
    private Paint mPaintText;
    private int resThumb0 = -1;// 灰星星
    private int resThumb1 = -1;// 彩星星
    private int resBg0 = -1;// 灰色横线
    private int resBg1 = -1;// 彩色横线
    
    public void setupImageRes(int thumb0, int thumb1, int bg0, int bg1) {
        this.resThumb0 = thumb0;
        this.resThumb1 = thumb1;
        this.resBg0 = bg0;
        this.resBg1 = bg1;
    }
    
    public int getSelectedIndex() {
        return this.selectedIndex;
    }
    
    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
        setImage();
    }
    
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.itemWidth = (w / 6);
        int height = getHeight();
        if (this.resThumb0 != -1) {
            this.thumb0 = resizeImage(BitmapFactory.decodeResource(getResources(), this.resThumb0), this.itemWidth, height);
        }
        if (this.resThumb1 != -1)
            this.thumb1 = resizeImage(BitmapFactory.decodeResource(getResources(), this.resThumb1), this.itemWidth, height);
    }
    
    public Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;
        
        float scaleWidth = newWidth / (float) width;
        float scaleHeight = newHeight / (float) height;
        
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width, height, matrix, true);
        return resizedBitmap;
    }
    
    private void init(Context context) {
        this.mPaintText = new Paint();
        this.mPaintText.setColor(-1);
        this.mPaintText.setTextSize(30.0F);
        this.mPaintText.setTextAlign(Paint.Align.CENTER);
        this.mDetector = new GestureDetector(context, new MyGestureListener());
        setLongClickable(true);
        setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return StarBar.this.mDetector.onTouchEvent(event);
            }
        });
    }
    
    public StarBar(Context context) {
        super(context);
        init(context);
    }
    
    public StarBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    
    public StarBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
    
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.selectedIndex == 0) {
            if (this.resThumb0 != -1)
                canvas.drawBitmap(this.thumb0, 0.0F, 0.0F, null);
        } else if (this.resThumb1 != -1) {
            canvas.drawBitmap(this.thumb1, this.selectedIndex * this.itemWidth, 0.0F, null);
        }
        Paint.FontMetrics fm = this.mPaintText.getFontMetrics();
        int fontHeight = (int) Math.ceil(fm.descent - fm.ascent);
        canvas.drawText(String.valueOf(this.selectedIndex), this.selectedIndex * this.itemWidth + this.itemWidth / 2, getHeight() / 2 + fontHeight / 3, this.mPaintText);
    }
    
    private void afterClick() {
        if (this.touchX > getWidth())
            this.touchX = Math.min(getWidth(), this.touchX);
        else if (this.touchX < 0.0F) {
            this.touchX = Math.max(0.0F, this.touchX);
        }
        int nSelect = (int) Math.floor(this.touchX / this.itemWidth);
        if (nSelect > 5) {
            nSelect = 5;
        }
        if (this.selectedIndex != nSelect) {
            this.selectedIndex = nSelect;
            setImage();
        }
    }
    
    private void setImage() {
        if (this.selectedIndex == 0) {
            if (this.resBg0 != -1) {
                setBackgroundResource(this.resBg0);
            }
        } else if (this.resBg1 != -1) {
            setBackgroundResource(this.resBg1);
        }
        
        postInvalidate();
    }
    
    public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            StarBar.this.touchX = e2.getX();
            StarBar.this.afterClick();
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
        
        public boolean onSingleTapConfirmed(MotionEvent e) {
            StarBar.this.touchX = e.getX();
            StarBar.this.afterClick();
            return super.onSingleTapConfirmed(e);
        }
    }
}