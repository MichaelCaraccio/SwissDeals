package ch.swissdeals.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import ch.swissdeals.ProviderManager;
import ch.swissdeals.R;
import ch.swissdeals.database.controllers.DatabaseHelper;
import ch.swissdeals.database.models.ModelDeals;
import ch.swissdeals.database.models.ModelProviders;
import ch.swissdeals.webcrapping.DealsWebscrapper;

/**
 * Service that download deals on their respective website and save them into the database
 * This is a 2 step process, first it parses the html pages (webscraping) where the deals are shown
 * into a JSON REST-like format.
 * Then, it parses this JSON into ProviderModels/DealsProviders objects that we can persist into
 * database.
 * <p/>
 * It works this way because the webscrapping part can be easily removed and moved into a PHP (or
 * whatever) REST webservice backend who provide the same JSON REST-like format that we already use
 */
public class DealDownloaderService extends IntentService {
    private static final String TAG = DealDownloaderService.class.getSimpleName();
    private static final int MAX_RETRY = 5;
    //private static final long TIME_BETWEEN_TRIES = 5 * 60 * 1000; // 5 min
    private static final long TIME_BETWEEN_TRIES = 2000; // 2 sec
    public static final String INTENT_NEW_DEALS_ADDED = "ch.swissdeals.service.DealDownloaderService.NEW_DEALS_ADDED";
    private static final int NOTIFICATION_ID = 666;
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

        // STEP 1 - get user's all subscribed providers from database
        Iterable<ModelProviders> subscribedProviders = dbHelper.getSubscribedProviders();
        Log.d(TAG, "subscribed providers: " + subscribedProviders.toString());
        List<ModelDeals> webscrappedDeals = new ArrayList<>();

        // STEP 2 - foreach provider, do the webscrapping
        Iterator<ModelProviders> subscribedProvidersIterator = subscribedProviders.iterator();
        while (subscribedProvidersIterator.hasNext()) {

            ModelProviders mProvider = subscribedProvidersIterator.next();
            String providerName = mProvider.getName();

            DealsWebscrapper webscrapper = new DealsWebscrapper(providerName);
            JSONObject jDeals = webscrapper.getDeals();

            //TODO: parse JSON (rest-like) and create ModelDeals
            //FIXME: the deal's provider must be in the db
            // STEP 3 - format the webscrapping data into a REST-like JSON
            parseDeals(jDeals, webscrappedDeals);
        }

        // STEP 4 - remove all existing deals (TRUNCATE)
        dbHelper.deleteAllDeals();

        // STEP 5 - add freshly webscrapped deals into database
        for (ModelDeals deal : webscrappedDeals) {
            Log.d(TAG, "createDeal: " + deal.toString());
            dbHelper.createDeal(deal);
        }


        // we are forced to use a handler to show something on the screen because IntentService runs
        // in the background
        //TODO: notify (using a notification) user for new deals ?
        this.handler.post(new Runnable() {
            @Override
            public void run() {
                showNotification();
            }
        });

        Log.d(TAG, "new deals have been webscrapped and added to database");

        // STEP 6 - inform everyone who might be interested (MainActivity for example) that new deals have been downloaded
        broadcastResult();
    }

    private void showNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(getString(R.string.notification_content));

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    /**
     * Broadcast an Intent that inform that new deals have been downloaded
     */
    private void broadcastResult() {
        Intent i = new Intent();
        i.setAction(INTENT_NEW_DEALS_ADDED);
        sendBroadcast(i);
    }

    /**
     * Parse a REST-like deal into a ModelDeals
     *
     * @param jDeals           the REST-like deal you want to parse
     * @param webscrappedDeals a list where you want to collect the parsed deals, the main idea is
     *                         to use the same list to collect all deals and then add it entirely
     *                         into the database
     * @throws JSONException
     */
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
            Log.d(TAG, "getOrNullAttr: " + e.getMessage());
        }
        return value;
    }
}
