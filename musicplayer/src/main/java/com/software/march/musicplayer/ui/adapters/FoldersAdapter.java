package com.software.march.musicplayer.ui.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.software.march.appcommonlibrary.RecyclerViewAdapter;
import com.software.march.musicplayer.R;
import com.software.march.musicplayer.bean.SongBean;
import com.software.march.musicplayer.ui.fragments.MoreFragment;

import java.io.File;
import java.util.List;

/**
 * @author Doc.March
 * @version V 1.0
 * @Description
 * @date 2017/4/1
 */
public class FoldersAdapter extends RecyclerViewAdapter<String> {

    private int[] counts;

    public FoldersAdapter(Context context, List<String> data, int[] counts) {
        super(context, data);
        this.counts = counts;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_folders;
    }

    @Override
    public void convert(ViewHolder holder, int position, String item) {
        TextView tvFolder = (TextView) holder.findViewById(R.id.tv_folder);
        TextView tvInfo = (TextView) holder.findViewById(R.id.tv_info);
        ImageView ivMore = (ImageView) holder.findViewById(R.id.iv_more);

        tvFolder.setText(item.substring(item.lastIndexOf(File.separator) + 1));
        tvInfo.setText(counts[position] + "首 " + item.substring(0, item.lastIndexOf(File.separator) + 1));

        ivMore.setTag(item);
        ivMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String folderPath = (String) v.getTag();
                if (mContext instanceof AppCompatActivity) {
                    FragmentManager manager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                    MoreFragment.newInstance(folderPath).show(manager, "folder");
                }
            }
        });
    }
}