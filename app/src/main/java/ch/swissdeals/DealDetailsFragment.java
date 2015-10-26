package ch.swissdeals;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ch.swissdeals.database.controllers.DatabaseHelper;
import ch.swissdeals.database.models.ModelDeals;

/**
 * A placeholder fragment containing a simple view.
 */
public class DealDetailsFragment extends Fragment {

    private int positionMain;
    private ModelDeals deal;
    private DatabaseHelper helper;

    public DealDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View detailFragmentInflate = inflater.inflate(R.layout.fragment_deal_details_fragment, container,false);

        this.helper = new DatabaseHelper(getActivity());

        this.deal = this.helper.getDeal(this.positionMain);

        TextView dealTitle = (TextView) detailFragmentInflate.findViewById(R.id.title);

        dealTitle.setText(this.deal.getTitle());

        return detailFragmentInflate;
    }

    public void updatePosition(int pos) {
        Log.d("didu", String.valueOf(pos));
        this.positionMain = pos;
    }
}
