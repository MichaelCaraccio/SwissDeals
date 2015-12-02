package ch.swissdeals;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DealsSubscribedFragment.OnFragmentInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    //private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;
    private TextView menuTitle;
    private SDDrawer drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.drawer = new SDDrawer(this, toolbar);
        drawer.setOnItemClickListener(new SlideMenuClickListener());

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }

        try {
            ProviderManager providerManager = ProviderManager.getInstance();
            providerManager.load(getApplicationContext());
            providerManager.subscribe("qoqa.ch");
            providerManager.subscribe("digitec.ch");
            providerManager.subscribe("topdeal.ch");
            providerManager.subscribe("qooking.ch");
            providerManager.subscribe("qsport.ch");
            providerManager.subscribe("qsport.ch");
        } catch (Exception e) {
            e.printStackTrace();
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();

                DialogFragment newFragment = new PopUpProviders();
                newFragment.show(getSupportFragmentManager(), "popup");

            }
        });


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        DealsSubscribedFragment dealsSubscribedFragment = new DealsSubscribedFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, dealsSubscribedFragment).commit();
    }

    /**
     * @param position
     */
    @Override
    public void onFragmentInteraction(int position) {
        Intent dealFragment = new Intent(getApplicationContext(), DealDetailsActivity.class);
        dealFragment.putExtra(DealsSubscribedFragment.POSITION_MAIN_LIST, position);

        startActivity(dealFragment);
    }

    @Override
    public void onBackPressed() {
        if (drawer.getDrawer().isDrawerOpen(GravityCompat.START)) {
            drawer.getDrawer().closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawer.getDrawer().closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        //boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                //fragment = new HomeFragment();
                break;
            case 1:
                //fragment = new FindPeopleFragment();
                break;
            case 2:
                //fragment = new PhotosFragment();
                break;
            case 3:
                //fragment = new CommunityFragment();
                break;
            case 4:
                //fragment = new PagesFragment();
                break;
            case 5:
                //fragment = new WhatsHotFragment();
                break;

            default:
                break;
        }

        //TODO Controler le click du drawer
        /*if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container,fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            //mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating
            DealsSubscribedFragment subscribedFragment = new DealsSubscribedFragment();

            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, subscribedFragment);
            //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
            Log.e("MainActivity", "Error in creating lol fragment");
        }*/
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        //mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        //mDrawerToggle.onConfigurationChanged(newConfig);
    }

}

