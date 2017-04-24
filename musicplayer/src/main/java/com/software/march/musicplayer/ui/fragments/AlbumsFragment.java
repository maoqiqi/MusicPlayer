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
import com.software.march.musicplayer.bean.AlbumBean;
import com.software.march.musicplayer.ui.activities.AlbumsActivity;
import com.software.march.musicplayer.ui.adapters.AlbumsAdapter;
import com.software.march.musicplayer.utils.MediaLoader;

import java.util.ArrayList;

/**
 * @author Doc.March
 * @version V 1.0
 * @Description 专辑列表
 * @date 2017/4/1
 */
public class AlbumsFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private ArrayList<AlbumBean> albumBeans;
    private AlbumsAdapter adapter;

    /**
     * Use this factory method to create a new instance of this fragment using the provided parameters.
     *
     * @return A new instance of fragment AlbumsFragment.
     */
    public static AlbumsFragment newInstance() {
        AlbumsFragment fragment = new AlbumsFragment();
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
                adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        AlbumBean albumBean = albumBeans.get(position);
                        Intent intent = new Intent(mContext, AlbumsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("albumBean", albumBean);
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
        inflater.inflate(R.menu.menu_show_method, menu);
        inflater.inflate(R.menu.menu_albums_sort, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}