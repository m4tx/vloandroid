package pl.krakow.vlo.ui.screens;

import java.util.ArrayList;

import pl.krakow.vlo.R;

/**
 * Created by m4tx on 5/1/14.
 */
public abstract class Screens {
    private static ArrayList<Screen> screens = new ArrayList<>();

    static {
        screens.add(new Header(R.string.app_name));
        screens.add(new InternetConnectionScreen());
        screens.add(new InternetConnectionScreen());
        screens.add(new Header(R.string.app_name));
        screens.add(new InternetConnectionScreen());
        screens.add(new InternetConnectionScreen());
        screens.add(new Header(R.string.app_name));
        screens.add(new InternetConnectionScreen());
        screens.add(new InternetConnectionScreen());
    }

    public static ArrayList<Screen> getScreens() {
        return screens;
    }
}
