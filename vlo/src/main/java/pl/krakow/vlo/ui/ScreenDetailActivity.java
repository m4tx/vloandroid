package pl.krakow.vlo.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;

import pl.krakow.vlo.R;
import pl.krakow.vlo.ui.screens.Screen;
import pl.krakow.vlo.ui.screens.Screens;

/**
 * An activity representing a single Screen detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ScreenListActivity}.
 * <p/>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a currently chosen Screen.
 */
public class ScreenDetailActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_detail);

        // Show the Up button in the action bar.
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Screen screen = getScreen();
            assert screen instanceof Fragment; // Every clickable Screen should be a Fragment
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.screen_detail_container, (Fragment) screen)
                    .commit();
        }
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        setTitle(getScreen().getNameResId());
        return super.onCreateView(parent, name, context, attrs);
    }

    private Screen getScreen() {
        return Screens.getScreens().get(getIntent().getIntExtra(ScreenListActivity
                .ARG_ITEM_POS, -1));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
