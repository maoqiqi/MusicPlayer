package com.software.march.musicplayer.ui.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;
import com.software.march.appcommonlibrary.BaseFragment;
import com.software.march.musicplayer.R;
import com.software.march.musicplayer.api.ApiClass;
import com.software.march.musicplayer.bean.AlbumBean;
import com.software.march.musicplayer.bean.BillList;
import com.software.march.musicplayer.ui.adapters.AlbumsAdapter;
import com.software.march.musicplayer.utils.HttpUtils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Doc.March
 * @version V 1.0
 * @Description 推荐列表
 * @date 2017/4/1
 */
public class RecommandFragment extends BaseFragment {

    private final String TAG = getClass().getSimpleName();

    private RecyclerView recyclerView;
    private ArrayList<AlbumBean> albumBeans;
    private AlbumsAdapter adapter;

    private ApiClass apiClass;

    /**
     * Use this factory method to create a new instance of this fragment using the provided parameters.
     *
     * @return A new instance of fragment RecommandFragment.
     */
    public static RecommandFragment newInstance() {
        RecommandFragment fragment = new RecommandFragment();
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
        new LoadRecommandSongList().execute();
    }

    private class LoadRecommandSongList extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                return HttpUtils.sendGetRequest(ApiClass.BASE_URL + "method=baidu.ting.billboard.billList&type=1&size=10&offset=0");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                // adapter = new ArtistsAdapter(mContext, artistBeans);
                BillList billList = new Gson().fromJson(s, BillList.class);
                Log.i(TAG, billList.toString());
                recyclerView.setAdapter(adapter);
            }
        }
    }
}