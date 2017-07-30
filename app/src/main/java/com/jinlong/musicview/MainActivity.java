package com.jinlong.musicview;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private MusicStyleView mMusicStyleView;
    private List<TextView> mTextViews;/*保存TextView的集合*/
    private List<MusicBean> mMusicBeanLists;
    private Random mRandom;/*随机数，这里是为了确定显示字体大小的*/
    private int[] mTextFount = {14, 16, 18, 20, 22};
    private String mNormalColor = "#141414";/*默认的字体颜色*/
    private String mSelectColor = "#ff6200";/*选择后的字体颜色*/
    private List<TextView> mSaveTextViews;/*保存的TextView集合*/
    private Animation mPanAnim;/*盘片的动画*/
    private LinearInterpolator mPanInterpolator;/*盘片的线性动画*/
    private MediaPlayer mMediaPlayer;
    private ImageView mIvPan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMusicStyleView = (MusicStyleView) findViewById(R.id.musicView);
        mIvPan = (ImageView) findViewById(R.id.iv_pan);

        mRandom = new Random();
        mMediaPlayer = new MediaPlayer();
        mPanAnim = AnimationUtils.loadAnimation(this, R.anim.animation_pan);
        mPanInterpolator = new LinearInterpolator();
        mPanAnim.setInterpolator(mPanInterpolator);
        initData();
        initView();
        initListener();
    }

    private void initData() {
        mSaveTextViews = new ArrayList<>();
        mTextViews = new ArrayList<>();
        mMusicBeanLists = new ArrayList<>();
        mMusicBeanLists.add(new MusicBean("TFBOYS", "http://xcmusic.oss-cn-beijing.aliyuncs.com/TFBOYS%20-%20%E9%9D%92%E6%98%A5%E4%BF%AE%E7%82%BC%E6%89%8B%E5%86%8C.mp3"));
        mMusicBeanLists.add(new MusicBean("周杰伦", "http://xcmusic.oss-cn-beijing.aliyuncs.com/TFBOYS%20-%20%E9%9D%92%E6%98%A5%E4%BF%AE%E7%82%BC%E6%89%8B%E5%86%8C.mp3"));
        mMusicBeanLists.add(new MusicBean("林俊杰", "http://xcmusic.oss-cn-beijing.aliyuncs.com/TFBOYS%20-%20%E9%9D%92%E6%98%A5%E4%BF%AE%E7%82%BC%E6%89%8B%E5%86%8C.mp3"));
        mMusicBeanLists.add(new MusicBean("张学友", "http://xcmusic.oss-cn-beijing.aliyuncs.com/TFBOYS%20-%20%E9%9D%92%E6%98%A5%E4%BF%AE%E7%82%BC%E6%89%8B%E5%86%8C.mp3"));
        mMusicBeanLists.add(new MusicBean("刘德华", "http://xcmusic.oss-cn-beijing.aliyuncs.com/TFBOYS%20-%20%E9%9D%92%E6%98%A5%E4%BF%AE%E7%82%BC%E6%89%8B%E5%86%8C.mp3"));
        mMusicBeanLists.add(new MusicBean("热歌", "http://xcmusic.oss-cn-beijing.aliyuncs.com/TFBOYS%20-%20%E9%9D%92%E6%98%A5%E4%BF%AE%E7%82%BC%E6%89%8B%E5%86%8C.mp3"));
        mMusicBeanLists.add(new MusicBean("新歌", "http://xcmusic.oss-cn-beijing.aliyuncs.com/TFBOYS%20-%20%E9%9D%92%E6%98%A5%E4%BF%AE%E7%82%BC%E6%89%8B%E5%86%8C.mp3"));
        mMusicBeanLists.add(new MusicBean("怀旧", "http://xcmusic.oss-cn-beijing.aliyuncs.com/TFBOYS%20-%20%E9%9D%92%E6%98%A5%E4%BF%AE%E7%82%BC%E6%89%8B%E5%86%8C.mp3"));
        mMusicBeanLists.add(new MusicBean("曲风", "http://xcmusic.oss-cn-beijing.aliyuncs.com/TFBOYS%20-%20%E9%9D%92%E6%98%A5%E4%BF%AE%E7%82%BC%E6%89%8B%E5%86%8C.mp3"));
        mMusicBeanLists.add(new MusicBean("音乐", "http://xcmusic.oss-cn-beijing.aliyuncs.com/TFBOYS%20-%20%E9%9D%92%E6%98%A5%E4%BF%AE%E7%82%BC%E6%89%8B%E5%86%8C.mp3"));
    }

    private void initView() {
        /*初始化显示的View*/
        for (int i = 0; i < mMusicBeanLists.size(); i++) {
            TextView tv = new TextView(this);
            tv.setText(mMusicBeanLists.get(i).getTag());
            int textFount = mRandom.nextInt(mTextFount.length);
            tv.setTextSize(mTextFount[textFount]);
            tv.setTextColor(Color.parseColor(mNormalColor));
            /*文字显示的渐变*/
            PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 0, 1f);
            ObjectAnimator.ofPropertyValuesHolder(tv, pvhX)
                    .setDuration(new Random().nextInt(5000))
                    .start();
            mTextViews.add(tv);
            mMusicStyleView.addView(tv);
        }
    }

    private void initListener() {
        for (int i = 0; i < mMusicBeanLists.size(); i++) {
            final int finalI = i;
            mTextViews.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  /*点击事件的处理*/
                    clickTextView(v, finalI);
                }
            });
        }
    }

    /*点击事件的处理*/
    private void clickTextView(View v, int finalI) {
        TextView clickTextView = (TextView) v;
        if (isSaveView(clickTextView)) {/*之前保存过这个View*/
            /*如果歌曲还在唱的话应该释放这个资源的*/
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.reset();
            }
            clickTextView.setTextColor(Color.parseColor(mNormalColor));
            mSaveTextViews.remove(clickTextView);
            mIvPan.clearAnimation();
        } else {/*之前没有保存过这个View的话应该开始播放音乐的*/
            clickTextView.setTextColor(Color.parseColor(mSelectColor));
            mSaveTextViews.add(clickTextView);
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mMediaPlayer.reset();
            }
            startMusic(mMusicBeanLists.get(finalI).getSingUrl());
            mIvPan.startAnimation(mPanAnim);
        }
    }

    /*之前时候有这个TextView*/
    public boolean isSaveView(TextView tv) {
        for (int i = 0; i < mSaveTextViews.size(); i++) {
            if (mSaveTextViews.get(i).equals(tv)) {
                return true;
            }
        }
        return false;
    }

    private void stopMusic() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    /**
     * 开始播放音乐
     */
    private void startMusic(String path) {
        try {
            mMediaPlayer.setDataSource(path);//设置播放的数据源。
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.prepare();//同步的准备方法。
            mMediaPlayer.start();
            /*播放完成的监听*/
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopMusic();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.reset();
        }
    }
}
