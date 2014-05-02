package pl.krakow.vlo.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pl.krakow.vlo.R;
import pl.krakow.vlo.ui.dummy.DummyContent;
import pl.krakow.vlo.ui.screens.Screen;
import pl.krakow.vlo.ui.screens.Screens;

/**
 * A fragment representing a single Screen detail screen.
 * This fragment is either contained in a {@link ScreenListActivity}
 * in two-pane mode (on tablets) or a {@link ScreenDetailActivity}
 * on handsets.
 */
public class ScreenDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_POS = "item_pos";

    /**
     * The screen that this fragment is currently presenting.
     */
    private Screen currentScreen;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ScreenDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_POS)) {
            currentScreen = Screens.getScreens().get(getArguments().getInt(ARG_ITEM_POS));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (currentScreen != null) {
            currentScreen.createView(inflater, container, savedInstanceState);
        }

        return null;
    }
}
