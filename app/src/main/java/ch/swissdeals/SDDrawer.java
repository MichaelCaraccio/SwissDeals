package ch.swissdeals;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ch.swissdeals.database.controllers.DatabaseHelper;
import ch.swissdeals.database.models.ModelProviders;
import ch.swissdeals.drawer.NavDrawerItem;
import ch.swissdeals.drawer.NavDrawerListAdapter;

public class SDDrawer {
    private final ListView mDrawerList;

    private DrawerLayout drawer;
    private Context mContext;

    public SDDrawer(Activity hostActivity, Toolbar toolbar) {
        this.mContext = hostActivity.getApplicationContext();

        // load slide menu items
        String[] navMenuTitles = mContext.getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        TypedArray navMenuIcons = hostActivity.getResources().obtainTypedArray(R.array.nav_drawer_icons);

        this.mDrawerList = (ListView) hostActivity.findViewById(R.id.list_slidermenu);

        // Get user's deals
        DatabaseHelper db = new DatabaseHelper(mContext);
        final List<ModelProviders> listproviders = db.getAllProviders();

        // adding nav drawer items to array
        ArrayList<NavDrawerItem> navDrawerItems = new ArrayList<>();
        for (ModelProviders provider : listproviders)
            navDrawerItems.add(new NavDrawerItem(provider.getDisplayName(), provider.getFavicon_url(), true, 11, 1));


        // Recycle the typed array
        navMenuIcons.recycle();


        // setting the nav drawer list adapter
        // TODO : Override method
        NavDrawerListAdapter adapter = new NavDrawerListAdapter(mContext, navDrawerItems);
        mDrawerList.setAdapter(adapter);

        drawer = (DrawerLayout) hostActivity.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                hostActivity, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mDrawerList.setOnItemClickListener(listener);
    }

    public DrawerLayout getDrawer() {
        return drawer;
    }
}
