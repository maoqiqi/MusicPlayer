package com.software.march.musicplayer.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.software.march.appcommonlibrary.BaseActivity;
import com.software.march.musicplayer.R;
import com.software.march.musicplayer.bean.ArtistBean;
import com.software.march.musicplayer.ui.fragments.SongsFragment;

public class ArtistsActivity extends BaseActivity {

    private ArtistBean artistBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_artists;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        artistBean = (ArtistBean) getIntent().getExtras().getSerializable("artistBean");

        String artist = artistBean.getArtist();
        if ("<unknown>".equals(artist)) {
            artist = "未知艺人";
        }
        setTitle(artist);

        Bundle bundle = new Bundle();
        bundle.putLong("artistId", artistBean.get_id());

        SongsFragment songsFragment = SongsFragment.newInstance();
        songsFragment.setArguments(bundle);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, songsFragment).commit();
    }
}