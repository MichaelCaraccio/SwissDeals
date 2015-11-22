package ch.swissdeals.drawer;

/**
 * Created by michaelcaraccio on 12/10/15.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ch.swissdeals.R;

public class NavDrawerListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NavDrawerItem> navDrawerItems;

    public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems) {
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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

        // displaying count
        // check whether it set visible or not
        if (navDrawerItems.get(position).getCounterVisibility()) {
            navCounter.setText(Integer.toString(navDrawerItems.get(position).getCount()));
        } else {
            // hide the counter view
            navCounter.setVisibility(View.GONE);
        }

        return convertView;
    }

}