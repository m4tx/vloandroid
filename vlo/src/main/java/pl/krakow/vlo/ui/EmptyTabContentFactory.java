package pl.krakow.vlo.ui;

import android.content.Context;
import android.view.View;
import android.widget.TabHost.TabContentFactory;

/**
 * Created by m4tx3 on 5/5/14.
 */
public class EmptyTabContentFactory implements TabContentFactory {
    private Context context;

    public EmptyTabContentFactory(Context context) {
        this.context = context;
    }

    @Override
    public View createTabContent(String s) {
        View v = new View(context);
        v.setMinimumWidth(0);
        v.setMinimumHeight(0);
        return v;
    }
}
