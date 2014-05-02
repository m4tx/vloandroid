package pl.krakow.vlo.ui.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import pl.krakow.vlo.R;

/**
 * Created by m4tx3 on 5/2/14.
 */
public class TimetableScreen implements Screen {
    @Override
    public int getNameResId() {
        return R.string.screen_timetable_title;
    }

    @Override
    public View getListItemView(LayoutInflater inflater, ViewGroup viewGroup) {
        return Screens.create1LineListItem(inflater, viewGroup, getNameResId(),
                R.drawable.ic_timetable);
    }

    @Override
    public void createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    }
}
