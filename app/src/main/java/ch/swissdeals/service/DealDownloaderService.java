package ch.swissdeals.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import ch.swissdeals.ProviderManager;
import ch.swissdeals.database.controllers.DatabaseHelper;
import ch.swissdeals.database.models.ModelDeals;
import ch.swissdeals.database.models.ModelProviders;
import ch.swissdeals.webcrapping.DealsWebscrapper;

public class DealDownloaderService extends IntentService {
    private static final String TAG = DealDownloaderService.class.getSimpleName();
    private final Handler handler;
    private DatabaseHelper dbHelper;

    public DealDownloaderService() {
        super("DealDownloaderService");
        this.handler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            work();
        } catch (JSONException | ParserConfigurationException | ProviderManager.NotLoadedException | IOException e) {
            e.printStackTrace();
        }
    }

    private void work() throws JSONException, ParserConfigurationException, ProviderManager.NotLoadedException, IOException {
        final Context ctx = getApplicationContext();

        dbHelper = new DatabaseHelper(ctx);

        ProviderManager providerManager = ProviderManager.getInstance();
        providerManager.load(ctx);

        //TODO: getSubscribedProviders()
        Iterable<String> subscribedProviders = providerManager.getSubscribedProviders();
        Log.d(TAG, "subscribed providers: " + providerManager.getSubscribedProviders().toString());
        List<ModelDeals> webscrappedDeals = new ArrayList<>();
        //TODO: foreach -> call DealWebscrapper
        for (String providerName : subscribedProviders) {
            //TODO: add or update existing providers (only thoses chosen by the user)
            ModelProviders pModel = providerManager.getModelProvider(providerName);
            dbHelper.createOrUpdateProvider(pModel);

            DealsWebscrapper webscrapper = new DealsWebscrapper(providerName);
            JSONObject jDeals = webscrapper.getDeals();

            //TODO: parse JSON (rest-like) and create ModelDeals
            //FIXME: the deal's provider must be in the db
            parseDeals(jDeals, webscrappedDeals);
        }

        /// TRANSACTION
        //TODO: remove unused providers (AND ITS DEALS !!! (no cascade delete), not necessary if TRUNCATE)

        Iterable<ModelProviders> unusedProviders = providerManager.getUnusedProviders();
        List<String> unusedProvidersName = new ArrayList<>();
        for (ModelProviders unusedProvider : unusedProviders) {
            unusedProvidersName.add(unusedProvider.getName());
        }
        boolean mustRemoveLinkedDeals = false;
        Log.d(TAG, "unusedProvidersName: " + unusedProvidersName.toString());
        dbHelper.deleteProviders(unusedProvidersName, mustRemoveLinkedDeals);

        //TODO: remove all existing deals (TRUNCATE)
        dbHelper.deleteAllDeals();
        //TODO: add freshly parsed deals (ModelDeals) to DB
        for (ModelDeals deal : webscrappedDeals) {
            dbHelper.createDeal(deal);
        }
        /// END TRANSACTION


        // we are forced to use a handler to show something on the screen because IntentService runs
        // in the background
        //TODO: notify user for new deals ?
        this.handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ctx, new Date().toString() + " - Je suis couru !", Toast.LENGTH_SHORT).show();
            }
        });

        Log.d(TAG, "Je suis couru !");

        Log.d(TAG, "persisted providers: " + dbHelper.getAllProviders().toString());
    }

    private void parseDeals(JSONObject jDeals, List<ModelDeals> webscrappedDeals) throws JSONException {
        String providerName = jDeals.getString("providerName");
        final int providerID = dbHelper.getProviderIDFromName(providerName);

        JSONArray jArr = jDeals.getJSONArray("deals");

        for (int i = 0; i < jArr.length(); i++) {
            JSONObject j = jArr.getJSONObject(i);
            ModelDeals d = new ModelDeals();

            d.setFk_provider_id(providerID);
            d.setTitle(j.getString("title"));
            d.setDescription(j.getString("description"));
            d.setImage_url(j.getString("image_url"));
            d.setPrice(parsePrice(j.getString("price")));
            d.setOld_price(parsePrice(j.getString("oldprice")));
            d.setLink(j.getString("link"));

            webscrappedDeals.add(d);
        }
    }

    private float parsePrice(String raw) {
        try {
            return Float.parseFloat(raw);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return -1;
        }
    }
}
