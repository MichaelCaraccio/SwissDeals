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

        ViewHolder mViewHolder;
        ModelDeals deal = values.get(position);
        ModelProviders provider = helper.getProvider(deal.getFk_provider_id());

        if(convertView == null){
            mViewHolder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            setBitmapHeight(80);
            setBitmapWidth(200);
            setCrossOutThickness(5);

            convertView = inflater.inflate(R.layout.item_deal, parent, false);
            mViewHolder.dealTitle = (TextView) convertView.findViewById(R.id.item_title);
            mViewHolder.dealProviderName = (TextView) convertView.findViewById(R.id.item_providerName);
            mViewHolder.dealDescription = (TextView) convertView.findViewById(R.id.item_shortDescription);
            mViewHolder.dealCurrentPrice = (TextView) convertView.findViewById(R.id.item_currentPrice);
            mViewHolder.dealOldPrice = (TextView) convertView.findViewById(R.id.item_oldPrice);
            mViewHolder.dealImage = (ImageView) convertView.findViewById(R.id.item_image);
            mViewHolder.dealFavicon = (ImageView) convertView.findViewById(R.id.item_favicon);
            mViewHolder.crossOut = (ImageView) convertView.findViewById(R.id.crossOut);

            if (!(Float.toString(deal.getOld_price()).equals("-1.0"))){
                crossOutOldPrice(mViewHolder,getBitmapWidth(), 25, 0, getBitmapHeight() - 25, Color.parseColor("#FF4640"));
            }

            convertView.setTag(mViewHolder);

        }else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        // TODO : insert null with image placeholder
        Picasso.with(context).load(provider != null ? provider.getFavicon_url() : null).resize(40, 40).into(mViewHolder.dealFavicon);

        // TODO : insert image placeholder instead of null
        Picasso.with(context)
                .load(deal.getImage_url())
                .resize(300, 300)
                .centerCrop()
                .into(mViewHolder.dealImage);

        mViewHolder.dealTitle.setText(deal.getTitle().toUpperCase());
        mViewHolder.dealProviderName.setText(helper.getProviderNameFromID(deal.getFk_provider_id()).toUpperCase());
        mViewHolder.dealCurrentPrice.setText(String.format("%.2f", deal.getPrice()));

        if (!(Float.toString(deal.getOld_price()).equals("-1.0")))
            mViewHolder.dealOldPrice.setText(String.format("%.2f", deal.getOld_price()));

        if (deal.getDescription() != null) {
            mViewHolder.dealDescription.setText(deal.getDescription().toUpperCase());
        }

        return convertView;
    }

    private void crossOutOldPrice(ViewHolder mViewHolder, float x, float y, float xend, float yend, int color) {

        Bitmap bmp = Bitmap.createBitmap(getBitmapWidth(), getBitmapHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        mViewHolder.crossOut.draw(c);

        Paint p = new Paint();
        p.setColor(color);
        p.setStrokeWidth((float) 5);
        c.drawLine(x, y, xend, yend, p);
        mViewHolder.crossOut.setImageBitmap(bmp);
    }

}

