package com.software.march.musicplayer.ui.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;

import com.software.march.appcommonlibrary.BaseActivity;
import com.software.march.musicplayer.R;
import com.software.march.musicplayer.bean.AlbumBean;
import com.software.march.musicplayer.ui.fragments.SongsFragment;

public class AlbumsActivity extends BaseActivity {

    private AlbumBean albumBean;

    private ImageView imageView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_albums;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        albumBean = (AlbumBean) getIntent().getExtras().getSerializable("albumBean");

        String album = albumBean.getAlbum();
        if ("<unknown>".equals(album)) album = "未知专辑";
        setTitle(album);

        imageView = (ImageView) findViewById(R.id.image_view);
        if (albumBean.getAlbumArt() != null &&
                !albumBean.getAlbumArt().equals("") &&
                !albumBean.getAlbumArt().equals("null")) {
            imageView.setImageURI(Uri.parse(albumBean.getAlbumArt()));
        } else {
            imageView.setImageResource(R.drawable.ic_empty_audio);
        }

        Bundle bundle = new Bundle();
        bundle.putLong("albumId", albumBean.get_id());

        SongsFragment songsFragment = SongsFragment.newInstance();
        songsFragment.setArguments(bundle);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, songsFragment).commit();
    }
}