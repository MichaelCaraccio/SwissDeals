package ch.swissdeals.drawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ch.swissdeals.R;
import ch.swissdeals.database.controllers.DatabaseHelper;
import ch.swissdeals.database.models.ModelProviders;

public class NavDrawerListAdapter extends ArrayAdapter<ModelProviders> {
    private final Context context;
    private final List<ModelProviders> navDrawerItems;
    private DatabaseHelper db;

    private static class ViewHolderPopUp {
        private TextView provider_name;
        private ImageView favicon;
        private ImageView download_icon;
        private TextView navCounter;
    }

    public NavDrawerListAdapter(Context context, List<ModelProviders> navDrawerItems) {
        super(context, R.layout.nav_drawer_list_item, navDrawerItems);
        this.navDrawerItems = navDrawerItems;
        this.context = context;
        db = new DatabaseHelper(context);
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public ModelProviders getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderPopUp viewHolder;

        if (convertView != null) {
            viewHolder = (ViewHolderPopUp) convertView.getTag();
        } else {
            viewHolder = new ViewHolderPopUp();

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.nav_drawer_list_item, parent, false);
            viewHolder.provider_name = (TextView) convertView.findViewById(R.id.nav_providerName);
            viewHolder.favicon = (ImageView) convertView.findViewById(R.id.nav_favicon);
            viewHolder.navCounter = (TextView) convertView.findViewById(R.id.nav_counter);

            viewHolder.download_icon = (ImageView) convertView.findViewById(R.id.nav_AddOrDelSub);
            convertView.setTag(viewHolder);
        }

        ModelProviders provider = navDrawerItems.get(position);

        // TODO : insert image placeholder
        Picasso.with(context).load(provider.getFavicon_url()).resize(40, 40).into(viewHolder.favicon);

        if (provider.isUserSubscribed()) {
            viewHolder.download_icon.setImageResource(R.mipmap.ic_remove);
        } else {
            viewHolder.download_icon.setImageResource(R.mipmap.ic_download_white);
        }
        viewHolder.provider_name.setText(provider.getDisplayName());

        viewHolder.navCounter.setText(db.countDealWithProviderID(provider.getProvider_id()));

        return convertView;
    }
}