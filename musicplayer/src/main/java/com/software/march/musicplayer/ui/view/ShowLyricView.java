package com.software.march.musicplayer.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import com.software.march.appcommonlibrary.DensityUtils;
import com.software.march.musicplayer.R;
import com.software.march.musicplayer.bean.LyricBean;

import java.util.ArrayList;

/**
 * @author Doc.March
 * @version V 1.0
 * @Description
 * @date 2017/4/14
 */
public class ShowLyricView extends TextView {

    /**
     * 宽
     */
    private int width;

    /**
     * 高
     */
    private int height;

    /**
     * 高亮显示部分画笔
     */
    private Paint paint;

    /**
     * 前面和后面内容画笔
     */
    private Paint whitePaint;

    /**
     * 歌词列表
     */
    private ArrayList<LyricBean> lyricBeans;


    /**
     * 每行的高
     */
    private float textHeight;

    /**
     * 当前播放进度
     */
    private float currentPosition;

    /**
     * 高亮显示的时间或者休眠时间
     */
    private float sleepTime;

    /**
     * 时间戳,什么时刻到高亮哪句歌词
     */
    private float timePoint;

    /**
     * 设置歌词列表
     *
     * @param lyricBeans
     */
    public void setLyrics(ArrayList<LyricBean> lyricBeans) {
        this.lyricBeans = lyricBeans;
    }


    /**
     * 当前正在播放的歌词列表索引,是第几句歌词
     */
    private int mIndex;

    public ShowLyricView(Context context) {
        this(context, null);
    }

    public ShowLyricView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShowLyricView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    private void initView(Context context) {
        textHeight = DensityUtils.dp2px(context, 18);

        // 创建画笔
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
        paint.setTextSize(DensityUtils.dp2px(context, 16));
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);

        whitePaint = new Paint();
        whitePaint.setColor(Color.WHITE);
        whitePaint.setTextSize(DensityUtils.dp2px(context, 16));
        whitePaint.setAntiAlias(true);
        whitePaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (lyricBeans != null && lyricBeans.size() > 0) {

            // 往上推移
            float plush = 0;
            if (sleepTime == 0) {
                plush = 0;
            } else {
                // 平移
                // 屏幕的的坐标 = 行高 + 移动的距离
                plush = textHeight + ((currentPosition - timePoint) / sleepTime) * textHeight;
            }
            canvas.translate(0, -plush);

            // 绘制歌词:绘制当前句
            String currentText = lyricBeans.get(mIndex).getContent();
            canvas.drawText(currentText, width / 2, height / 2, paint);

            // 绘制前面部分
            float tempY = height / 2;// Y轴的中间坐标
            for (int i = mIndex - 1; i >= 0; i--) {
                // 每一句歌词
                String preContent = lyricBeans.get(i).getContent();
                tempY = tempY - textHeight;
                if (tempY < 0) {
                    break;
                }
                canvas.drawText(preContent, width / 2, tempY, whitePaint);
            }

            // 绘制后面部分
            tempY = height / 2;// Y轴的中间坐标
            for (int i = mIndex + 1; i < lyricBeans.size(); i++) {
                // 每一句歌词
                String nextContent = lyricBeans.get(i).getContent();
                tempY = tempY + textHeight;
                if (tempY > height) {
                    break;
                }
                canvas.drawText(nextContent, width / 2, tempY, whitePaint);
            }
        } else {
            canvas.drawText("没有歌词", width / 2, height / 2, paint);
        }
    }

    /**
     * 根据当前播放的位置,找出该高亮显示哪句歌词
     *
     * @param currentPosition
     */
    public void showNextLyric(int currentPosition) {
        this.currentPosition = currentPosition;

        if (lyricBeans == null || lyricBeans.size() == 0)
            return;

        for (int i = 1; i < lyricBeans.size(); i++) {
            if (currentPosition < lyricBeans.get(i).getTimePoint()) {
                int tempIndex = i - 1;
                if (currentPosition >= lyricBeans.get(tempIndex).getTimePoint()) {
                    // 当前正在播放的哪句歌词
                    mIndex = tempIndex;
                    timePoint = lyricBeans.get(mIndex).getTimePoint();
                    sleepTime = lyricBeans.get(mIndex).getSleepTime();
                    break;
                }
            }
        }
        // 重新绘制
        invalidate();// 在主线程中
    }
}