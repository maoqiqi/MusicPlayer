package com.software.march.appcommonlibrary;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.software.march.musicplayer.R;

/**
 * @author Doc.March
 * @version V 1.0
 * @Description Activity 基类, 继承AppCompatActivity
 * @date 2017/3/7
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected final String TAG = getClass().getSimpleName();

    /**
     * 上下文
     */
    protected Context mContext;

    /**
     * 根布局
     */
    protected View mRootView;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mRootView = View.inflate(mContext, getLayoutId(), null);
        setContentView(mRootView);
        setToolbar();
        afterCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContext = null;
        mRootView = null;
    }

    /**
     * 强制子类重写,返回布局Id
     *
     * @return layout Id
     */
    protected abstract int getLayoutId();

    protected abstract void afterCreate(Bundle savedInstanceState);

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar == null)
            return;
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);

    }

    protected void setTitle(String title) {
        if (title == null || toolbar == null)
            return;
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}