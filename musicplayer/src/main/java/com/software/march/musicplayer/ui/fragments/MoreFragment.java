package com.software.march.musicplayer.ui.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.software.march.appcommonlibrary.BaseDialogFragment;
import com.software.march.musicplayer.R;
import com.software.march.musicplayer.bean.AlbumBean;
import com.software.march.musicplayer.bean.ArtistBean;
import com.software.march.musicplayer.bean.MoreItemBean;
import com.software.march.musicplayer.bean.SongBean;
import com.software.march.musicplayer.ui.adapters.MoreAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Doc.March
 * @version V 1.0
 * @Description 更多
 * @date 2017/4/23
 */
public class MoreFragment extends DialogFragment {

    private static final int OVERFLOW_SONG = 0;
    private static final int OVERFLOW_ARTIST = 1;
    private static final int OVERFLOW_ALBUM = 2;
    private static final int OVERFLOW_FOLDER = 3;

    private int type;
    private SongBean songBean;

    private View mRootView;
    private TextView tvTitle;
    private RecyclerView recyclerView;
    private List<MoreItemBean> moreItemBeans;
    private MoreAdapter adapter;

    public static MoreFragment newInstance(SongBean songBean) {
        MoreFragment fragment = new MoreFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", OVERFLOW_SONG);
        bundle.putParcelable("songBean", songBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static MoreFragment newInstance(ArtistBean artistBean) {
        MoreFragment fragment = new MoreFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", OVERFLOW_ARTIST);
        bundle.putSerializable("artistBean", artistBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static MoreFragment newInstance(AlbumBean albumBean) {
        MoreFragment fragment = new MoreFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", OVERFLOW_FOLDER);
        bundle.putSerializable("albumBean", albumBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static MoreFragment newInstance(String folderPath) {
        MoreFragment fragment = new MoreFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", OVERFLOW_ALBUM);
        bundle.putSerializable("folderPath", folderPath);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        params.height = dm.heightPixels / 2;
        params.gravity = Gravity.BOTTOM;
        getDialog().getWindow().setAttributes(params);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_more, container, false);
        afterCreate(savedInstanceState);
        return mRootView;
    }

    protected void afterCreate(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        type = bundle.getInt("type", -1);
        if (type == -1) {
            return;
        } else if (type == OVERFLOW_SONG) {
            songBean = bundle.getParcelable("songBean");
        } else if (type == OVERFLOW_ARTIST) {

        } else if (type == OVERFLOW_ALBUM) {

        } else if (type == OVERFLOW_FOLDER) {

        }

        tvTitle = (TextView) mRootView.findViewById(R.id.tv_title);
        recyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        initData();
        adapter = new MoreAdapter(getActivity(), moreItemBeans);
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        if (type == OVERFLOW_SONG) {
            tvTitle.setText("歌曲：" + " " + songBean.getTitle());
        } else if (type == OVERFLOW_ARTIST) {
            tvTitle.setText("歌手：" + " ");
        } else if (type == OVERFLOW_ALBUM) {
            tvTitle.setText("专辑：" + " ");
        } else if (type == OVERFLOW_FOLDER) {
            tvTitle.setText("文件夹：" + " ");
        }

        moreItemBeans = new ArrayList<>();
        if (type == OVERFLOW_SONG) {
            moreItemBeans.add(new MoreItemBean("下一首播放", R.drawable.ic_more_next));
            moreItemBeans.add(new MoreItemBean("收藏到歌单", R.drawable.ic_more_fav));
            moreItemBeans.add(new MoreItemBean("分享", R.drawable.ic_more_share));
            moreItemBeans.add(new MoreItemBean("歌手：" + songBean.getArtist(), R.drawable.ic_more_artist));
            moreItemBeans.add(new MoreItemBean("专辑：" + songBean.getAlbum(), R.drawable.ic_more_album));
            moreItemBeans.add(new MoreItemBean("设为铃声", R.drawable.ic_more_ring));
            moreItemBeans.add(new MoreItemBean("查看歌曲信息", R.drawable.ic_more_document));
            moreItemBeans.add(new MoreItemBean("删除", R.drawable.ic_more_delete));
        } else {
            moreItemBeans.add(new MoreItemBean("下一首播放", R.drawable.ic_more_next));
            moreItemBeans.add(new MoreItemBean("收藏到歌单", R.drawable.ic_more_fav));
            moreItemBeans.add(new MoreItemBean("删除", R.drawable.ic_more_delete));
        }
    }
}