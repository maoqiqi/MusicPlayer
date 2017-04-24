package com.software.march.musicplayer.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;

import com.software.march.appcommonlibrary.BaseActivity;
import com.software.march.musicplayer.R;

// 启动页
// SplashActivity
// LauncherActivity
// WelcomeActivity
public class SplashActivity extends BaseActivity {

    private Handler handler = new Handler();
    private boolean isFirstStartMain;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        // MainActivity启动多次解决方案
        // 1.单例模式 + 移除消息
        // 2.源头控制(isFirstStartMain) + 移除消息
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // 两秒后才执行到这里
                // 执行在主线程中
                startMainActivity();
            }
        }, 1000);
    }

    // 跳转到主页面,并且把当前页面关闭掉
    private void startMainActivity() {
        if (!isFirstStartMain) {
            isFirstStartMain = true;
            Intent intent = new Intent(mContext, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            finish();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        startMainActivity();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        // 把所有的消息和回调移除
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}