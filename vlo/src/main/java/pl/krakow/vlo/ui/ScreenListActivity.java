package pl.krakow.vlo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import pl.krakow.vlo.R;
import pl.krakow.vlo.ui.screens.Screen;
import pl.krakow.vlo.ui.screens.Screens;


/**
 * An activity representing a list of Screens. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ScreenDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * This activity also implements the required
 * {@link ScreenListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class ScreenListActivity extends FragmentActivity
        implements ScreenListFragment.Callbacks {

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_POS = "item_pos";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_list);
        setTitle(R.string.app_title);

        if (findViewById(R.id.screen_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((ScreenListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.screen_list))
                    .setActivateOnItemClick(true);
        }

        // TODO: If exposing deep links into your app, handle intents here.
    }

    /**
     * Callback method from {@link ScreenListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(int position) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Screen screen = Screens.getScreens().get(position);
            assert screen instanceof Fragment; // Every clickable Screen should be a Fragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.screen_detail_container, (Fragment) screen)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, ScreenDetailActivity.class);
            detailIntent.putExtra(ARG_ITEM_POS, position);
            startActivity(detailIntent);
        }
    }
}
