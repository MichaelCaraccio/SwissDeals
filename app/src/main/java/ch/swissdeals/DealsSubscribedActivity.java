package ch.swissdeals;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class DealsSubscribedActivity extends AppCompatActivity implements DealsSubscribedFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deals_subscribed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onFragmentInteraction(int position) {
        Intent i = new Intent(this, DealDetailsActivity.class);
        i.putExtra(DealsSubscribedFragment.POSITION_MAIN_LIST, position);
        startActivity(i);
    }

}
