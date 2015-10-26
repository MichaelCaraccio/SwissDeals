package ch.swissdeals;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class DealDetailsActivity extends AppCompatActivity {

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_details_fragment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        position = i.getIntExtra(DealsSubscribedFragment.POSITION_MAIN_LIST, 0);
        onItemListSelected(position);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void onItemListSelected(int pos) {

        Log.d("prout", String.valueOf(pos));

        //DealDetailsFragment gridFragment = new DealDetailsFragment();
        DealDetailsFragment detailFragment  = (DealDetailsFragment) getFragmentManager().findFragmentById(R.id.fragmentDealDetails);

        detailFragment.updatePosition(pos);
    }

}
