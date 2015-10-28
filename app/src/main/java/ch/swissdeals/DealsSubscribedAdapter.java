package ch.swissdeals;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ch.swissdeals.database.controllers.DatabaseHelper;
import ch.swissdeals.database.models.ModelDeals;
import ch.swissdeals.database.models.ModelProviders;

public class DealsSubscribedAdapter extends ArrayAdapter<ModelDeals>{
    private final Context context;
    private final List<ModelDeals> values;
    private final DatabaseHelper helper;
    ImageView crossOut;
    private int bitmapWidth;
    private int bitmapHeight;
    private int crossOutThickness;

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

    public DealsSubscribedAdapter(Context context, List<ModelDeals> values) {
        super(context, R.layout.item_deal, values);
        this.context = context;
        this.values = values;
        this.helper = new DatabaseHelper(getContext());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        setBitmapHeight(80);
        setBitmapWidth(200);
        setCrossOutThickness(5);

        View rowView = inflater.inflate(R.layout.item_deal, parent, false);
        TextView dealTitle = (TextView) rowView.findViewById(R.id.item_title);
        TextView dealProviderName = (TextView) rowView.findViewById(R.id.item_providerName);
        TextView dealDescription = (TextView) rowView.findViewById(R.id.item_shortDescription);
        TextView dealCurrentPrice = (TextView) rowView.findViewById(R.id.item_currentPrice);
        TextView dealOldPrice = (TextView) rowView.findViewById(R.id.item_oldPrice);


        ImageView dealImage = (ImageView) rowView.findViewById(R.id.item_image);
        ImageView dealFavicon = (ImageView) rowView.findViewById(R.id.item_favicon);
        crossOut = (ImageView) rowView.findViewById(R.id.crossOut);


        ModelProviders provider = helper.getProvider(values.get(position).getFk_provider_id());

        // TODO : insert image placeholder
        Picasso.with(context).load(provider.getFavicon_url()).resize(40, 40).into(dealFavicon);

        // TODO : insert image placeholder
        Picasso.with(context)
                .load(values.get(position).getImage_url())
                .resize(300, 300)
                .centerCrop()
                .into(dealImage);

        dealTitle.setText(values.get(position).getTitle().toUpperCase());
        dealProviderName.setText(helper.getProviderNameFromID(values.get(position).getFk_provider_id()).toUpperCase());
        dealDescription.setText(values.get(position).getDescription().toUpperCase());
        dealCurrentPrice.setText(String.format("%.2f", values.get(position).getPrice()));
        dealOldPrice.setText(String.format("%.2f", values.get(position).getOld_price()));

        crossOutOldPrice(getBitmapWidth(), 25, 0, getBitmapHeight() - 25, Color.parseColor("#FF4640"));

        return rowView;
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

