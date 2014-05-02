package pl.krakow.vlo.ui.screens;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Interface used to create "Screens" - app modules accessible from the main Activity of the app.
 * <p>
 * <strong>Please note that this interface is generally only used to show the item on Screen list
 * - every clickable Screen list item should be subclass of
 * {@link android.support.v4.app.Fragment}.</strong>
 */
public interface Screen {
    /**
     * Returns resource ID that points to the name string of the screen. The name is mainly used
     * to set title for the {@link pl.krakow.vlo.ui.ScreenDetailActivity} on handsets.
     *
     * @return resource ID of the name string of the screen
     */
    int getNameResId();

    /**
     * Returns the view that is used to show as Screen List item.
     *
     * @param inflater  {@link android.view.LayoutInflater} that may be used to inflate the layout
     *                  of the view
     * @param viewGroup parent of the created view
     * @return the view that should be shown as Screen List item
     */
    View getListItemView(LayoutInflater inflater, ViewGroup viewGroup);
}
