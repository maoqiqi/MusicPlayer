package com.software.march.musicplayer.ui.fragments;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import com.software.march.appcommonlibrary.BaseFragment;
import com.software.march.musicplayer.R;
import com.software.march.musicplayer.ui.activities.MainActivity;

/**
 * @author Doc.March
 * @version V 1.0
 * @Description
 * @date 2017/4/1
 */
public class MusicPlayFragment extends BaseFragment {

    /**
     * Use this factory method to create a new instance of this fragment using the provided parameters.
     *
     * @return A new instance of fragment MusicPlayFragment.
     */
    public static MusicPlayFragment newInstance() {
        MusicPlayFragment fragment = new MusicPlayFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_music_play;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("正在播放");
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            activity.setSupportActionBar(toolbar);

            DrawerLayout drawerLayout = activity.getDrawerLayout();
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(),
                    drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
            toggle.syncState();
            drawerLayout.addDrawerListener(toggle);
        }
    }
}