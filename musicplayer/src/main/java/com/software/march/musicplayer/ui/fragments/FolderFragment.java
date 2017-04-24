package com.software.march.musicplayer.ui.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.software.march.appcommonlibrary.BaseFragment;
import com.software.march.appcommonlibrary.RecyclerViewAdapter;
import com.software.march.musicplayer.R;
import com.software.march.musicplayer.ui.activities.FoldersActivity;
import com.software.march.musicplayer.ui.adapters.FoldersAdapter;
import com.software.march.musicplayer.utils.MediaLoader;

import java.util.ArrayList;

/**
 * @author Doc.March
 * @version V 1.0
 * @Description
 * @date 2017/4/1
 */
public class FolderFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private ArrayList<String> folderList;
    private int[] counts;
    private FoldersAdapter adapter;

    /**
     * Use this factory method to create a new instance of this fragment using the provided parameters.
     *
     * @return A new instance of fragment FolderFragment.
     */
    public static FolderFragment newInstance() {
        FolderFragment fragment = new FolderFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_folder;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        folderList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void loadData() {
        super.loadData();
        new LoadFolders().execute();
    }

    private class LoadFolders extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            folderList.addAll(MediaLoader.getFolders(mContext));
            counts = new int[folderList.size()];
            for (int i = 0; i < folderList.size(); i++) {
                counts[i] = MediaLoader.getSongs(mContext, 0, 0, folderList.get(i)).size();
            }
            return "SUCCESS";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("SUCCESS")) {
                adapter = new FoldersAdapter(mContext, folderList, counts);
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        String folderPath = folderList.get(position);
                        Intent intent = new Intent(mContext, FoldersActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("folderPath", folderPath);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
        }
    }
}