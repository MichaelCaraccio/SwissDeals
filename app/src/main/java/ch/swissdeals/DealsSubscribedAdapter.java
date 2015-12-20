package ch.swissdeals;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
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

public class DealsSubscribedAdapter extends ArrayAdapter<ModelDeals> {
    private final Context context;
    private final List<ModelDeals> values;
    private final DatabaseHelper helper;

    private static class ViewHolder {
        private TextView dealTitle;
        private TextView dealProviderName;
        private TextView dealDescription;
        private TextView dealCurrentPrice;
        private TextView dealOldPrice;
        private ImageView dealImage;
        private ImageView dealFavicon;
        private ImageView crossOut;
    }

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
        ViewHolder viewHolder;
        ModelDeals deal = values.get(position);
        ModelProviders provider = helper.getProvider(deal.getFk_provider_id());

        if (convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            setBitmapHeight(80);
            setBitmapWidth(200);
            setCrossOutThickness(5);

            convertView = inflater.inflate(R.layout.item_deal, parent, false);
            viewHolder.dealTitle = (TextView) convertView.findViewById(R.id.item_title);
            viewHolder.dealProviderName = (TextView) convertView.findViewById(R.id.item_providerName);
            viewHolder.dealDescription = (TextView) convertView.findViewById(R.id.item_shortDescription);
            viewHolder.dealCurrentPrice = (TextView) convertView.findViewById(R.id.item_currentPrice);
            viewHolder.dealOldPrice = (TextView) convertView.findViewById(R.id.item_oldPrice);
            viewHolder.dealImage = (ImageView) convertView.findViewById(R.id.item_image);
            viewHolder.dealFavicon = (ImageView) convertView.findViewById(R.id.item_favicon);
            viewHolder.crossOut = (ImageView) convertView.findViewById(R.id.crossOut);

            convertView.setTag(viewHolder);
        }

        viewHolder = (ViewHolder) convertView.getTag();

        // TODO : insert null with image placeholder
        Picasso.with(context).load(provider != null ? provider.getFavicon_url() : null).resize(40, 40).into(viewHolder.dealFavicon);

        // TODO : insert image placeholder instead of null
        Picasso.with(context)
                .load(deal.getImage_url())
                .resize(300, 300)
                .centerCrop()
                .into(viewHolder.dealImage);

        viewHolder.dealTitle.setText(deal.getTitle().toUpperCase());
        viewHolder.dealProviderName.setText(helper.getProviderNameFromID(deal.getFk_provider_id()).toUpperCase());
        viewHolder.dealCurrentPrice.setText(String.format("%.2f", deal.getPrice()));

        // If price not equals to -1 draw bitmap and display price
        if (deal.getOld_price() != -1.0) {
            crossOutOldPrice(viewHolder, getBitmapWidth(), 25, 0, getBitmapHeight() - 25, ContextCompat.getColor(context, R.color.colorCrossout));
            viewHolder.dealOldPrice.setText(String.format("%.2f", deal.getOld_price()));
        } else {
            drawBlankBitmap(viewHolder);
            viewHolder.dealOldPrice.setText("");
        }

        if (deal.getDescription() != null)
            viewHolder.dealDescription.setText(deal.getDescription().toUpperCase());

        if (deal.getPrice() == -1.0)
            viewHolder.dealCurrentPrice.setText("");

        return convertView;
    }


    private void drawBlankBitmap(ViewHolder mViewHolder) {
        Bitmap bmp = Bitmap.createBitmap(getBitmapWidth(), getBitmapHeight(), Bitmap.Config.ARGB_8888);
        mViewHolder.crossOut.setImageBitmap(bmp);
    }

    private void crossOutOldPrice(ViewHolder mViewHolder, float x, float y, float xend, float yend, int color) {
        Bitmap bmp = Bitmap.createBitmap(getBitmapWidth(), getBitmapHeight(), Bitmap.Config.ARGB_8888);
        mViewHolder.crossOut.setImageBitmap(bmp);
        Canvas c = new Canvas(bmp);

        Paint p = new Paint();
        p.setColor(color);
        p.setStrokeWidth((float) 5);
        c.drawLine(x, y, xend, yend, p);
        mViewHolder.crossOut.setImageBitmap(bmp);
    }
}