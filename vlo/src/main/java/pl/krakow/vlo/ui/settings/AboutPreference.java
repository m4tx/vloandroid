package pl.krakow.vlo.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.preference.Preference;
import android.util.AttributeSet;

/**
 * Created by m4tx3 on 5/3/14.
 */
public class AboutPreference extends Preference {
    public AboutPreference(Context context) {
        super(context);
    }

    public AboutPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AboutPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onClick() {
        Intent intent = new Intent(getContext(), AboutActivity.class);
        getContext().startActivity(intent);
    }
}
