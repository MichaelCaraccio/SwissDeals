package ch.swissdeals;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class PopUpProviders extends DialogFragment implements ProvidersListFragment.OnFragmentInteractionListener {
    private MainActivity parent;
    private DealsPopupAdapter.ColorTheme colorTheme;
    private ProvidersListFragment fragProviderList;

    public PopUpProviders() {
        this.colorTheme = DealsPopupAdapter.ColorTheme.BLUE;
    }

    public PopUpProviders(MainActivity mainActivity) {
        this();
        this.parent = mainActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Set title for this dialog
        getDialog().setTitle("Edit Providers");

        final View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_pop_up_providers, container, false);

        this.fragProviderList = (ProvidersListFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.popup_list_providers_fragment);

        Button btn_cancel = (Button) v.findViewById(R.id.popup_btn_cancel);
        Button btn_update = (Button) v.findViewById(R.id.popup_btn_update);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.refreshDealsList();
                getDialog().dismiss();
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateListColorTheme(this.colorTheme);
    }

    public void updateListColorTheme(DealsPopupAdapter.ColorTheme colorTheme) {
        this.fragProviderList.updateListColorTheme(colorTheme);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // nothing
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Fragment fragment = (getFragmentManager().findFragmentById(R.id.popup_list_providers_fragment));
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }
}
