package com.software.march.musicplayer.ui.activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.software.march.appcommonlibrary.BaseActivity;
import com.software.march.musicplayer.R;
import com.software.march.musicplayer.bean.SongBean;
import com.software.march.musicplayer.service.IMusicPlayerService;
import com.software.march.musicplayer.service.MusicPlayerService;
import com.software.march.musicplayer.ui.view.ShowLyricView;
import com.software.march.musicplayer.utils.Constant;
import com.software.march.musicplayer.utils.DateUtils;
import com.software.march.musicplayer.utils.LyricUtils;

import java.io.File;

public class MusicPlayerActivity extends BaseActivity implements View.OnClickListener {

    private DateUtils dateUtils;
    private LyricUtils lyricUtils;

    private TextView tvName;
    private TextView tvArtist;
    private ShowLyricView showLyricView;
    private TextView tvCurrentPosition;
    private SeekBar seekBar;
    private TextView tvDuration;
    private ImageView ivPlayMode;
    private ImageView ivPrevious;
    private ImageView ivPlayOrPause;
    private ImageView ivNext;
    private ImageView ivPlayList;

    private IMusicPlayerService iMusicPlayerService;

    private boolean mFromUser;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_music_player;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        dateUtils = new DateUtils();
        lyricUtils = new LyricUtils();

        tvName = (TextView) findViewById(R.id.tv_name);
        tvArtist = (TextView) findViewById(R.id.tv_artist);
        showLyricView = (ShowLyricView) findViewById(R.id.show_lyric_view);
        tvCurrentPosition = (TextView) findViewById(R.id.tv_current_position);
        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        tvDuration = (TextView) findViewById(R.id.tv_duration);
        ivPlayMode = (ImageView) findViewById(R.id.iv_play_mode);
        ivPrevious = (ImageView) findViewById(R.id.iv_previous);
        ivPlayOrPause = (ImageView) findViewById(R.id.iv_play_or_pause);
        ivNext = (ImageView) findViewById(R.id.iv_next);
        ivPlayList = (ImageView) findViewById(R.id.iv_play_list);

        seekBar.setEnabled(false);

        registerReceivers();
        bindAndStartService();
    }

    // setSystemUiVisibility(int visibility)方法可传入的实参为：
    // 1. View.SYSTEM_UI_FLAG_VISIBLE：显示状态栏，Activity不全屏显示(恢复到有状态栏的正常情况)。
    // 2. View.INVISIBLE：隐藏状态栏，同时Activity会伸展全屏显示。
    // 3. View.SYSTEM_UI_FLAG_FULLSCREEN：Activity全屏显示，且状态栏被隐藏覆盖掉。
    // 4. View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN：Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态遮住。
    // 5. View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION：效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    // 6. View.SYSTEM_UI_LAYOUT_FLAGS：效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    // 7. View.SYSTEM_UI_FLAG_HIDE_NAVIGATION：隐藏虚拟按键(导航栏)。有些手机会用虚拟按键来代替物理按键。
    // 8. View.SYSTEM_UI_FLAG_LOW_PROFILE：状态栏显示处于低能显示状态(low profile模式)，状态栏上一些图标显示会被隐藏

    // Android4.4 新特性又增加下面俩个：
    // View.SYSTEM_UI_FLAG_IMMERSIVE
    // View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    // Android 4.4新特性之开启全屏沉浸模式 http://www.cnblogs.com/8dull/p/5372165.html

    // 另外
    // View.SYSTEM_UI_FLAG_LAYOUT_STABLE

    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void showSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
    }

    /**
     * 注册广播
     */
    private void registerReceivers() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicPlayerService.OPEN_AUDIO);
        intentFilter.addAction(MusicPlayerService.STOP_AUDIO);
        registerReceiver(receiver, intentFilter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MusicPlayerService.OPEN_AUDIO)) {
                // 设置页面展示数据
                setViewData();
                // 校验播放模式按钮
                checkPlayMode(false);
                // 校验播放和暂停的按钮
                checkPlayAndPause();
                // 显示歌词
                showLyric();
            } else if (action.equals(MusicPlayerService.STOP_AUDIO)) {
                checkPlayAndPause();
            }
        }
    };

    /**
     * 设置页面展示数据
     */
    private void setViewData() {
        if (iMusicPlayerService == null) return;

        try {
            SongBean songBean = iMusicPlayerService.getCurrentSong();
            if (songBean == null) return;

            String displayName = songBean.getDisplayName();

            String name;
            String artist;

            int start = displayName.indexOf(" - ");
            if (start != -1) {
                name = displayName.substring(start + " - ".length(), displayName.lastIndexOf("."));
                artist = displayName.substring(0, start);
            } else {
                name = songBean.getTitle();
                artist = songBean.getArtist();
            }

            if ("<unknown>".equals(artist)) {
                artist = "未知艺人";
            }

            tvName.setText(name);
            tvArtist.setText(artist);

            int duration = (int) songBean.getDuration();
            seekBar.setMax(duration);
            tvDuration.setText(dateUtils.stringForTime(duration));

            handler.sendEmptyMessage(Constant.PROGRESS);

            seekBar.setEnabled(true);
            // 设置拖动监听
            seekBar.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());

            ivPlayMode.setOnClickListener(this);
            ivPrevious.setOnClickListener(this);
            ivPlayOrPause.setOnClickListener(this);
            ivNext.setOnClickListener(this);
            ivPlayList.setOnClickListener(this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constant.PROGRESS:
                    try {
                        int currentPosition = iMusicPlayerService.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                        tvCurrentPosition.setText(dateUtils.stringForTime(currentPosition));

                        handler.removeMessages(Constant.PROGRESS);
                        if (iMusicPlayerService.isPlaying()) {
                            // 每秒更新一次
                            handler.sendEmptyMessageDelayed(Constant.PROGRESS, 1000);
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case Constant.SHOW_LYRIC:
                    try {
                        // 1.得到当前的进度
                        int currentPosition = iMusicPlayerService.getCurrentPosition();
                        // 2.把进度传入ShowLyricView控件,并且计算该高亮哪一句
                        showLyricView.showNextLyric(currentPosition);

                        // 3.实时的发消息
                        handler.removeMessages(Constant.SHOW_LYRIC);
                        if (iMusicPlayerService.isPlaying()) {
                            handler.sendEmptyMessage(Constant.SHOW_LYRIC);
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    };

    private class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        // 该方法拖动进度条进度改变的时候调用
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mFromUser = fromUser;
        }

        // 该方法拖动进度条开始拖动的时候调用
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        // 该方法拖动进度条停止拖动的时候调用
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (mFromUser) {
                if (iMusicPlayerService == null) return;

                // 拖动进度
                try {
                    iMusicPlayerService.seekTo(seekBar.getProgress());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_play_mode:
                if (iMusicPlayerService == null) return;

                try {
                    int playMode = iMusicPlayerService.getPlayMode();
                    if (playMode == MusicPlayerService.ORDER_NORMAL) {
                        playMode = MusicPlayerService.SINGLE_REPEAT;
                    } else if (playMode == MusicPlayerService.SINGLE_REPEAT) {
                        playMode = MusicPlayerService.RANDOM;
                    } else if (playMode == MusicPlayerService.RANDOM) {
                        playMode = MusicPlayerService.ORDER_NORMAL;
                    } else {
                        playMode = MusicPlayerService.ORDER_NORMAL;
                    }
                    iMusicPlayerService.setPlayMode(playMode);
                    checkPlayMode(true);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.iv_previous:
                if (iMusicPlayerService == null) return;

                try {
                    iMusicPlayerService.previous();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.iv_play_or_pause:
                if (iMusicPlayerService == null) return;

                try {
                    if (iMusicPlayerService.isPlaying()) {
                        iMusicPlayerService.pause();
                        handler.removeMessages(Constant.PROGRESS);
                        handler.removeMessages(Constant.SHOW_LYRIC);
                    } else {
                        iMusicPlayerService.start();
                        handler.sendEmptyMessage(Constant.PROGRESS);
                        handler.sendEmptyMessage(Constant.SHOW_LYRIC);
                    }
                    checkPlayAndPause();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.iv_next:
                if (iMusicPlayerService == null) return;

                try {
                    iMusicPlayerService.next();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.iv_play_list:
                break;
        }
    }

    /**
     * 校验播放和暂停的按钮
     */
    private void checkPlayMode(boolean isShowToast) {
        if (iMusicPlayerService == null) return;

        try {
            int playMode = iMusicPlayerService.getPlayMode();
            String message;
            if (playMode == MusicPlayerService.ORDER_NORMAL) {
                ivPlayMode.setImageResource(R.drawable.ic_play_mode_normal_order);
                message = "顺序播放";
            } else if (playMode == MusicPlayerService.SINGLE_REPEAT) {
                ivPlayMode.setImageResource(R.drawable.ic_play_mode_single_repeat);
                message = "单曲循环";
            } else if (playMode == MusicPlayerService.RANDOM) {
                ivPlayMode.setImageResource(R.drawable.ic_play_mode_random);
                message = "随机播放";
            } else {
                ivPlayMode.setImageResource(R.drawable.ic_play_mode_normal_order);
                message = "顺序播放";
            }

            if (isShowToast)
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 校验播放和暂停的按钮
     */
    private void checkPlayAndPause() {
        if (iMusicPlayerService == null) return;

        try {
            if (iMusicPlayerService.isPlaying()) {
                ivPlayOrPause.setImageResource(R.drawable.ic_pause);
            } else {
                ivPlayOrPause.setImageResource(R.drawable.ic_play);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示歌词
     */
    private void showLyric() {
        if (iMusicPlayerService == null) return;

        try {
            SongBean songBean = iMusicPlayerService.getCurrentSong();
            if (songBean == null) return;
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        File file = new File("/storage/emulated/0/Music/郭静_心墙.lrc");
        if (!file.exists()) {
            Toast.makeText(this, "没有歌词", Toast.LENGTH_SHORT).show();
        }

        lyricUtils.readLyricFile(file);// 解析歌词
        showLyricView.setLyrics(lyricUtils.getLyricBeans());

        if (lyricUtils.isExistsLyric()) {
            handler.sendEmptyMessage(Constant.SHOW_LYRIC);
        }
    }

    /**
     * 绑定并且启动 MusicPlayerService
     */
    private void bindAndStartService() {
        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.setAction(Constant.MUSIC_PLAYER_SERVICE);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);// 不至于实例化多个服务
        startService(intent);
    }

    private ServiceConnection conn = new ServiceConnection() {

        /**
         * 当连接成功的时候回调这个方法
         * @param name
         * @param service
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iMusicPlayerService = IMusicPlayerService.Stub.asInterface(service);
            if (iMusicPlayerService != null) {
                // 设置页面展示数据
                setViewData();
                // 校验播放模式按钮
                checkPlayMode(false);
                // 校验播放和暂停的按钮
                checkPlayAndPause();
                // 显示歌词
                showLyric();
            }
        }

        /**
         * 当断开连接的时候回调这个方法
         * @param name
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (iMusicPlayerService != null) {
                try {
                    iMusicPlayerService.stop();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                iMusicPlayerService = null;
            }
        }
    };

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);

        // 取消注册广播
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }

        // 解绑服务
        if (conn != null) {
            unbindService(conn);
            conn = null;
        }
        super.onDestroy();
    }
}