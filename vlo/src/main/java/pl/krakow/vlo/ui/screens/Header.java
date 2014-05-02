package pl.krakow.vlo.ui.screens;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pl.krakow.vlo.R;

/**
 * Implementation of {@link Screen} interface that is used to create section headers.
 */
public class Header implements Screen {
    private int textResId;

    public Header(int textResId) {
        this.textResId = textResId;
    }

    @Override
    public int getNameResId() {
        return textResId;
    }

    @Override
    public View getListItemView(LayoutInflater inflater, ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.list_header, viewGroup, false);
        TextView tv = (TextView) view.findViewById(android.R.id.content);
        tv.setText(textResId);
        return view;
    }
}
