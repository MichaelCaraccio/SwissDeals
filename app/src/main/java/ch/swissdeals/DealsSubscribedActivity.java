package ch.swissdeals;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DealsSubscribedActivity extends AppCompatActivity implements DealsSubscribedFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // check if dual pane mode is active
        // if yes, finish this activity
        /*if (getResources().getBoolean(R.bool.twoPaneMode)) {
            finish();
            return;
        }*/
        setContentView(R.layout.activity_deals_subscribed);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //String url = extras.getString(EXTRA_URL);
            DealsSubscribedFragment detailFragment = (DealsSubscribedFragment) getFragmentManager()
                    .findFragmentById(R.id.dealssubscribedfragment);
            //detailFragment.setText(url);
        }

    }

    @Override
    public void onFragmentInteraction(int position) {

        Intent i = new Intent(this, DealDetailsActivity.class);
        i.putExtra(DealsSubscribedFragment.POSITION_MAIN_LIST, position);
        startActivity(i);
    }

}
