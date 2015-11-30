package ch.swissdeals;

import android.content.Context;
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

    private static class ViewHolderPopUp {
        private TextView provider_name;
        private ImageView favicon;
        private ImageView download_icon;
    }


    public DealsPopupAdapter(Context context, List<ModelProviders> values) {
        super(context, R.layout.item_deal_popup, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolderPopUp mViewHolder;

        if(convertView == null) {

            mViewHolder = new ViewHolderPopUp();

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.item_deal_popup, parent, false);
            mViewHolder.provider_name = (TextView) convertView.findViewById(R.id.popup_item_providerName);
            mViewHolder.favicon = (ImageView) convertView.findViewById(R.id.popup_item_image);

            // TODO : if provider subscribed -> cross icon, else -> download icon
            mViewHolder.download_icon = (ImageView) convertView.findViewById(R.id.popup_crossOut);
            convertView.setTag(mViewHolder);

        }else{
            mViewHolder = (ViewHolderPopUp) convertView.getTag();

        }

        ModelProviders provider = values.get(position);

        // TODO : insert image placeholder
        Picasso.with(context).load(provider.getFavicon_url()).resize(40, 40).into(mViewHolder.favicon);

        mViewHolder.provider_name.setText(provider.getDisplayName());

        int id = this.context.getResources().getIdentifier("@drawable/ic_remove", null, null);

        mViewHolder.download_icon.setImageResource(id);
        return convertView;
    }
}



