package pl.krakow.vlo.ui.screens;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import pl.krakow.vlo.R;

/**
 * Created by m4tx on 5/1/14.
 */
public abstract class Screens {
    private static ArrayList<Screen> screens = new ArrayList<>();

    static {
        screens.add(new InternetConnectionScreen());
        screens.add(new Header(R.string.screen_section_lessons));
        screens.add(new TimetableScreen());
        screens.add(new SubsScreen());
        screens.add(new Header(R.string.screen_section_misc));
        screens.add(new JpksScreen());
    }

    public static ArrayList<Screen> getScreens() {
        return screens;
    }

    public static View create1LineListItem(LayoutInflater inflater, ViewGroup viewGroup,
                                           int textResId, int iconResId) {
        return create1LineListItem(inflater, viewGroup, R.layout.list_item_1, textResId,
                iconResId);
    }

    private static View create1LineListItem(LayoutInflater inflater, ViewGroup viewGroup,
                                            int layoutResId, int textResId, int iconResId) {
        View view = inflater.inflate(layoutResId, viewGroup, false);
        TextView tv = (TextView) view.findViewById(android.R.id.text1);
        tv.setText(textResId);
        ImageView iv = (ImageView) view.findViewById(android.R.id.icon);
        iv.setImageResource(iconResId);
        return view;
    }

    public static View create2LineListItem(LayoutInflater inflater, ViewGroup viewGroup,
                                           int text1ResId, int text2ResId, int iconResId) {
        View view = create1LineListItem(inflater, viewGroup, R.layout.list_item_2,
                text1ResId, iconResId);
        TextView tv = (TextView) view.findViewById(android.R.id.text2);
        tv.setText(text2ResId);
        return view;
    }
}
