package pl.krakow.vlo.ui.screens;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.krakow.vlo.R;

/**
 * Created by m4tx3 on 5/2/14.
 */
public class InternetConnectionScreen extends Fragment implements Screen {
    @Override
    public int getNameResId() {
        return R.string.screen_connection_title;
    }

    @Override
    public View getListItemView(LayoutInflater inflater, ViewGroup viewGroup) {
        return Screens.create2LineListItem(inflater, viewGroup, getNameResId(), getNameResId(),
                R.drawable.ic_internet_connection);
    }
}
