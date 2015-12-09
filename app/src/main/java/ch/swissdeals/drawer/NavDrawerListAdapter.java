package ch.swissdeals.drawer;

/**
 * Created by michaelcaraccio on 12/10/15.
 */

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
        /*
        if (convertView == null) {

            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.nav_drawer_list_item, null);
        }

        ImageView navFavicon = (ImageView) convertView.findViewById(R.id.nav_favicon);
        TextView navProviderName = (TextView) convertView.findViewById(R.id.nav_providerName);
        ImageView navAddOrDelSub = (ImageView) convertView.findViewById(R.id.nav_AddOrDelSub);
        TextView navCounter = (TextView) convertView.findViewById(R.id.nav_counter);

        Picasso.with(context).load(navDrawerItems.get(position).getIcon()).resize(40, 40).into(navFavicon);

        navProviderName.setText(navDrawerItems.get(position).getTitle());
    */

        ViewHolderPopUp mViewHolder;

        if(convertView == null) {

            mViewHolder = new ViewHolderPopUp();

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.nav_drawer_list_item, parent, false);
            mViewHolder.provider_name = (TextView) convertView.findViewById(R.id.nav_providerName);
            mViewHolder.favicon = (ImageView) convertView.findViewById(R.id.nav_favicon);
            mViewHolder.navCounter = (TextView) convertView.findViewById(R.id.nav_counter);

            // TODO : if provider subscribed -> cross icon, else -> download icon
            mViewHolder.download_icon = (ImageView) convertView.findViewById(R.id.nav_AddOrDelSub);
            convertView.setTag(mViewHolder);

        }else{
            mViewHolder = (ViewHolderPopUp) convertView.getTag();
        }

        ModelProviders provider = navDrawerItems.get(position);

        // TODO : insert image placeholder
        Picasso.with(context).load(provider.getFavicon_url()).resize(40, 40).into(mViewHolder.favicon);

        if(provider.isUserSubscribed()){
            mViewHolder.download_icon.setImageResource(R.mipmap.ic_remove);
        }else{
            mViewHolder.download_icon.setImageResource(R.mipmap.ic_download_white);
        }
        mViewHolder.provider_name.setText(provider.getDisplayName());


        mViewHolder.navCounter.setText(db.countDealWithProviderID(provider.getProvider_id()));


        return convertView;
    }

}