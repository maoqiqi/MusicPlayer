package com.software.march.musicplayer.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.software.march.appcommonlibrary.BaseActivity;
import com.software.march.musicplayer.R;
import com.software.march.musicplayer.ui.fragments.SongsFragment;

import java.io.File;

/**
 * @author Doc.March
 * @version V 1.0
 * @Description
 * @date 2017/4/1
 */
public class FoldersActivity extends BaseActivity {

    private String folderPath;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_folders;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        folderPath = getIntent().getExtras().getString("folderPath");
        String folderName = folderPath.substring(folderPath.lastIndexOf(File.separator) + 1);
        setTitle(folderName);

        Bundle bundle = new Bundle();
        bundle.putString("folderPath", folderPath);

        SongsFragment songsFragment = SongsFragment.newInstance();
        songsFragment.setArguments(bundle);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, songsFragment).commit();
    }
}