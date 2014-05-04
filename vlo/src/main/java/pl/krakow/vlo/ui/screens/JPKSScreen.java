package pl.krakow.vlo.ui.screens;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

import pl.krakow.vlo.R;
import pl.krakow.vlo.jpks.JPKSClient;

/**
 * Created by m4tx3 on 5/3/14.
 */
public class JPKSScreen extends Fragment implements Screen {
    @Override
    public int getNameResId() {
        return R.string.screen_jpks_title;
    }

    @Override
    public View getListItemView(LayoutInflater inflater, ViewGroup viewGroup) {
        return Screens.create2LineListItem(inflater, viewGroup, R.string.screen_jpks_title,
                R.string.screen_jpks_disconnected, R.drawable.ic_jpks);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... arg) {
                new JPKSClient("android");
                return null;
            }
        }.execute();

        return super.onCreateView(inflater, container, savedInstanceState);
    }


}
