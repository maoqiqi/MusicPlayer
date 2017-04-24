package com.software.march.musicplayer.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Doc.March
 * @version V 1.0
 * @Description
 * @date 2017/4/3
 */
public class SongBean implements Parcelable {

    // MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

    // 单曲编号
    private long _id;

    // 歌曲的演唱者
    private String artist;

    // 标题
    private String title;

    // 文件的显示名称 (singer + "-" + title)
    private String displayName;

    // 磁盘上的文件的路径
    private String data;

    // 文件的时长
    private long duration;

    // 文件的大小
    private long size;

    // 专辑id
    private long albumId;

    // 专辑名
    private String album;

    // 艺术家id
    private long artistId;

    public SongBean() {

    }

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

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getArtistId() {
        return artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "SongBean{" +
                "_id=" + _id +
                ", artist='" + artist + '\'' +
                ", title='" + title + '\'' +
                ", displayName='" + displayName + '\'' +
                ", data='" + data + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                ", albumId=" + albumId +
                ", album='" + album + '\'' +
                ", artistId=" + artistId +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_id);
        dest.writeString(artist);
        dest.writeString(title);
        dest.writeString(displayName);
        dest.writeString(data);
        dest.writeLong(duration);
        dest.writeLong(size);
        dest.writeLong(albumId);
        dest.writeString(album);
        dest.writeLong(artistId);
    }

    protected SongBean(Parcel in) {
        _id = in.readLong();
        artist = in.readString();
        title = in.readString();
        displayName = in.readString();
        data = in.readString();
        duration = in.readLong();
        size = in.readLong();
        albumId = in.readLong();
        album = in.readString();
        artistId = in.readLong();
    }

    public static final Creator<SongBean> CREATOR = new Creator<SongBean>() {
        @Override
        public SongBean createFromParcel(Parcel in) {
            return new SongBean(in);
        }

        @Override
        public SongBean[] newArray(int size) {
            return new SongBean[size];
        }
    };
}