package com.software.march.musicplayer.ui.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.software.march.appcommonlibrary.RecyclerViewAdapter;
import com.software.march.musicplayer.R;
import com.software.march.musicplayer.bean.ArtistBean;
import com.software.march.musicplayer.bean.SongBean;
import com.software.march.musicplayer.ui.fragments.MoreFragment;

import java.util.List;

/**
 * @author Doc.March
 * @version V 1.0
 * @Description
 * @date 2017/4/3
 */
public class ArtistsAdapter extends RecyclerViewAdapter<ArtistBean> {

    public ArtistsAdapter(Context context, List<ArtistBean> data) {
        super(context, data);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_artists;
    }

    @Override
    public void convert(ViewHolder holder, int position, ArtistBean item) {
        TextView tvArtist = (TextView) holder.findViewById(R.id.tv_artist);
        TextView tvInfo = (TextView) holder.findViewById(R.id.tv_info);
        ImageView ivMore = (ImageView) holder.findViewById(R.id.iv_more);

        String artist = item.getArtist();
        if ("<unknown>".equals(artist)) artist = "未知艺人";

        tvArtist.setText(artist);
        tvInfo.setText(item.getNumberOfAlbums() + " 张专辑 | " + item.getNumberOfTracks() + " 首歌");

        ivMore.setTag(item);
        ivMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ArtistBean artistBean = (ArtistBean) v.getTag();
                if (mContext instanceof AppCompatActivity) {
                    FragmentManager manager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                    MoreFragment.newInstance(artistBean).show(manager, "artist");
                }
            }
        });
    }
}