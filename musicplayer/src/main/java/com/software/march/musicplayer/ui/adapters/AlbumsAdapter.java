package com.software.march.musicplayer.ui.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.software.march.appcommonlibrary.RecyclerViewAdapter;
import com.software.march.musicplayer.R;
import com.software.march.musicplayer.bean.AlbumBean;

import java.util.List;

/**
 * @author Doc.March
 * @version V 1.0
 * @Description
 * @date 2017/4/3
 */
public class AlbumsAdapter extends RecyclerViewAdapter<AlbumBean> {

    public AlbumsAdapter(Context context, List<AlbumBean> data) {
        super(context, data);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_albums;
    }

    @Override
    public void convert(ViewHolder holder, int position, AlbumBean item) {
        ImageView ivIcon = (ImageView) holder.findViewById(R.id.iv_icon);
        TextView tvAlbum = (TextView) holder.findViewById(R.id.tv_album);
        TextView tvInfo = (TextView) holder.findViewById(R.id.tv_info);

        ivIcon.setTag(position);
        int tag = (int) ivIcon.getTag();
        if (position == tag) {
            if (item.getAlbumArt() != null &&
                    !item.getAlbumArt().equals("") &&
                    !item.getAlbumArt().equals("null")) {
                ivIcon.setImageURI(Uri.parse(item.getAlbumArt()));
            } else {
                ivIcon.setImageResource(R.drawable.ic_music);
            }
        }

        String album = item.getAlbum();
        if ("<unknown>".equals(album)) {
            album = "未知专辑";
        }
        String artist = item.getArtist();
        if ("<unknown>".equals(artist)) {
            artist = "未知艺人";
        }
        tvAlbum.setText(album);
        tvInfo.setText(artist + " | " + item.getNumberOfSongs() + " 首歌");

        Log.i("info", item.toString());
    }
}