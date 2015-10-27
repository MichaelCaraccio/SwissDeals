package ch.swissdeals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ch.swissdeals.database.controllers.DatabaseHelper;
import ch.swissdeals.database.models.ModelDeals;

public class DealsSubscribedAdapter extends ArrayAdapter<ModelDeals> {
    private final Context context;
    private final List<ModelDeals> values;
    private final DatabaseHelper helper;

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

        View rowView = inflater.inflate(R.layout.item_deal, parent, false);
        TextView dealTitle = (TextView) rowView.findViewById(R.id.item_title);
        TextView dealProviderName = (TextView) rowView.findViewById(R.id.item_providerName);
        TextView dealDescription = (TextView) rowView.findViewById(R.id.item_shortDescription);
        TextView dealCurrentPrice = (TextView) rowView.findViewById(R.id.item_currentPrice);
        TextView dealOldPrice = (TextView) rowView.findViewById(R.id.item_oldPrice);


        ImageView dealImage = (ImageView) rowView.findViewById(R.id.item_image);

        dealTitle.setText(values.get(position).getTitle());
        dealProviderName.setText(helper.getProviderNameFromID(values.get(position).getFk_provider_id()));
        dealDescription.setText(values.get(position).getDescription());
        dealCurrentPrice.setText(String.valueOf(values.get(position).getPrice()));
        dealOldPrice.setText(String.valueOf(values.get(position).getOld_price()));

        return rowView;
    }
}