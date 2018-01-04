package com.saosao.snow.oma.brid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.saosao.snow.newalpha.myview.brid.CallBack;


/**
 * Created by 19780 on 2018/1/3.
 */

public class bridview extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    SurfaceHolder mSurfaceHolder;
    Canvas canvas;
    Paint paint;
    int y = 0;
    int linex = 0;
    Bitmap mBitmap;
    boolean isOver = false;
    int type = 0;

    private void init(Context context) {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);

        paint = new Paint();
        paint.setColor(Color.BLUE);

    }

    public void setBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public bridview(Context context) {
        super(context);
        init(context);
    }

    public bridview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public bridview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public bridview(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawing();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void run() {
        drawing();
        dong();
    }

    int[] topheights = new int[4];
    int onewidth;
    int oneheight;
    int[] bottomheights = new int[4];

    private void drawing() {
        try {
            canvas = mSurfaceHolder.lockCanvas();
            canvas.drawColor(Color.WHITE);
//            if (mBitmap == null)
//                canvas.drawCircle(getWidth() / 2, getHeight() / 2 + y, 100, paint);
//            else
                canvas.drawBitmap(mBitmap, getWidth() / 2 - 50, getHeight() / 2 + y, paint);


            onewidth = getWidth() / 7;
            oneheight = (getHeight() / 2 - 100) / 10;
            int ff = 0;
            if(type==0)
                ff=onewidth / 10 * 10 * 2;
            else ff=onewidth / 5 * 5 * 2;
            if (linex == ff) {
                int[] temp = new int[4];
                temp[0] = topheights[1];
                temp[1] = topheights[2];
                temp[2] = topheights[3];
                temp[3] = getLineRandom();
                topheights = temp;

                int[] temp1 = new int[4];
                temp1[0] = bottomheights[1];
                temp1[1] = bottomheights[2];
                temp1[2] = bottomheights[3];
                temp1[3] = getLineRandom();
                bottomheights = temp1;

                linex = 0;
                callBack.onAdd();
            }
            else if (linex == 0) {
                topheights = getRandom();
                bottomheights = getRandom();
            }

            for (int i = 1; i <= 7; i = i + 2) {
                int line = 0;
                int line1 = 0;
                switch (i) {
                    case 1:
                        line = topheights[0];
                        line1 = bottomheights[0];
                        break;
                    case 3:
                        line = topheights[1];
                        line1 = bottomheights[1];
                        break;
                    case 5:
                        line = topheights[2];
                        line1 = bottomheights[2];
                        break;
                    case 7:
                        line = topheights[3];
                        line1 = bottomheights[3];
                        break;
                }

                Rect rect;
                Rect rect1;
                if(type==0){
                rect=new Rect(onewidth * i - linex, 0, onewidth * (i + 1) - linex, line * oneheight);
                rect1=new Rect(onewidth * i - linex, getHeight()-line1*oneheight, onewidth * (i + 1) - linex, getHeight());
                }else {
                    rect=new Rect(onewidth * i - linex, 0, onewidth * (i + 1) - linex, line * oneheight-350);
                    rect1=new Rect(onewidth * i - linex, getHeight()-line1*(getHeight()-line * oneheight)/10, onewidth * (i + 1) - linex, getHeight());
                }
                canvas.drawRect(rect, paint);
                Region region=new Region(rect);
                if(region.contains(getWidth() / 2 , getHeight() / 2 + y)){
                    isOver = true;
                    callBack.onOver();
                };


                canvas.drawRect(rect1 ,paint);
                Region region1=new Region(rect1);
                if(region1.contains(getWidth() / 2 , getHeight() / 2 + y)){
                    isOver = true;
                    callBack.onOver();
                };
            }


        } finally {
            if (canvas != null) {
                mSurfaceHolder.unlockCanvasAndPost(canvas);
            }

        }
    }

    public void start() {
        dong();
    }

    private void dong() {
        if(y-100 >= getHeight()/2){
            handlerShow.removeCallbacks(this);
            callBack.onOver();
            return;
        }
        if(isOver){
            handlerShow.removeCallbacks(this);
            callBack.onOver();
            return;
        }
        y += 30;
        if(type ==0)
        linex += onewidth / 10;
        else
            linex += onewidth / 5;
        handlerShow.postDelayed(this, 50);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                y -= 250;
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:

        }
        return true;
    }

    CallBack callBack;

    public void setOnOver(CallBack callBack) {
        this.callBack = callBack;
    }

    public void reset(int type) {
        this.type = type;
        callBack.onReset();
        handlerShow.removeCallbacks(this);
        y = 0;
        linex = 0;
        isOver = false;
        drawing();
    }

    public void stop() {
        isOver = false;
        handlerShow.removeCallbacks(this);
    }
    Handler handlerShow = new Handler();
    public int[] getRandom() {
        int[] is = new int[4];
        is[0] = (int) (Math.random() * 10);
        is[1] = (int) (Math.random() * 10);
        is[2] = (int) (Math.random() * 10);
        is[3] = (int) (Math.random() * 10);
        return is;
    }

    public int getLineRandom() {
        return (int) (Math.random() * 10);
    }

}
