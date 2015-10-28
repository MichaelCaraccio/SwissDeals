package ch.swissdeals;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ch.swissdeals.database.controllers.DatabaseHelper;
import ch.swissdeals.database.models.ModelDeals;
import ch.swissdeals.database.models.ModelProviders;

public class DealDetailsActivity extends AppCompatActivity {

    private int position;
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

        setBitmapHeight(80);
        setBitmapWidth(200);
        setCrossOutThickness(5);

        this.helper = new DatabaseHelper(getApplicationContext());

        Intent i = getIntent();
        position = i.getIntExtra(DealsSubscribedFragment.POSITION_MAIN_LIST, 0);
        deal = helper.getDeal(position);

        // Update action bar title
        getSupportActionBar().setTitle(helper.getProviderNameFromID(deal.getFk_provider_id()));


        TextView textProviderName = (TextView) this.findViewById(R.id.detail_providerName);
        TextView textCurrentPrice = (TextView) this.findViewById(R.id.detail_currentPrice);
        TextView textOldPrice = (TextView) this.findViewById(R.id.detail_oldPrice);
        TextView textTitle = (TextView) this.findViewById(R.id.detail_title);
        TextView textLongDescription = (TextView) this.findViewById(R.id.detail_longDescription);




        textProviderName.setText(helper.getProviderNameFromID(deal.getFk_provider_id()).toUpperCase());
        textCurrentPrice.setText(String.format("%.2f", deal.getPrice()));
        textOldPrice.setText(String.format("%.2f", deal.getOld_price()));
        textTitle.setText(deal.getTitle().toUpperCase());
        textLongDescription.setText(deal.getDescription());

    }

    // Trick to get imageView height and width. onCreate method does not provide those properties
    // because they are not instanciated yet.
    public void onWindowFocusChanged(boolean hasFocus) {

        super.onWindowFocusChanged(hasFocus);

        ImageView imagePrimaryImage = (ImageView) this.findViewById(R.id.detail_primaryImage);
        ImageView imageFavicon = (ImageView) this.findViewById(R.id.detail_favicon);

        crossOut = (ImageView) this.findViewById(R.id.detail_crossOut);
        crossOutOldPrice(getBitmapWidth(), 30, 0, getBitmapHeight() - 30, Color.parseColor("#FF4640"));


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