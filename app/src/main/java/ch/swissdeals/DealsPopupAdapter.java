package ch.swissdeals;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ch.swissdeals.database.models.ModelProviders;

public class DealsPopupAdapter extends ArrayAdapter<ModelProviders> {
    private final Context context;
    private final List<ModelProviders> values;
    private ColorTheme colorTheme;
    private int mainColor;

    public enum ColorTheme {
        WHITE, BLUE
    }

    private static class ViewHolderPopUp {
        private TextView provider_name;
        private ImageView favicon;
        private ImageView download_icon;
    }


    public DealsPopupAdapter(Context context, List<ModelProviders> values) {
        super(context, R.layout.item_deal_popup, values);
        this.context = context;
        this.values = values;
        setColorTheme(ColorTheme.BLUE);
    }

    public DealsPopupAdapter(Context context, List<ModelProviders> values, ColorTheme colorTheme) {
        this(context, values);
        setColorTheme(colorTheme);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolderPopUp mViewHolder;

        if (convertView == null) {

            mViewHolder = new ViewHolderPopUp();

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.item_deal_popup, parent, false);
            mViewHolder.provider_name = (TextView) convertView.findViewById(R.id.popup_item_providerName);
            mViewHolder.favicon = (ImageView) convertView.findViewById(R.id.popup_item_image);

            // TODO : if provider subscribed -> cross icon, else -> download icon
            mViewHolder.download_icon = (ImageView) convertView.findViewById(R.id.popup_downloadOrDelete);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolderPopUp) convertView.getTag();
        }

        ModelProviders provider = values.get(position);

        // TODO : insert image placeholder
        Picasso.with(context).load(provider.getFavicon_url()).resize(40, 40).into(mViewHolder.favicon);


        if (provider.isUserSubscribed()) {
            mViewHolder.download_icon.setImageResource(R.mipmap.ic_remove);
        } else {
            mViewHolder.download_icon.setImageResource(R.mipmap.ic_download_white);
            mViewHolder.download_icon.setColorFilter(ContextCompat.getColor(context, this.mainColor));
        }
        mViewHolder.provider_name.setText(provider.getName());
        mViewHolder.provider_name.setTextColor(ContextCompat.getColor(context, this.mainColor));

        return convertView;
    }

    public void setColorTheme(ColorTheme colorTheme) {
        switch (colorTheme) {
            case WHITE:
                this.mainColor = R.color.colorWhite;
                break;

            case BLUE:
                this.mainColor = R.color.mainColorBlue;
                break;

        }
        notifyDataSetChanged();
    }
}



