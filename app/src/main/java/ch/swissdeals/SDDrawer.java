package ch.swissdeals;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;

import ch.swissdeals.database.controllers.DatabaseHelper;
import ch.swissdeals.database.models.ModelProviders;
import ch.swissdeals.drawer.NavDrawerListAdapter;

public class SDDrawer {

    private DrawerLayout drawer;
    private DatabaseHelper db;
    private List<ModelProviders> listproviders;

    public SDDrawer(final MainActivity hostActivity, Toolbar toolbar) {
        Context mContext = hostActivity.getApplicationContext();

        // load slide menu items
        String[] navMenuTitles = mContext.getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        TypedArray navMenuIcons = hostActivity.getResources().obtainTypedArray(R.array.nav_drawer_icons);

        ListView mDrawerList = (ListView) hostActivity.findViewById(R.id.list_slidermenu);

        // Get user's deals
        db = new DatabaseHelper(mContext);
        listproviders = db.getAllProviders();

        // adding nav drawer items to array
       // ArrayList<NavDrawerItem> navDrawerItems = new ArrayList<>();
        //for (ModelProviders provider : listproviders)
        //navDrawerItems.add(listproviders);


        // Recycle the typed array
        navMenuIcons.recycle();


        // setting the nav drawer list adapter
        // TODO : Override method
        final NavDrawerListAdapter adapter = new NavDrawerListAdapter(mContext, listproviders);
        mDrawerList.setAdapter(adapter);

        drawer = (DrawerLayout) hostActivity.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                hostActivity, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close){

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                    hostActivity.refreshDealsList();
                }
        };



        drawer.setDrawerListener(toggle);
        toggle.syncState();


        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ModelProviders pro = listproviders.get(position);

                ImageView imageDownloadOrDelete = (ImageView) view.findViewById(R.id.nav_AddOrDelSub);  //replace with your ImageView id

                if (pro.isUserSubscribed()) {
                    pro.setUserSubscribed(false);
                    db.unsubscribeProvider(pro.getProvider_id());
                    imageDownloadOrDelete.setImageResource(R.mipmap.ic_download_white);
                } else {
                    pro.setUserSubscribed(true);
                    db.subscribeProvider(pro.getProvider_id());
                    imageDownloadOrDelete.setImageResource(R.mipmap.ic_remove);
                }

                adapter.notifyDataSetChanged();
            }
        });
    }

    public DrawerLayout getDrawer() {
        return drawer;
    }
}
