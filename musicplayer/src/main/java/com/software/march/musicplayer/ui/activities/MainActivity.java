package com.software.march.musicplayer.ui.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.software.march.appcommonlibrary.BaseActivity;
import com.software.march.musicplayer.R;
import com.software.march.musicplayer.ui.fragments.MusicListFragment;
import com.software.march.musicplayer.ui.fragments.PlayListFragment;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private MusicListFragment musicListFragment;
    private PlayListFragment playListFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        int[][] states = new int[][]{new int[]{-android.R.attr.state_checked}, new int[]{android.R.attr.state_checked}};
        int[] colors = new int[]{Color.parseColor("#000000"), ContextCompat.getColor(mContext, R.color.colorPrimary)};
        ColorStateList csl = new ColorStateList(states, colors);
        navigationView.setItemTextColor(csl);
        navigationView.setItemIconTintList(csl);
        navigationView.setNavigationItemSelectedListener(this);

        navigateMusicList();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_music_list:
                navigateMusicList();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_play_list:
                navigatePlayList();
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_network_music_list:
                startActivity(new Intent(mContext, NetworkMusicListActivity.class));
                break;
            case R.id.nav_music_play:
                startActivity(new Intent(mContext, MusicPlayerActivity.class));
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_settings:
                break;
        }
        return false;
    }

    private void navigateMusicList() {
        if (musicListFragment == null)
            musicListFragment = MusicListFragment.newInstance();

        navigationView.getMenu().findItem(R.id.nav_music_list).setChecked(true);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, musicListFragment).commit();
    }

    private void navigatePlayList() {
        if (playListFragment == null)
            playListFragment = PlayListFragment.newInstance();

        navigationView.getMenu().findItem(R.id.nav_play_list).setChecked(true);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, playListFragment).commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }
}