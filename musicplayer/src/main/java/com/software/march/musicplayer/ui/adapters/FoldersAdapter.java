package com.software.march.musicplayer.ui.adapters;

import android.content.Context;
import android.widget.TextView;

import com.software.march.appcommonlibrary.RecyclerViewAdapter;
import com.software.march.musicplayer.R;

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

        tvFolder.setText(item.substring(item.lastIndexOf(File.separator) + 1));
        tvInfo.setText(counts[position] + "首 " + item.substring(0, item.lastIndexOf(File.separator) + 1));
    }
}