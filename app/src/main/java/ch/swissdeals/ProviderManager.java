package ch.swissdeals;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.swissdeals.database.controllers.DatabaseHelper;
import ch.swissdeals.database.models.ModelProviders;
import ch.swissdeals.webcrapping.ProviderParser;
import ch.swissdeals.webcrapping.ProviderParserFactory;


public class ProviderManager {
    private static final int RES_ID = R.raw.providers;
    private static final String TAG = ProviderManager.class.getSimpleName();
    private static final String JSON_ARRAY_NAME = "providers";
    private static volatile ProviderManager instance;
    private static final Object lock = new Object();

    private JSONObject jsonProviders;
    private Set<ProviderParser> providerParsers;
    private DatabaseHelper dbHelper;

    // source: http://stackoverflow.com/a/11165926
    public static ProviderManager getInstance() {
        ProviderManager mgr = instance;
        if (mgr == null) {
            synchronized (lock) {
                mgr = instance;
                if (mgr == null) {
                    mgr = new ProviderManager();
                    instance = mgr;
                }
            }
        }
        return mgr;
    }

    private ProviderManager() {
        this.providerParsers = new HashSet<>();
    }

    public synchronized void load(Context context) throws JSONException {
        this.dbHelper = new DatabaseHelper(context);
        loadJSONFromAssets(context);
        buildProviders();
        upgradeDBProviders();
    }

    public void subscribe(String providerID) {
        this.dbHelper.subscribeProvider(providerID);
    }

    public void unsubscribe(String providerID) {
        this.dbHelper.unsubscribeProvider(providerID);
    }

    /**
     * Returns a ProviderParser from a providerID.
     * <p/>
     * <b>Warning</b> Result can be null !
     *
     * @param providerID the provider id
     * @return a ProviderParser
     * @throws NotLoadedException
     * @throws JSONException
     */
    public synchronized ProviderParser getProviderParser(String providerID) throws NotLoadedException, JSONException {
        if (this.jsonProviders == null) {
            throw new NotLoadedException();
        }

        for (ProviderParser pParser : this.providerParsers) {
            if (pParser.getProviderID().equals(providerID))
                return pParser;
        }
        return null;
    }

    private void buildProviders() throws JSONException {
        JSONArray arrProviders = this.jsonProviders.getJSONArray("providers");

        for (int i = 0; i < arrProviders.length(); i++) {
            JSONObject jobj = arrProviders.getJSONObject(i);

            ProviderParser pParser = ProviderParserFactory.fromJSON(jobj);
            this.providerParsers.add(pParser);
        }
    }

    private void loadJSONFromAssets(Context context) throws JSONException {
        InputStream is = null;
        try {
            is = context.getResources().openRawResource(RES_ID);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);

            this.jsonProviders = new JSONObject(new String(buffer, "UTF-8"));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * update existing provider in database
     * and remove the old/removed ones.
     * Keep as much as possible the user's subscribed providers
     */
    private synchronized void upgradeDBProviders() {
        List<String> providersToKeep = new ArrayList<>();

        for (ProviderParser pParser : providerParsers) {
            String providerName = pParser.getProviderID();
            String displayName = pParser.getDisplayName();
            String url = pParser.getUrl();
            String favicon_url = pParser.getFaviconUrl();
            String category = pParser.getCategory();

            ModelProviders mProvider = new ModelProviders(providerName, displayName, url, favicon_url, category);
            this.dbHelper.createOrUpdateProvider(mProvider);

            providersToKeep.add(providerName);
        }

        boolean cascadeRemove = true;
        this.dbHelper.deleteAllProvidersExcept(providersToKeep, cascadeRemove);
    }

    public class NotLoadedException extends Exception {

        public NotLoadedException() {
            super(RES_ID + " file not loaded. Make sure you have called load() method at least once.");
        }
    }
}
