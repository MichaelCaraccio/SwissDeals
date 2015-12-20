package ch.swissdeals;

import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import ch.swissdeals.service.DealDownloaderService;

public class MainActivity extends AppCompatActivity implements DealsSubscribedFragment.OnFragmentInteractionListener, ProvidersListFragment.OnFragmentInteractionListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    // used to store app title
    private CharSequence mTitle;
    private SDDrawer drawer;
    private BroadcastReceiver newDealsReceiver;
    private DealsSubscribedFragment dealsSubscribedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.drawer = new SDDrawer(this, toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new PopUpProviders(MainActivity.this);
                newFragment.show(getSupportFragmentManager(), "popup");
            }
        });

        dealsSubscribedFragment = new DealsSubscribedFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, dealsSubscribedFragment);
        ft.addToBackStack(null);
        ft.commit();

        /*
        listen to DealDownloaderService and refresh the deal list
        when the service has inserted new deals in database
         */
        IntentFilter filter = new IntentFilter();
        filter.addAction(DealDownloaderService.INTENT_NEW_DEALS_ADDED);

        newDealsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                refreshDealsList();
            }
        };
        registerReceiver(newDealsReceiver, filter);
    }

    @Override
    public void onFragmentInteraction(int position) {
        Intent dealDetailsActivity = new Intent(getApplicationContext(), DealDetailsActivity.class);
        dealDetailsActivity.putExtra(DealsSubscribedFragment.POSITION_MAIN_LIST, position);

        startActivity(dealDetailsActivity);
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
    public void setTitle(CharSequence title) {
        mTitle = title;
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(newDealsReceiver);
        super.onDestroy();
    }

    public void refreshDealsList() {
        dealsSubscribedFragment.updateAdapter();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //nothing
    }
}

