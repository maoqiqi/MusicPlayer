package com.software.march.musicplayer.ui.fragments;

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
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.software.march.appcommonlibrary.BaseFragment;
import com.software.march.musicplayer.R;
import com.software.march.musicplayer.bean.SongBean;
import com.software.march.musicplayer.service.IMusicPlayerService;
import com.software.march.musicplayer.service.MusicPlayerService;
import com.software.march.musicplayer.ui.activities.MusicPlayerActivity;
import com.software.march.musicplayer.utils.Constant;

import java.util.List;

/**
 * @author Doc.March
 * @version V 1.0
 * @Description
 * @date 2017/4/19
 */
public class PlayerControllerFragment extends BaseFragment implements View.OnClickListener {

    private static PlayerControllerFragment instance;

    private RelativeLayout rlPlayerController;
    private ImageView imageView;
    private SeekBar seekBar;
    private TextView tvName;
    private TextView tvArtist;
    private ImageView ivPlayOrPause;
    private ImageView ivNext;
    private ImageView ivPlayList;

    private List<SongBean> songBeans;
    private IMusicPlayerService iMusicPlayerService;
    private boolean mFromUser;

    public static PlayerControllerFragment getInstance() {
        return instance;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_player_controller;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        instance = this;

        rlPlayerController = (RelativeLayout) findViewById(R.id.rl_player_controller);
        imageView = (ImageView) findViewById(R.id.image_view);
        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvArtist = (TextView) findViewById(R.id.tv_artist);
        ivPlayOrPause = (ImageView) findViewById(R.id.iv_play_or_pause);
        ivNext = (ImageView) findViewById(R.id.iv_next);
        ivPlayList = (ImageView) findViewById(R.id.iv_play_list);

        seekBar.setEnabled(false);
        rlPlayerController.setOnClickListener(this);

        registerReceivers();
    }

    /**
     * 注册广播
     */
    private void registerReceivers() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicPlayerService.OPEN_AUDIO);
        intentFilter.addAction(MusicPlayerService.STOP_AUDIO);
        mContext.registerReceiver(receiver, intentFilter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MusicPlayerService.OPEN_AUDIO)) {
                // 设置页面展示数据
                setViewData();
                // 校验播放和暂停的按钮
                checkPlayAndPause();
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

            String artist = songBean.getArtist();
            if ("<unknown>".equals(artist)) artist = "未知艺人";

            tvName.setText(songBean.getTitle());
            tvArtist.setText(artist);

            int duration = (int) songBean.getDuration();
            seekBar.setMax(duration);

            handler.sendEmptyMessage(Constant.PROGRESS);

            seekBar.setEnabled(true);
            // 设置拖动监听
            seekBar.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());

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

                        handler.removeMessages(Constant.PROGRESS);
                        if (iMusicPlayerService.isPlaying()) {
                            // 每秒更新一次
                            handler.sendEmptyMessageDelayed(Constant.PROGRESS, 1000);
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
            case R.id.rl_player_controller:
                Intent intent = new Intent(mContext, MusicPlayerActivity.class);
                mContext.startActivity(intent);
                break;
            case R.id.iv_play_or_pause:
                if (iMusicPlayerService == null) return;

                try {
                    if (iMusicPlayerService.isPlaying()) {
                        iMusicPlayerService.pause();
                        handler.removeMessages(Constant.PROGRESS);
                    } else {
                        iMusicPlayerService.start();
                        handler.sendEmptyMessage(Constant.PROGRESS);
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
    private void checkPlayAndPause() {
        if (iMusicPlayerService == null) return;

        try {
            if (iMusicPlayerService.isPlaying()) {
                ivPlayOrPause.setImageResource(R.drawable.ic_pause_selector);
            } else {
                ivPlayOrPause.setImageResource(R.drawable.ic_play_selector);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到播放列表
     *
     * @return
     */
    public List<SongBean> getPlayList() {
        try {
            return iMusicPlayerService.getPlayList();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 得到当前播放的歌曲信息
     *
     * @return
     */
    public long getCurrentSongId() {
        if (iMusicPlayerService == null) return 0;

        try {
            return iMusicPlayerService.getCurrentSong().get_id();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 设置播放列表
     *
     * @param songBeans
     */
    public void setPlayList(List<SongBean> songBeans, boolean isInit) {
        this.songBeans = songBeans;
        if (isInit) {
            bindAndStartService();
        } else {
            try {
                iMusicPlayerService.setPlayList(songBeans);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 播放某一首
     *
     * @param index
     */
    public void play(int index) {
        if (iMusicPlayerService == null) return;

        try {
            iMusicPlayerService.setPreparedStart(true);
            iMusicPlayerService.openAudioForIndex(index);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 绑定并且启动 MusicPlayerService
     */
    private void bindAndStartService() {
        Intent intent = new Intent(mContext, MusicPlayerService.class);
        intent.setAction(Constant.MUSIC_PLAYER_SERVICE);
        mContext.bindService(intent, conn, Context.BIND_AUTO_CREATE);// 不至于实例化多个服务
        mContext.startService(intent);
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
                try {
                    if (iMusicPlayerService.getPlayList() == null || iMusicPlayerService.getPlayList().size() == 0) {
                        iMusicPlayerService.setPlayList(songBeans);
                        iMusicPlayerService.openAudio();
                    }
                    // 设置页面展示数据
                    setViewData();
                    // 校验播放和暂停的按钮
                    checkPlayAndPause();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
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
    public void onResume() {
        super.onResume();
        if (iMusicPlayerService == null) return;

        // 设置页面展示数据
        setViewData();
        // 校验播放和暂停的按钮
        checkPlayAndPause();
    }

    @Override
    public void onDestroy() {
        if (instance != null) {
            instance = null;
        }

        // 取消注册广播
        if (receiver != null) {
            mContext.unregisterReceiver(receiver);
            receiver = null;
        }

        // 解绑服务
        if (conn != null) {
            mContext.unbindService(conn);
            conn = null;
        }
        super.onDestroy();
    }
}