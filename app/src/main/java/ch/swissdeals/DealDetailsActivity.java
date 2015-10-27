package ch.swissdeals;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import ch.swissdeals.database.controllers.DatabaseHelper;
import ch.swissdeals.database.models.ModelDeals;

public class DealDetailsActivity extends AppCompatActivity {

    private int position;
    private DatabaseHelper helper;
    private ModelDeals deal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_details_fragment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        this.helper = new DatabaseHelper(getApplicationContext());

        Intent i = getIntent();
        position = i.getIntExtra(DealsSubscribedFragment.POSITION_MAIN_LIST, 0);
        deal = helper.getDeal(position);

        // Update action bar title
        getSupportActionBar().setTitle(helper.getProviderNameFromID(deal.getFk_provider_id()));


        ImageView imageFavicon = (ImageView) this.findViewById(R.id.detail_favicon);
        TextView textProviderName = (TextView) this.findViewById(R.id.detail_providerName);
        TextView textCurrentPrice = (TextView) this.findViewById(R.id.detail_currentPrice);
        TextView textOldPrice = (TextView) this.findViewById(R.id.detail_oldPrice);
        TextView textTitle = (TextView) this.findViewById(R.id.detail_title);
        ImageView imagePrimaryImage = (ImageView) this.findViewById(R.id.detail_primaryImage);
        TextView textLongDescription = (TextView) this.findViewById(R.id.detail_longDescription);

        //imageFavicon.setImageResource();
        textProviderName.setText(helper.getProviderNameFromID(deal.getFk_provider_id()));
        textCurrentPrice.setText(String.valueOf(deal.getPrice()));
        textOldPrice.setText(String.valueOf(deal.getOld_price()));
        textTitle.setText(deal.getTitle());
        //imagePrimaryImage.setImageResource();
        textLongDescription.setText(deal.getDescription());

    }
}