package com.jinlong.musicview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 音乐风格的自定义View
 */

public class MusicStyleView extends ViewGroup {


    private Random mRandom;
    private List<Rect> mRects;
    private int mOutr;

    public MusicStyleView(Context context) {
        this(context, null);
    }

    public MusicStyleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MusicStyleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initRandom();
    }

    private void initRandom() {
        mRandom = new Random();
        mRects = new ArrayList<>();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int cCount = getChildCount();
        int screenWidth = getWidth();
        int screenHeight = getHeight();
        //圆心坐标
        mOutr = screenWidth / 2;

        int cl = 0, ct = 0, cr = 0, cb = 0;
        for (int i = 0; i < cCount; i++) {
            View childAt = getChildAt(i);
            int cWidth = childAt.getMeasuredWidth();
            int cHeight = childAt.getMeasuredHeight();
            if (i == 0) {
                cl = screenWidth / 2 - cWidth / 2;
                ct = screenHeight / 2 - cHeight / 2;
                cr = screenWidth / 2 + cWidth / 2;
                cb = screenHeight / 2 + cHeight / 2;
                mRects.add(new Rect(cl, ct, cr, cb));
                childAt.layout(cl, ct, cr, cb);
            } else if (i == 1) {
                cl = screenWidth / 2 + getChildAt(0).getMeasuredWidth() / 2 - cWidth;
                ct = screenHeight / 2 - getChildAt(0).getMeasuredHeight() / 2;
                cr = screenWidth / 2 + getChildAt(0).getMeasuredWidth() / 2;
                cb = screenHeight / 2 + cHeight - getChildAt(0).getMeasuredHeight() / 2;
                mRects.add(new Rect(cl, ct, cr, cb));
                childAt.layout(cl, ct, cr, cb);
            } else {/*这个是第一个View的时候，展示在中间的*/
                setLocationView(screenWidth, screenHeight, childAt, cWidth, cHeight);
            }
        }
    }

    private void setLocationView(int screenWidth, int screenHeight, View childAt, int cWidth, int cHeight) {
        int cl;
        int ct;
        int cr;
        int cb;
        /*这里随机的是结束点*/
        int randomWidth = mRandom.nextInt(screenWidth);
        int randomHeight = mRandom.nextInt(screenHeight);

        cl = randomWidth - cWidth;
        ct = randomHeight - cHeight;
        cr = randomWidth;
        cb = randomHeight;
        int distanceX = Math.abs(mOutr - randomWidth);
        int distanceY = Math.abs(mOutr - randomHeight);

        //点击位置与圆心的直线距离
        int distanceZ = (int) Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
        if (cl < 0 || ct < 0 || distanceZ > mOutr) {
            /*这里应该重新获取点重新计算*/
            setLocationView(screenWidth, screenHeight, childAt, cWidth, cHeight);
            return;
        }

        /*这里判断的是是否重合*/
        Rect currentRect = new Rect(cl, ct, cr, cb);
        for (int i = 0; i < mRects.size(); i++) {
            Rect rect = mRects.get(i);
            if (Rect.intersects(rect, currentRect)) {
                setLocationView(screenWidth, screenHeight, childAt, cWidth, cHeight);
                return;
            }
        }
        childAt.layout(cl, ct, cr, cb);
        mRects.add(new Rect(cl, ct, cr, cb));
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    /*当布局设置成Match——params时使用如下方法设置测量值，若是要精准计算的话就要测量每一个View的值了在进行测绘*/
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        //测量并保存layout的宽高(使用getDefaultSize时，wrap_content和match_perent都是填充屏幕)
        //稍后会重新写这个方法，能达到wrap_content的效果
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec));
    }
}