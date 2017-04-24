package com.software.march.musicplayer.ui.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.software.march.appcommonlibrary.BaseFragment;
import com.software.march.musicplayer.R;
import com.software.march.musicplayer.bean.AlbumBean;
import com.software.march.musicplayer.ui.adapters.AlbumsAdapter;
import com.software.march.musicplayer.utils.MediaLoader;

import java.util.ArrayList;

/**
 * @author Doc.March
 * @version V 1.0
 * @Description 排行列表
 * @date 2017/4/1
 */
public class RankingFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private ArrayList<AlbumBean> albumBeans;
    private AlbumsAdapter adapter;

    /**
     * Use this factory method to create a new instance of this fragment using the provided parameters.
     *
     * @return A new instance of fragment RankingFragment.
     */
    public static RankingFragment newInstance() {
        RankingFragment fragment = new RankingFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_albums;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        albumBeans = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void loadData() {
        super.loadData();
        new LoadAlbums().execute();
    }

    private class LoadAlbums extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            albumBeans.addAll(MediaLoader.getAlbums(mContext));
            return "SUCCESS";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("SUCCESS")) {
                adapter = new AlbumsAdapter(mContext, albumBeans);
                recyclerView.setAdapter(adapter);
            }
        }
    }
}