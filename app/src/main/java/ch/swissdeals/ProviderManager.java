package ch.swissdeals;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;

import ch.swissdeals.webcrapping.ProviderParser;
import ch.swissdeals.webcrapping.ProviderParserFactory;


public class ProviderManager {

    private static final String FILENAME = "subscribed_providers.dat";
    private static final int RES_ID = R.raw.providers;
    private static final String TAG = ProviderManager.class.getSimpleName();
    private static final String JSON_ARRAY_NAME = "providers";
    private static ProviderManager instance;

    private JSONObject jsonProviders;
    private Set<String> subscribedProviders;

    public static ProviderManager getInstance() {
        if (instance == null) {
            instance = new ProviderManager();
        }
        return instance;
    }

    private ProviderManager() {
        this.subscribedProviders = new HashSet<>();
    }

    public void load(Context context) throws JSONException {
        loadJSONFromAssets(context);
        loadUserProviders(context);
    }

//    public List<Provider> getAvailableProviders() {
//        //TODO:
//        return null;
//    }

//    public List<Provider> getSubscribedProviders() {
//        //TODO...
//        return null;
//    }

//    public List<Provider> getUnusedProviders() {
//        //TODO...
//        return null;
//    }

    public Iterable<String> getSubscribedProviders() {
        return subscribedProviders;
    }

    /**
     * save the user's providers. Don't forget to call this method
     * after calling subscribe or unsubscribe to persist the changes.
     * <p/>
     * TODO: TEST IT !!
     *
     * @param context
     * @throws IOException
     * @throws JSONException
     */
    public void saveUserProviders(Context context) throws IOException, JSONException {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(FILENAME, Context.MODE_PRIVATE));
        String data = convertUserProvidersToJSONArray();
        outputStreamWriter.write(data);
        outputStreamWriter.close();
    }

    /**
     * @param providerID
     * @return
     * @see Set#add(Object)
     * Don't forget to call saveUserProviders() to make this effective
     * <p/>
     * TODO: TEST IT !!
     */
    public boolean subscribe(String providerID) {
        return this.subscribedProviders.add(providerID);
    }

    /**
     * @param providerID
     * @see Set#remove(Object)
     * * Don't forget to call saveUserProviders() to make this effective
     * <p/>
     * TODO: TEST IT !!
     */
    public void unsubscribe(String providerID) {
        this.subscribedProviders.remove(providerID);
    }

    public ProviderParser getProviderParser(String providerID) throws NotLoadedException, JSONException {
        if (this.jsonProviders == null) {
            throw new NotLoadedException();
        }

        //TODO: move this in a load function, add every provider into a map
        //TODO: and then in this function do: return mapProvider.get(providerName);
        //TODO: and in getAvailableProviders() do: return mapProvider.values();
        //TODO: warning ! we need both Provider and ProviderParser, store all in RAM ?
        JSONArray arrProviders = this.jsonProviders.getJSONArray("providers");

        for (int i = 0; i < arrProviders.length(); i++) {
            JSONObject jobj = arrProviders.getJSONObject(i);

            String providerName = jobj.getString("name");
            if (providerName.equals(providerID)) {
                Log.d(TAG, "providerID [" + providerID + "] found !");
                return ProviderParserFactory.fromJSON(jobj);
            }
        }
        Log.e(TAG, "providerID [" + providerID + "] NOT found !");
        return null;
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

    //TODO: TEST IT !!
    private void loadUserProviders(Context context) throws JSONException {

        InputStream is = null;
        try {
            is = context.openFileInput(FILENAME);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);

            String rawData = new String(buffer, "UTF-8");
            if (rawData.isEmpty()) {
                Log.d(TAG, "no previous data found in " + FILENAME);
                return;
            }

            JSONObject jobj = new JSONObject(rawData);
            JSONArray jProviders = jobj.getJSONArray(JSON_ARRAY_NAME);

            this.subscribedProviders.clear();
            for (int i = 0; i < jProviders.length(); i++) {
                this.subscribedProviders.add(jProviders.getString(i));
            }
        } catch (FileNotFoundException e) {
            Log.d(TAG, "file does not exist yet !");
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

    //TODO: TEST IT !!
    private String convertUserProvidersToJSONArray() throws JSONException {
        JSONObject jobj = new JSONObject();

        JSONArray jArr = new JSONArray();
        for (String provider : this.subscribedProviders) {
            jArr.put(provider);
        }

        jobj.put(JSON_ARRAY_NAME, jArr);
        return jobj.toString();
    }

    public class NotLoadedException extends Exception {

        public NotLoadedException() {
            super(RES_ID + " file not loaded. Make sure you have called load() method at least once.");
        }
    }
}
