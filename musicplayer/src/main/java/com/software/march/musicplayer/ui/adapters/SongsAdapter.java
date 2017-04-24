package com.software.march.musicplayer.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.software.march.appcommonlibrary.RecyclerViewAdapter;
import com.software.march.musicplayer.R;
import com.software.march.musicplayer.bean.SongBean;
import com.software.march.musicplayer.ui.fragments.MoreFragment;
import com.software.march.musicplayer.ui.fragments.PlayerControllerFragment;

import java.util.List;

/**
 * @author Doc.March
 * @version V 1.0
 * @Description
 * @date 2017/4/3
 */
public class SongsAdapter extends RecyclerViewAdapter<SongBean> {

    private PlayerControllerFragment playerControllerFragment;
    private int color;

    public SongsAdapter(Context context, List<SongBean> data) {
        super(context, data);
        this.playerControllerFragment = PlayerControllerFragment.getInstance();
        this.color = ContextCompat.getColor(mContext, R.color.colorPrimary);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_songs;
    }

    @Override
    public void convert(ViewHolder holder, int position, SongBean item) {
        ImageView ivIcon = (ImageView) holder.findViewById(R.id.iv_icon);
        TextView tvName = (TextView) holder.findViewById(R.id.tv_name);
        TextView tvInfo = (TextView) holder.findViewById(R.id.tv_info);
        ImageView ivMore = (ImageView) holder.findViewById(R.id.iv_more);

        tvName.setTextColor(Color.parseColor("#212121"));
        // 判断该条目音乐是否在播放
        if (playerControllerFragment != null &&
                playerControllerFragment.getCurrentSongId() == item.get_id()) {
            tvName.setTextColor(color);
        }

        String displayName = item.getDisplayName();

        String name;
        String artist;

        int start = displayName.indexOf(" - ");
        if (start != -1) {
            name = displayName.substring(start + " - ".length(), displayName.lastIndexOf("."));
            artist = displayName.substring(0, start);
        } else {
            name = item.getTitle();
            artist = item.getArtist();
        }

        if ("<unknown>".equals(artist)) {
            artist = "未知艺人";
        }
        String album = item.getAlbum();
        if ("<unknown>".equals(album)) {
            album = "未知专辑";
        }

        tvName.setText(name);
        tvInfo.setText(artist + "·" + album);

        ivMore.setTag(item);
        ivMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SongBean songBean = (SongBean) v.getTag();
                if (mContext instanceof AppCompatActivity) {
                    FragmentManager manager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                    MoreFragment.newInstance(songBean).show(manager, "song");
                }
            }
        });
    }
}