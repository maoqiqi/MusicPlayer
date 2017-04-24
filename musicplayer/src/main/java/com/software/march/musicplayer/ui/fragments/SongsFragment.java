package com.software.march.musicplayer.ui.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.software.march.appcommonlibrary.BaseFragment;
import com.software.march.appcommonlibrary.RecyclerViewAdapter;
import com.software.march.musicplayer.R;
import com.software.march.musicplayer.bean.SongBean;
import com.software.march.musicplayer.service.MusicPlayerService;
import com.software.march.musicplayer.ui.adapters.SongsAdapter;
import com.software.march.musicplayer.utils.MediaLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Doc.March
 * @version V 1.0
 * @Description 歌曲列表
 * @date 2017/4/1
 */
public class SongsFragment extends BaseFragment {

    private int type;
    private long artistId;
    private long albumId;
    private String folderPath;

    private PlayerControllerFragment playerControllerFragment;

    private RecyclerView recyclerView;
    private List<SongBean> songBeans;
    private SongsAdapter adapter;

    /**
     * Use this factory method to create a new instance of this fragment using the provided parameters.
     *
     * @return A new instance of fragment SongsFragment.
     */
    public static SongsFragment newInstance() {
        SongsFragment fragment = new SongsFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_songs;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt("type", 0);
            if (type == 0) {
                artistId = bundle.getLong("artistId", 0);
                albumId = bundle.getLong("albumId", 0);
                folderPath = bundle.getString("folderPath", null);
            }
        }

        playerControllerFragment = PlayerControllerFragment.getInstance();

        setHasOptionsMenu(true);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        songBeans = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        // 如果可以确定每个item的高度是固定的,设置这个选项可以提高性能
        recyclerView.setHasFixedSize(true);

        registerReceivers();
    }

    /**
     * 注册广播
     */
    private void registerReceivers() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicPlayerService.OPEN_AUDIO);
        intentFilter.addAction(MusicPlayerService.STOP_AUDIO);
        mContext.registerReceiver(receiver, intentFilter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MusicPlayerService.OPEN_AUDIO)) {
                notifyDataSetChanged();
            } else if (action.equals(MusicPlayerService.STOP_AUDIO)) {
                notifyDataSetChanged();
            }
        }
    };

    @Override
    public void loadData() {
        super.loadData();
        if (type == 0) {
            new LoadSongs().execute();
        } else if (type == 1) {
            if (playerControllerFragment != null) {
                songBeans = playerControllerFragment.getPlayList();
                if (songBeans == null)
                    songBeans = new ArrayList<>();
                initAdapter();
            }
        }
    }

    private class LoadSongs extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            songBeans.addAll(MediaLoader.getSongs(mContext, artistId, albumId, folderPath));
            return "SUCCESS";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("SUCCESS")) {
                if (playerControllerFragment != null) {
                    playerControllerFragment.setPlayList(songBeans, true);
                }
                initAdapter();
            }
        }
    }

    private void initAdapter() {
        adapter = new SongsAdapter(mContext, songBeans);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                SongBean songBean = songBeans.get(position);
                if (playerControllerFragment != null) {
                    if (playerControllerFragment.getCurrentSongId() == songBean.get_id()) {
                        return;
                    } else {
                        playerControllerFragment.setPlayList(songBeans, false);
                        playerControllerFragment.play(position);
                    }
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (type == 0) {
            inflater.inflate(R.menu.menu_songs_sort, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * 更新数据
     */
    private void notifyDataSetChanged() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        // 取消注册广播
        if (receiver != null) {
            mContext.unregisterReceiver(receiver);
            receiver = null;
        }
        super.onDestroy();
    }
}