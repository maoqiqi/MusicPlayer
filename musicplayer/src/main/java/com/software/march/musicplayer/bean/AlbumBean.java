package com.software.march.musicplayer.bean;

import java.io.Serializable;

/**
 * @author Doc.March
 * @version V 1.0
 * @Description
 * @date 2017/4/3
 */
public class AlbumBean implements Serializable {

    // MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI

    // 专辑id
    private long _id;

    // 专辑名
    private String album;

    // 专辑歌手
    private String artist;

    // 专辑歌曲数
    private long numberOfSongs;

    private long minYear;

    private String albumArt;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getMinYear() {
        return minYear;
    }

    public void setMinYear(long minYear) {
        this.minYear = minYear;
    }

    public long getNumberOfSongs() {
        return numberOfSongs;
    }

    public void setNumberOfSongs(long numberOfSongs) {
        this.numberOfSongs = numberOfSongs;
    }

    @Override
    public String toString() {
        return "AlbumBean{" +
                "_id=" + _id +
                ", album='" + album + '\'' +
                ", artist='" + artist + '\'' +
                ", numberOfSongs=" + numberOfSongs +
                ", minYear=" + minYear +
                ", albumArt='" + albumArt + '\'' +
                '}';
    }
}