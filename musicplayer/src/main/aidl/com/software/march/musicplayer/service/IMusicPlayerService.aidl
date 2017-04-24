// IMusicPlayerService.aidl
package com.software.march.musicplayer.service;

// Declare any non-default types here with import statements
import com.software.march.musicplayer.bean.SongBean;

interface IMusicPlayerService {

    /**
     * 得到当前播放的歌曲信息
     *
     * @return
     */
    SongBean getCurrentSong();

    /**
     * 得到当前的播放进度
     *
     * @return
     */
    int getCurrentPosition();

    /**
     * 得到是否是音乐准备好就开始播放
     */
    boolean isPreparedStart();

    /**
     * 设置是否是音乐准备好就开始播放
     */
    void setPreparedStart(boolean preparedStart);

    /**
     * 打开音频文件,并且播放
     */
    void openAudio() ;

    /**
     * 根据位置打开对应的音频文件,并且播放
     *
     * @param position
     */
    void openAudioForIndex(int index);

    /**
     * 设置播放列表
     *
     * @param songBeans
     */
    void setPlayList(in List<SongBean> songBeans);

    /**
     * 得到播放列表
     *
     * @return
     */
    List<SongBean> getPlayList();

    /**
     * 设置为下一首播放
     *
     * @param songBean
     */
    void setNextPlay(in SongBean songBean);

    /**
     * 是否在播放音频
     *
     * @return
     */
    boolean isPlaying();

    /**
     * 播放音乐
     */
    void start();

    /**
     * 暂停音乐
     */
    void pause();

    /**
     * 停止
     */
    void stop();

    /**
     * 播放上一个音频
     */
    void previous();

    /**
     * 播放下一个音频
     */
    void next();

    /**
     * 拖动音频
     *
     * @param position
     */
    void seekTo(int position);

    /**
     * 设置播放模式
     *
     * @param playMode
     */
    void setPlayMode(int playMode);

    /**
     * 得到播放模式
     *
     * @return
     */
    int getPlayMode();
}