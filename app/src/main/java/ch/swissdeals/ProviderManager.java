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
    private static ProviderManager instance;

    private JSONObject jsonProviders;
    private Set<ProviderParser> providerParsers;
    private DatabaseHelper dbHelper;

    public static ProviderManager getInstance() {
        if (instance == null) {
            instance = new ProviderManager();
        }
        return instance;
    }

    private ProviderManager() {
        this.providerParsers = new HashSet<>();
    }

    public void load(Context context) throws JSONException {
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
     * <p>
     * Result can be null !
     *
     * @param providerID
     * @return
     * @throws NotLoadedException
     * @throws JSONException
     */
    public ProviderParser getProviderParser(String providerID) throws NotLoadedException, JSONException {
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
        //TODO: move this in a load function, add every provider into a map
        //TODO: and then in this function do: return mapProvider.get(providerName);
        //TODO: and in getAvailableProviders() do: return mapProvider.values();
        //TODO: warning ! we need both Provider and ProviderParser, store all in RAM ?
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
    private void upgradeDBProviders() {
        List<String> providersToKeep = new ArrayList<>();

        for (ProviderParser pParser : providerParsers) {
            String providerName = pParser.getProviderID();
            String url = pParser.getUrl();
            String favicon_url = pParser.getFaviconUrl();
            String category = pParser.getCategory();

            ModelProviders mProvider = new ModelProviders(providerName, url, favicon_url, category);
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
