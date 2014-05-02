package pl.krakow.vlo.ui.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by m4tx on 5/1/14.
 */
public interface Screen {
    int getNameResId();
    View getListItemView(LayoutInflater inflater, ViewGroup viewGroup);
    void createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
}
