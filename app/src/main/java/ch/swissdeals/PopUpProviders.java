package ch.swissdeals;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;

import ch.swissdeals.database.controllers.DatabaseHelper;
import ch.swissdeals.database.models.ModelProviders;


public class PopUpProviders extends DialogFragment {

    private DatabaseHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Set title for this dialog
        getDialog().setTitle("Edit Providers");

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_pop_up_providers, container, false);

        Context ctx = getActivity().getApplicationContext();

        // Get user's deals
        db = new DatabaseHelper(ctx);
        final List<ModelProviders> listproviders = db.getAllProviders();

        // Set the adapter
        ListView mListView = (ListView) v.findViewById(R.id.popup_content_deal_list);

        // Set the list in Adapter
        final DealsPopupAdapter mAdapter = new DealsPopupAdapter(ctx, listproviders);

        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ModelProviders pro = listproviders.get(position);

                ImageView imageDownloadOrDelete=(ImageView) view.findViewById(R.id.popup_downloadOrDelete);  //replace with your ImageView id

                if(pro.isUserSubscribed()) {
                    pro.setUserSubscribed(false);
                    db.unsubscribeProvider(pro.getProvider_id());
                    imageDownloadOrDelete.setImageResource(R.mipmap.ic_download_white);
                } else {
                    pro.setUserSubscribed(true);
                    db.subscribeProvider(pro.getProvider_id());
                    imageDownloadOrDelete.setImageResource(R.mipmap.ic_remove);
                }

                mAdapter.notifyDataSetChanged();
            }
        });

        db.unsubscribeProvider(1);
        builder.setTitle("Edit providers").setView(v);

        return v;
    }
}
