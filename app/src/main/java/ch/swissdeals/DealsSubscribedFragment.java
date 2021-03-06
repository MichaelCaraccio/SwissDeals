package ch.swissdeals;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;

import ch.swissdeals.database.controllers.DatabaseHelper;
import ch.swissdeals.database.models.ModelDeals;

public class DealsSubscribedFragment extends Fragment implements AbsListView.OnItemClickListener {
    private List<ModelDeals> listdeals;
    public static final String POSITION_MAIN_LIST = "DealsSubscribedFragment.list.position";
    private Context ctx;

    private OnFragmentInteractionListener mListener;

    private ListView mListView;
    private LinearLayout mPlaceHolderView;

    public static DealsSubscribedFragment newInstance() {
        DealsSubscribedFragment fragment = new DealsSubscribedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DealsSubscribedFragment() {
        // nothing
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ctx = getActivity().getApplicationContext();
        updateAdapter();
    }

    /**
     * Update deals list from database
     */
    public void updateAdapter() {
        DatabaseHelper db = new DatabaseHelper(ctx);
        listdeals = db.getAllDealsSubscribed();

        boolean mustShowPlaceholder = listdeals.size() == 0;
        showPlaceholder(mustShowPlaceholder);

        DealsSubscribedAdapter mAdapter = new DealsSubscribedAdapter(ctx, listdeals);

        if (mListView != null)
            mListView.setAdapter(mAdapter);
    }

    private void showPlaceholder(boolean mustShowPlaceholder) {
        // when the fragment start the view is null
        if (mPlaceHolderView == null)
            return;

        if (mustShowPlaceholder) {
            mPlaceHolderView.setVisibility(LinearLayout.VISIBLE);
        } else {
            mPlaceHolderView.setVisibility(LinearLayout.INVISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_deals_subscribed, container, false);

        // Set the adapter
        mListView = (ListView) view.findViewById(R.id.content_deal_subscribed_list);
        mPlaceHolderView = (LinearLayout) view.findViewById(R.id.content_deal_subscribed_placeholder);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateAdapter();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            mListener = (OnFragmentInteractionListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.

            mListener.onFragmentInteraction(listdeals.get(position).getDeal_id());
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int id);
    }

}
