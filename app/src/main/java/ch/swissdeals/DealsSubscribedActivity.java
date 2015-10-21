package ch.swissdeals;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DealsSubscribedActivity extends AppCompatActivity {

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

}
