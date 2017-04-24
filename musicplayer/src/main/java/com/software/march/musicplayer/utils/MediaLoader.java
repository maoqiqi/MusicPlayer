package com.software.march.musicplayer.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.software.march.musicplayer.bean.AlbumBean;
import com.software.march.musicplayer.bean.ArtistBean;
import com.software.march.musicplayer.bean.SongBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Doc.March
 * @version V 1.0
 * @Description
 * @date 2017/4/3
 */
public class MediaLoader {

    public static final int FILTER_SIZE = 1 * 1024 * 1024;// 1MB
    public static final int FILTER_DURATION = 1 * 60 * 1000;// 1分钟

    private static List<SongBean> getSongsForCursor(Cursor cursor) {
        List<SongBean> list = new ArrayList();
        try {
            if ((cursor != null) && (cursor.moveToFirst())) {
                // cursor.getColumnIndex:得到指定列名的索引号,就是说这个字段是第几列
                // cursor.getString(columnIndex) 可以得到当前行的第几列的值
                int _id = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
                int artist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                int title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                int displayName = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
                int data = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                int duration = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
                int size = cursor.getColumnIndex(MediaStore.Audio.Media.SIZE);
                int albumId = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
                int album = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
                int artistId = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID);

                do {
                    SongBean bean = new SongBean();

                    bean.set_id(cursor.getLong(_id));
                    bean.setArtist(cursor.getString(artist));
                    bean.setTitle(cursor.getString(title));
                    bean.setDisplayName(cursor.getString(displayName));
                    bean.setData(cursor.getString(data));
                    bean.setDuration(cursor.getLong(duration));
                    bean.setSize(cursor.getLong(size));
                    bean.setAlbumId(cursor.getLong(albumId));
                    bean.setAlbum(cursor.getString(album));
                    bean.setArtistId(cursor.getLong(artistId));

                    list.add(bean);
                }
                while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    private static List<ArtistBean> getArtistsForCursor(Cursor cursor) {
        List<ArtistBean> list = new ArrayList();
        try {
            if ((cursor != null) && (cursor.moveToFirst())) {
                int _id = cursor.getColumnIndex(MediaStore.Audio.Artists._ID);
                int artist = cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST);
                int numberOfAlbums = cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS);
                int numberOfTracks = cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS);

                do {
                    ArtistBean bean = new ArtistBean();

                    bean.set_id(cursor.getLong(_id));
                    bean.setArtist(cursor.getString(artist));
                    bean.setNumberOfAlbums(cursor.getLong(numberOfAlbums));
                    bean.setNumberOfTracks(cursor.getLong(numberOfTracks));

                    list.add(bean);
                }
                while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    private static List<AlbumBean> getAlbumsForCursor(Cursor cursor) {
        List<AlbumBean> list = new ArrayList();
        try {
            if ((cursor != null) && (cursor.moveToFirst())) {
                int _id = cursor.getColumnIndex(MediaStore.Audio.Albums._ID);
                int album = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
                int artist = cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST);
                int numberOfSongs = cursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS);
                int minYear = cursor.getColumnIndex(MediaStore.Audio.Albums.FIRST_YEAR);
                int albumArt = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
                do {
                    AlbumBean bean = new AlbumBean();

                    bean.set_id(cursor.getLong(_id));
                    bean.setAlbum(cursor.getString(album));
                    bean.setArtist(cursor.getString(artist));
                    bean.setNumberOfSongs(cursor.getLong(numberOfSongs));
                    bean.setMinYear(cursor.getLong(minYear));
                    bean.setAlbumArt(cursor.getString(albumArt));

                    list.add(bean);
                }
                while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    private static List<String> getFoldersForCursor(Cursor cursor) {
        List<String> list = new ArrayList<>();
        try {
            if ((cursor != null) && (cursor.moveToFirst())) {
                int data = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);

                String filePath;
                do {
                    filePath = cursor.getString(data);
                    list.add(filePath.substring(0, filePath.lastIndexOf(File.separator)));
                }
                while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    /**
     * 得到 Cursor
     *
     * @param context
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    private static Cursor makeCursor(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (context == null)
            return null;

        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(uri, projection, selection, selectionArgs, sortOrder);
        return cursor;
    }

    /**
     * 得到歌曲列表
     *
     * @param context
     * @param artistId
     * @param albumId
     * @return
     */
    public static List<SongBean> getSongs(Context context, long artistId, long albumId, String folderPath) {
        // Uri 地址
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        // 查询字段
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST_ID
        };
        // 帅选条件(MediaStore.Audio.Media.IS_MUSIC=1 and MediaStore.Audio.Media.TITLE!='')
        StringBuilder builder = new StringBuilder(MediaStore.Audio.Media.IS_MUSIC + " = 1 " + " AND " + MediaStore.Audio.Media.TITLE + " != ''");
        if (artistId != 0) {
            builder.append(" AND " + MediaStore.Audio.Media.ARTIST_ID + " = " + artistId);
        }
        if (albumId != 0) {
            builder.append(" AND " + MediaStore.Audio.Media.ALBUM_ID + " = " + albumId);
        }
        String selection = builder.toString();
        // 帅选条件参数
        String[] selectionArgs = null;
        // 排序
        String sortOrder = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;

        Cursor cursor = makeCursor(context, uri, projection, selection, selectionArgs, sortOrder);

        List<SongBean> songBeans = getSongsForCursor(cursor);
        if (folderPath == null)
            return songBeans;
        else {
            List<SongBean> list = new ArrayList<>();
            String data;
            for (int j = 0; j < songBeans.size(); j++) {
                data = songBeans.get(j).getData();
                if (data.substring(0, data.lastIndexOf(File.separator)).equals(folderPath)) {
                    list.add(songBeans.get(j));
                }
            }
            return list;
        }
    }

    /**
     * 得到艺术家列表
     *
     * @param context
     * @return
     */
    public static List<ArtistBean> getArtists(Context context) {
        Uri uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Artists._ID,
                MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
                MediaStore.Audio.Artists.NUMBER_OF_TRACKS
        };
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = MediaStore.Audio.Artists.DEFAULT_SORT_ORDER;

        Cursor cursor = makeCursor(context, uri, projection, selection, selectionArgs, sortOrder);

        return getArtistsForCursor(cursor);
    }

    /**
     * 得到专辑列表
     *
     * @param context
     * @return
     */
    public static List<AlbumBean> getAlbums(Context context) {
        Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS,
                MediaStore.Audio.Albums.FIRST_YEAR,
                MediaStore.Audio.Albums.ALBUM_ART
        };
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = MediaStore.Audio.Albums.DEFAULT_SORT_ORDER;

        Cursor cursor = makeCursor(context, uri, projection, selection, selectionArgs, sortOrder);

        return getAlbumsForCursor(cursor);
    }

    /**
     * 获取包含音频文件的文件夹信息
     *
     * @param context
     * @return
     */
    public static List<String> getFolders(Context context) {
        Uri uri = MediaStore.Files.getContentUri("external");
        String[] projection = {MediaStore.Files.FileColumns.DATA};

        // 查询语句：检索出.mp3为后缀名,时长大于1分钟,文件大小大于1MB的媒体文件
        StringBuilder builder = new StringBuilder();
        builder.append(MediaStore.Files.FileColumns.MEDIA_TYPE + " = " + MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO);
        builder.append(" and " + "(" + MediaStore.Files.FileColumns.DATA + " like '%.mp3' or " + MediaStore.Audio.Media.DATA + " like'%.wma')");
        builder.append(" and " + MediaStore.Audio.Media.SIZE + " > " + FILTER_SIZE);
        builder.append(" and " + MediaStore.Audio.Media.DURATION + " > " + FILTER_DURATION);
        builder.append(") group by ( " + MediaStore.Files.FileColumns.PARENT);

        String selection = builder.toString();
        String[] selectionArgs = null;
        String sortOrder = null;

        Cursor cursor = makeCursor(context, uri, projection, selection, selectionArgs, sortOrder);

        return getFoldersForCursor(cursor);
    }

    /**
     * 通过album_id,找到其所对应的专辑图片路径
     *
     * @param context
     * @param albumId
     * @return
     */
    public static String getAlbumArt(Context context, long albumId) {
        String albumArt = "";

        Uri uri = Uri.parse(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI + "/" + albumId);
        String[] projection = new String[]{MediaStore.Audio.Albums.ALBUM_ART};
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = MediaStore.Audio.Artists.DEFAULT_SORT_ORDER;

        Cursor cursor = makeCursor(context, uri, projection, selection, selectionArgs, sortOrder);

        try {
            if (cursor.getCount() > 0) {
                cursor.moveToNext();
                albumArt = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return albumArt;
    }
}