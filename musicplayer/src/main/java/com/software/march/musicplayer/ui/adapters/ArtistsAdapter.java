package com.software.march.musicplayer.ui.adapters;

import android.content.Context;
import android.widget.TextView;

import com.software.march.appcommonlibrary.RecyclerViewAdapter;
import com.software.march.musicplayer.R;
import com.software.march.musicplayer.bean.ArtistBean;

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

        String artist = item.getArtist();
        if ("<unknown>".equals(artist)) {
            artist = "未知艺人";
        }
        tvArtist.setText(artist);
        tvInfo.setText(item.getNumberOfAlbums() + " 张专辑 | " + item.getNumberOfTracks() + " 首歌");
    }
}