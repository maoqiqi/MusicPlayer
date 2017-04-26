package com.software.march.musicplayer.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author Doc.March
 * @version V 1.0
 * @Description 当你的界面view太多的时候, 往往这个TextView就不一定能够获取到焦点,获取不到焦点也就看不到跑马灯效果了
 * @date 2017/4/26
 */
public class MarqueTextView extends TextView {

    public MarqueTextView(Context context) {
        super(context);
    }

    public MarqueTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // 自定义TextView重写isFocused()函数,让他返回true,也就是一直获取了焦点,效果自然也就出来了
    @Override
    public boolean isFocused() {
        return true;
    }
}