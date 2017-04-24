package com.software.march.musicplayer.ui.adapters;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.software.march.appcommonlibrary.RecyclerViewAdapter;
import com.software.march.musicplayer.R;
import com.software.march.musicplayer.bean.MoreItemBean;

import java.util.List;

/**
 * @author Doc.March
 * @version V 1.0
 * @Description
 * @date 2017/4/23
 */
public class MoreAdapter extends RecyclerViewAdapter<MoreItemBean> {

    public MoreAdapter(Context context, List<MoreItemBean> data) {
        super(context, data);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_more;
    }

    @Override
    public void convert(ViewHolder holder, int position, MoreItemBean item) {
        ImageView iv = (ImageView) holder.findViewById(R.id.iv);
        TextView tvTitle = (TextView) holder.findViewById(R.id.tv_title);

        iv.setImageResource(item.getImageId());
        tvTitle.setText(item.getTitle());
    }
}