package ch.swissdeals;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ch.swissdeals.database.controllers.DatabaseHelper;
import ch.swissdeals.database.models.ModelDeals;
import ch.swissdeals.database.models.ModelProviders;

public class DealDetailsActivity extends AppCompatActivity {

    private DatabaseHelper helper;
    private ModelDeals deal;
    private int bitmapWidth;
    private int bitmapHeight;
    private int crossOutThickness;
    ImageView crossOut;

    public int getBitmapWidth() {
        return bitmapWidth;
    }

    public void setBitmapWidth(int bitmapWidth) {
        this.bitmapWidth = bitmapWidth;
    }

    public int getBitmapHeight() {
        return bitmapHeight;
    }

    public void setBitmapHeight(int bitmapHeight) {
        this.bitmapHeight = bitmapHeight;
    }

    public int getCrossOutThickness() {
        return crossOutThickness;
    }

    public void setCrossOutThickness(int crossOutThickness) {
        this.crossOutThickness = crossOutThickness;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_details_fragment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setBitmapHeight(100);
        setBitmapWidth(200);
        setCrossOutThickness(5);

        this.helper = new DatabaseHelper(getApplicationContext());

        Intent i = getIntent();

        int position = i.getIntExtra(DealsSubscribedFragment.POSITION_MAIN_LIST, 0);
        deal = helper.getDeal(position);

        // Update action bar title
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(helper.getProviderNameFromID(deal.getFk_provider_id()));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textProviderName = (TextView) this.findViewById(R.id.detail_providerName);
        TextView textCurrentPrice = (TextView) this.findViewById(R.id.detail_currentPrice);
        TextView textOldPrice = (TextView) this.findViewById(R.id.detail_oldPrice);
        TextView textTitle = (TextView) this.findViewById(R.id.detail_title);
        TextView textTitleDealDescription = (TextView) this.findViewById(R.id.detail_titleDealDescription);
        TextView textLongDescription = (TextView) this.findViewById(R.id.detail_longDescription);
        TextView textWebsiteRedirect = (TextView) this.findViewById(R.id.detail_website_redirect);

        textProviderName.setText(helper.getProviderNameFromID(deal.getFk_provider_id()).toUpperCase());
        textCurrentPrice.setText(String.format("%.2f", deal.getPrice()));
        textTitle.setText(deal.getTitle().toUpperCase());

        // Hide Description title and text if does not exist
        if(deal.getDescription()==null || deal.getDescription().isEmpty()){
            textTitleDealDescription.setVisibility(View.GONE);
            textLongDescription.setVisibility(View.GONE);
        }
        else{
            textTitleDealDescription.setText(R.string.DealDetailsTitleDealDescription);
            textLongDescription.setText(deal.getDescription());
        }

        // Redirect button
        textWebsiteRedirect.setText(getString(R.string.DealDetailWebsiteRedirect, helper.getProviderNameFromID(deal.getFk_provider_id())));

        textWebsiteRedirect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(deal.getLink()));
                startActivity(browserIntent);
            }
        });

        if (deal.getOld_price() != -1.0) {
            textOldPrice.setText(String.format("%.2f", deal.getOld_price()));
            crossOut = (ImageView) this.findViewById(R.id.detail_crossOut);
            crossOutOldPrice(getBitmapWidth(), 40, 0, getBitmapHeight() - 40, Color.parseColor("#FF4640"));
        }
        if (deal.getPrice() == -1.0)
            textCurrentPrice.setText("");
    }

    // Trick to get imageView height and width. onCreate method does not provide those properties
    // because they are not instanciated yet.
    public void onWindowFocusChanged(boolean hasFocus) {

        super.onWindowFocusChanged(hasFocus);

        ImageView imagePrimaryImage = (ImageView) this.findViewById(R.id.detail_primaryImage);
        ImageView imageFavicon = (ImageView) this.findViewById(R.id.detail_favicon);

        // Image view -> Fullscreen
        imagePrimaryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FullscreenActivity.class);
                intent.putExtra("URL", deal.getImage_url());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                getApplicationContext().startActivity(intent);
            }
        });

        ModelProviders provider = helper.getProvider(deal.getFk_provider_id());

        Picasso.with(getApplicationContext()).load(provider.getFavicon_url()).resize(imageFavicon.getWidth(), imageFavicon.getHeight()).into(imageFavicon);

        Picasso.with(getApplicationContext())
                .load(deal.getImage_url())
                .resize(imagePrimaryImage.getWidth(), imagePrimaryImage.getHeight())
                .centerCrop()
                .into(imagePrimaryImage);
    }

    private void crossOutOldPrice(float x, float y, float xend, float yend, int color) {

        Bitmap bmp = Bitmap.createBitmap(getBitmapWidth(), getBitmapHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        crossOut.draw(c);

        Paint p = new Paint();
        p.setColor(color);
        p.setStrokeWidth((float) 5);
        c.drawLine(x, y, xend, yend, p);
        crossOut.setImageBitmap(bmp);
    }
}