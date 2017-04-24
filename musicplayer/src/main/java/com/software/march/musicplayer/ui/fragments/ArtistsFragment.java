package com.software.march.musicplayer.ui.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.software.march.appcommonlibrary.BaseFragment;
import com.software.march.appcommonlibrary.RecyclerViewAdapter;
import com.software.march.musicplayer.R;
import com.software.march.musicplayer.bean.ArtistBean;
import com.software.march.musicplayer.ui.activities.ArtistsActivity;
import com.software.march.musicplayer.ui.adapters.ArtistsAdapter;
import com.software.march.musicplayer.utils.MediaLoader;

import java.util.ArrayList;

/**
 * @author Doc.March
 * @version V 1.0
 * @Description 艺术家列表
 * @date 2017/4/1
 */
public class ArtistsFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private ArrayList<ArtistBean> artistBeans;
    private ArtistsAdapter adapter;

    /**
     * Use this factory method to create a new instance of this fragment using the provided parameters.
     *
     * @return A new instance of fragment ArtistsFragment.
     */
    public static ArtistsFragment newInstance() {
        ArtistsFragment fragment = new ArtistsFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_artists;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        artistBeans = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void loadData() {
        super.loadData();
        new LoadArtists().execute();
    }

    private class LoadArtists extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            artistBeans.addAll(MediaLoader.getArtists(mContext));
            return "SUCCESS";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("SUCCESS")) {
                adapter = new ArtistsAdapter(mContext, artistBeans);
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        ArtistBean artistBean = artistBeans.get(position);
                        Intent intent = new Intent(mContext, ArtistsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("artistBean", artistBean);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_artists_sort, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}