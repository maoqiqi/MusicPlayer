package com.software.march.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.software.march.musicplayer.bean.SongBean;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * @author Doc.March
 * @version V 1.0
 * @Description
 * @date 2017/4/10
 */
public class MusicPlayerService extends Service {

    public static final String OPEN_AUDIO = "com.software.march.musicplayer.service.open.audio";
    public static final String STOP_AUDIO = "com.software.march.musicplayer.service.stop.audio";

    /**
     * 顺序播放
     */
    public static final int ORDER_NORMAL = 1;

    /**
     * 单曲循环
     */
    public static final int SINGLE_REPEAT = 2;

    /**
     * 随机播放
     */
    public static final int RANDOM = 3;

    /**
     * 播放器对象
     */
    private MediaPlayer mediaPlayer;

    /**
     * 播放列表
     */
    private List<SongBean> mSongBeans;

    /**
     * 播放位置索引
     */
    private int mIndex;

    /**
     * 当前播放的音频文件对象
     */
    private SongBean mSongBean;

    /**
     * 播放模式
     */
    private int playMode = ORDER_NORMAL;

    /**
     * 是否是音乐准备好就开始播放
     */
    private boolean preparedStart = false;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    private IMusicPlayerService.Stub stub = new IMusicPlayerService.Stub() {

        MusicPlayerService musicPlayerService = MusicPlayerService.this;

        @Override
        public SongBean getCurrentSong() throws RemoteException {
            return musicPlayerService.getCurrentSong();
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return musicPlayerService.getCurrentPosition();
        }

        @Override
        public boolean isPreparedStart() throws RemoteException {
            return musicPlayerService.isPreparedStart();
        }

        @Override
        public void setPreparedStart(boolean preparedStart) throws RemoteException {
            musicPlayerService.setPreparedStart(preparedStart);
        }

        @Override
        public void openAudio() throws RemoteException {
            musicPlayerService.openAudio();
        }

        @Override
        public void openAudioForIndex(int index) throws RemoteException {
            musicPlayerService.openAudioForIndex(index);
        }

        @Override
        public void setPlayList(List<SongBean> songBeans) throws RemoteException {
            musicPlayerService.setPlayList(songBeans);
        }

        @Override
        public List<SongBean> getPlayList() throws RemoteException {
            return musicPlayerService.getPlayList();
        }

        @Override
        public void setNextPlay(SongBean songBean) throws RemoteException {
            musicPlayerService.setNextPlay(songBean);
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return musicPlayerService.isPlaying();
        }

        @Override
        public void start() throws RemoteException {
            musicPlayerService.start();
        }

        @Override
        public void pause() throws RemoteException {
            musicPlayerService.pause();
        }

        @Override
        public void stop() throws RemoteException {
            musicPlayerService.stop();
        }

        @Override
        public void previous() throws RemoteException {
            musicPlayerService.previous();
        }

        @Override
        public void next() throws RemoteException {
            musicPlayerService.next();
        }

        @Override
        public void seekTo(int position) throws RemoteException {
            musicPlayerService.seekTo(position);
        }

        @Override
        public void setPlayMode(int playMode) throws RemoteException {
            musicPlayerService.setPlayMode(playMode);
        }

        @Override
        public int getPlayMode() throws RemoteException {
            return musicPlayerService.getPlayMode();
        }
    };

    /**
     * 得到当前播放的歌曲信息
     *
     * @return
     */
    public SongBean getCurrentSong() {
        return mSongBean;
    }

    /**
     * 得到当前的播放进度
     *
     * @return
     */
    public int getCurrentPosition() {
        if (mediaPlayer != null)
            return mediaPlayer.getCurrentPosition();
        return 0;
    }

    /**
     * 得到是否是音乐准备好就开始播放
     *
     * @return
     */
    public boolean isPreparedStart() {
        return preparedStart;
    }

    /**
     * 设置是否是音乐准备好就开始播放
     *
     * @param preparedStart
     */
    public void setPreparedStart(boolean preparedStart) {
        this.preparedStart = preparedStart;
    }

    /**
     * 打开音频文件,并且播放
     */
    public void openAudio() {
        if (mSongBeans == null || mSongBeans.size() == 0)
            return;
        if (mIndex < 0 || mIndex > mSongBeans.size())
            return;

        this.mSongBean = mSongBeans.get(mIndex);

        if (mediaPlayer != null) {
            mediaPlayer.reset();// 释放资源
        }

        try {
            mediaPlayer = new MediaPlayer();
            // 设置监听：准备好,播放完成,播放出错
            mediaPlayer.setOnPreparedListener(new MyOnPreparedListener());
            mediaPlayer.setOnCompletionListener(new MyOnCompletionListener());
            mediaPlayer.setOnErrorListener(new MyOnErrorListener());
            mediaPlayer.setDataSource(mSongBean.getData());
            mediaPlayer.prepareAsync();

            if (playMode == MusicPlayerService.SINGLE_REPEAT) {
                mediaPlayer.setLooping(true);// 单曲循环播放-不会触发播放完成的回调
            } else {
                mediaPlayer.setLooping(false);// 不循环播放
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据位置打开对应的音频文件,并且播放
     *
     * @param index
     */
    public void openAudioForIndex(int index) {
        this.mIndex = index;
        openAudio();
    }

    /**
     * 设置播放列表
     *
     * @param songBeans
     */
    public void setPlayList(List<SongBean> songBeans) {
        this.mSongBeans = songBeans;
    }

    /**
     * 得到播放列表
     *
     * @return
     */
    public List<SongBean> getPlayList() {
        return mSongBeans;
    }

    /**
     * 设置为下一首播放
     *
     * @param songBean
     */
    public void setNextPlay(SongBean songBean) {
        if (songBean != null) {
            mSongBeans.add(mIndex, songBean);
        }
    }

    /**
     * 是否在播放音频
     *
     * @return
     */
    public boolean isPlaying() {
        if (mediaPlayer != null)
            return mediaPlayer.isPlaying();
        return false;
    }

    /**
     * 播放音乐
     */
    public void start() {
        if (mediaPlayer != null)
            mediaPlayer.start();
    }

    /**
     * 暂停音乐
     */
    public void pause() {
        if (mediaPlayer != null)
            mediaPlayer.pause();
    }

    /**
     * 停止
     */
    public void stop() {
        if (mediaPlayer != null)
            mediaPlayer.stop();
    }

    /**
     * 播放上一个音频
     */
    public void previous() {
        if (playMode == MusicPlayerService.RANDOM) {
            Random random = new Random();
            mIndex = random.nextInt(mSongBeans.size() - 1);
        } else {
            // 设置上一个的位置
            if (mIndex == 0) {
                mIndex = mSongBeans.size() - 1;
            } else {
                mIndex--;
            }
        }
        openAudioForIndex(mIndex);
    }

    /**
     * 播放下一个音频
     */
    public void next() {
        if (playMode == MusicPlayerService.RANDOM) {
            Random random = new Random();
            mIndex = random.nextInt(mSongBeans.size() - 1);
        } else {
            // 设置下一个的位置
            if (mIndex == mSongBeans.size() - 1) {
                mIndex = 0;
            } else {
                mIndex++;
            }
        }
        openAudioForIndex(mIndex);
    }

    /**
     * 拖动音频
     *
     * @param position
     */
    public void seekTo(int position) {
        if (mediaPlayer != null)
            mediaPlayer.seekTo(position);
    }

    /**
     * 设置播放模式
     *
     * @param playMode
     */
    public void setPlayMode(int playMode) {
        this.playMode = playMode;
        if (playMode == MusicPlayerService.SINGLE_REPEAT) {
            mediaPlayer.setLooping(true);
        } else {
            mediaPlayer.setLooping(false);
        }
    }

    /**
     * 得到播放模式
     *
     * @return
     */
    public int getPlayMode() {
        return playMode;
    }

    // 准备好
    private final class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {
            // 通知Activity来获取信息--广播
            notifyChange(OPEN_AUDIO);
            if (preparedStart) {
                start();
            } else {
                preparedStart = true;
            }
        }
    }

    // 播放完成
    private final class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            if (playMode == MusicPlayerService.ORDER_NORMAL) {
                if (mIndex == mSongBeans.size() - 1) {
                    // 结束播放
                    notifyChange(STOP_AUDIO);
                    return;
                }
            }
            next();
        }
    }

    // 播放出错
    private final class MyOnErrorListener implements MediaPlayer.OnErrorListener {

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            // 播放出现错误,播放下一首
            next();
            return true;
        }
    }

    /**
     * 根据动作发广播
     *
     * @param action
     */
    private void notifyChange(String action) {
        Intent intent = new Intent(action);
        sendBroadcast(intent);
    }
}