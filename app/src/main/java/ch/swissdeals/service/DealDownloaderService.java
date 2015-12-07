package ch.swissdeals.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import ch.swissdeals.ProviderManager;
import ch.swissdeals.database.controllers.DatabaseHelper;
import ch.swissdeals.database.models.ModelDeals;
import ch.swissdeals.database.models.ModelProviders;
import ch.swissdeals.webcrapping.DealsWebscrapper;

public class DealDownloaderService extends IntentService {
    private static final String TAG = DealDownloaderService.class.getSimpleName();
    private static final int MAX_RETRY = 5;
    //private static final long TIME_BETWEEN_TRIES = 5 * 60 * 1000; // 5 min
    private static final long TIME_BETWEEN_TRIES = 2000; // 2 sec
    private final Handler handler;
    private DatabaseHelper dbHelper;

    public DealDownloaderService() {
        super("DealDownloaderService");
        this.handler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            int retryCounter = 0;

            while (!isUserOnline() && retryCounter < MAX_RETRY) {
                Thread.sleep(TIME_BETWEEN_TRIES);
                retryCounter++;

                Log.d(TAG, "retryCounter:" + retryCounter);
            }

            // if we have not been able to connect to Internet, we give up.
            if (retryCounter >= MAX_RETRY) {
                Log.e(TAG, "Cannot connect to Internet. This job is cancelled.");
                return;
            }

            work();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void work() throws JSONException, ParserConfigurationException, ProviderManager.NotLoadedException, IOException {
        final Context ctx = getApplicationContext();

        dbHelper = new DatabaseHelper(ctx);

        ProviderManager providerManager = ProviderManager.getInstance();
        providerManager.load(ctx);

        //TODO: getSubscribedProviders()
        Iterable<ModelProviders> subscribedProviders = dbHelper.getSubscribedProviders();
        Log.d(TAG, "subscribed providers: " + subscribedProviders.toString());
        List<ModelDeals> webscrappedDeals = new ArrayList<>();
        //TODO: foreach -> call DealWebscrapper
        Iterator<ModelProviders> subscribedProvidersIterator = subscribedProviders.iterator();
        while (subscribedProvidersIterator.hasNext()) {

            ModelProviders mProvider = subscribedProvidersIterator.next();
            String providerName = mProvider.getName();

            DealsWebscrapper webscrapper = new DealsWebscrapper(providerName);
            JSONObject jDeals = webscrapper.getDeals();

            //TODO: parse JSON (rest-like) and create ModelDeals
            //FIXME: the deal's provider must be in the db
            parseDeals(jDeals, webscrappedDeals);
        }

        /// TRANSACTION
        //TODO: remove all existing deals (TRUNCATE)
        dbHelper.deleteAllDeals();
        //TODO: add freshly parsed deals (ModelDeals) to DB
        for (ModelDeals deal : webscrappedDeals) {
            Log.d(TAG, "createDeal: " + deal.toString());
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

        broadcastResult();

        Log.d(TAG, "persisted providers: " + dbHelper.getAllProviders().toString());
    }

    private void broadcastResult() {
        Intent i = new Intent();
        i.setAction("ch.swissdeals.service.DealDownloaderService.NEW_DEALS_ADDED");
        sendBroadcast(i);
    }

    private void parseDeals(JSONObject jDeals, List<ModelDeals> webscrappedDeals) throws JSONException {
        String providerName = jDeals.getString("providerName");
        final int providerID = dbHelper.getProviderIDFromName(providerName);

        JSONArray jArr = jDeals.getJSONArray("deals");

        for (int i = 0; i < jArr.length(); i++) {
            JSONObject j = jArr.getJSONObject(i);
            ModelDeals d = new ModelDeals();

            d.setFk_provider_id(providerID);
            d.setTitle(getOrNullAttr(j, "title"));
            d.setDescription(getOrNullAttr(j, "description"));
            d.setImage_url(getOrNullAttr(j, "image_url"));
            d.setPrice(parsePrice(getOrNullAttr(j, "price")));
            d.setOld_price(parsePrice(getOrNullAttr(j, "oldprice")));
            d.setLink(getOrNullAttr(j, "link"));

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

    private boolean isUserOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private static String getOrNullAttr(JSONObject j, String attr) {
        String value = null;
        try {
            value = j.getString(attr);
        } catch (JSONException e) {
            Log.e(TAG, "getOrNullAttr: " + e.getMessage());
        }
        return value;
    }
}
