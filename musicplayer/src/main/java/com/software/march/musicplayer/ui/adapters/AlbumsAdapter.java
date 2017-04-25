package com.software.march.musicplayer.ui.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.software.march.appcommonlibrary.RecyclerViewAdapter;
import com.software.march.musicplayer.R;
import com.software.march.musicplayer.bean.AlbumBean;
import com.software.march.musicplayer.bean.ArtistBean;
import com.software.march.musicplayer.ui.fragments.MoreFragment;

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
        ImageView ivMore = (ImageView) holder.findViewById(R.id.iv_more);

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

        String artist = item.getArtist();
        String album = item.getAlbum();

        if ("<unknown>".equals(artist)) artist = "未知艺人";
        if ("<unknown>".equals(album)) album = "未知专辑";

        tvAlbum.setText(album);
        tvInfo.setText(artist + " | " + item.getNumberOfSongs() + " 首歌");

        ivMore.setTag(item);
        ivMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlbumBean albumBean = (AlbumBean) v.getTag();
                if (mContext instanceof AppCompatActivity) {
                    FragmentManager manager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                    MoreFragment.newInstance(albumBean).show(manager, "album");
                }
            }
        });
    }
}