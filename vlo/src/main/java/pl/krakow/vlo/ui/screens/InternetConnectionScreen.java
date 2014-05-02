package pl.krakow.vlo.ui.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pl.krakow.vlo.R;

/**
 * Created by m4tx3 on 5/2/14.
 */
public class InternetConnectionScreen implements Screen {
    @Override
    public int getNameResId() {
        return R.string.screen_connection_title;
    }

    @Override
    public View getListItemView(LayoutInflater inflater, ViewGroup viewGroup) {
        View view = inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
        TextView tv = (TextView) view.findViewById(android.R.id.text1);
        tv.setText(getNameResId());
        return view;
    }

    @Override
    public void createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    }
}
