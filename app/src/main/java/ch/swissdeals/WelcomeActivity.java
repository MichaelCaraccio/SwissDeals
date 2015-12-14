package ch.swissdeals;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;

import ch.swissdeals.prefs.SwissDealsPrefs;
import ch.swissdeals.service.DealDownloaderService;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener, ProvidersListFragment.OnFragmentInteractionListener {

    private ProvidersListFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        if (hasUserAlreadySeenThisScreen()) {
            startNextActivity();
        }

        this.fragment = (ProvidersListFragment) getSupportFragmentManager().findFragmentById(R.id.welcome_deal_subscribed_fragment);
        this.fragment.updateListColorTheme(DealsPopupAdapter.ColorTheme.WHITE);

        Button btnGo = (Button) findViewById(R.id.go_button);
        btnGo.setOnClickListener(this);

        loadProviders();
    }

    @Override
    public void onClick(View v) {
        // remember that the user have seen this welcome screen
        SharedPreferences.Editor editor = getSharedPreferences(SwissDealsPrefs.PREF_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(SwissDealsPrefs.SKIP_WELCOME, true);
        editor.apply();

        Toast.makeText(getApplicationContext(), this.getString(R.string.after_welcome), Toast.LENGTH_LONG).show();

        startService(new Intent(this, DealDownloaderService.class));
        startNextActivity();
    }

    private boolean hasUserAlreadySeenThisScreen() {
        SharedPreferences prefs = getSharedPreferences(SwissDealsPrefs.PREF_NAME, MODE_PRIVATE);
        return prefs.getBoolean(SwissDealsPrefs.SKIP_WELCOME, false);
    }

    private void loadProviders() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    ProviderManager.getInstance().load(getApplicationContext());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                fragment.refreshProvidersList();
            }
        }.execute();
    }

    private void startNextActivity() {
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //nothing
    }
}
