package com.software.march.musicplayer.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.software.march.appcommonlibrary.ScreenUtils;
import com.software.march.musicplayer.R;
import com.software.march.musicplayer.bean.AlbumBean;
import com.software.march.musicplayer.bean.ArtistBean;
import com.software.march.musicplayer.bean.MoreItemBean;
import com.software.march.musicplayer.bean.SongBean;
import com.software.march.musicplayer.ui.adapters.MoreAdapter;

import java.io.File;
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
    private ArtistBean artistBean;
    private AlbumBean albumBean;
    private String folderPath;

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
        bundle.putInt("type", OVERFLOW_ALBUM);
        bundle.putSerializable("albumBean", albumBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static MoreFragment newInstance(String folderPath) {
        MoreFragment fragment = new MoreFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", OVERFLOW_FOLDER);
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

        if (type == OVERFLOW_SONG)
            params.height = ScreenUtils.getScreenHeight(getActivity()) / 2;
        else
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        params.gravity = Gravity.BOTTOM;
        getDialog().getWindow().setAttributes(params);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setWindowAnimations(R.style.AnimationDialogStyle);
        mRootView = inflater.inflate(R.layout.fragment_more, container, false);
        afterCreate();
        return mRootView;
    }

    protected void afterCreate() {
        Bundle bundle = getArguments();
        type = bundle.getInt("type", -1);
        if (type == -1) {
            return;
        } else if (type == OVERFLOW_SONG) {
            songBean = bundle.getParcelable("songBean");
        } else if (type == OVERFLOW_ARTIST) {
            artistBean = (ArtistBean) bundle.getSerializable("artistBean");
        } else if (type == OVERFLOW_ALBUM) {
            albumBean = (AlbumBean) bundle.getSerializable("albumBean");
        } else if (type == OVERFLOW_FOLDER) {
            folderPath = bundle.getString("folderPath");
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
            String artist = artistBean.getArtist();
            if ("<unknown>".equals(artist)) artist = "未知艺人";
            tvTitle.setText("歌手：" + " " + artist);
        } else if (type == OVERFLOW_ALBUM) {
            String album = albumBean.getAlbum();
            if ("<unknown>".equals(album)) album = "未知专辑";
            tvTitle.setText("专辑：" + " " + album);
        } else if (type == OVERFLOW_FOLDER) {
            String folderName = folderPath.substring(folderPath.lastIndexOf(File.separator) + 1);
            tvTitle.setText("文件夹：" + " " + folderName);
        }

        moreItemBeans = new ArrayList<>();
        if (type == OVERFLOW_SONG) {
            String artist = songBean.getArtist();
            String album = songBean.getAlbum();

            if ("<unknown>".equals(artist)) artist = "未知艺人";
            if ("<unknown>".equals(album)) album = "未知专辑";

            moreItemBeans.add(new MoreItemBean("下一首播放", R.drawable.ic_more_next));
            moreItemBeans.add(new MoreItemBean("收藏到歌单", R.drawable.ic_more_fav));
            moreItemBeans.add(new MoreItemBean("分享", R.drawable.ic_more_share));
            moreItemBeans.add(new MoreItemBean("歌手：" + artist, R.drawable.ic_more_artist));
            moreItemBeans.add(new MoreItemBean("专辑：" + album, R.drawable.ic_more_album));
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