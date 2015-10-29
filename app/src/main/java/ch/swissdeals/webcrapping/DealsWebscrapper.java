package ch.swissdeals.webcrapping;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import ch.swissdeals.ProviderManager;


/**
 * The purpose of this class is to extract deals from the provider's website
 * using webscraping and basically regexes.
 * <p/>
 * It returns a JSON REST-like object in order to mimic the behaviour of a web service.
 * This way it can easily be replaced if we choose to rely on a webservice
 */
public class DealsWebscrapper {

    private static final String TAG = DealsWebscrapper.class.getSimpleName();
    private static OkHttpClient client = new OkHttpClient();
    private final ProviderParser providerParser;
    private String htmlBody;


    public DealsWebscrapper(String providerID) throws JSONException, ProviderManager.NotLoadedException, IOException, ParserConfigurationException {
        this.providerParser = ProviderManager.getInstance().getProviderParser(providerID);
        downloadHtmlPage();
    }

    public JSONObject getDeals() throws JSONException {

        JSONObject jobj = new JSONObject();
        jobj.put("providerName", providerParser.getProviderID());
        jobj.put("providerUrl", providerParser.getUrl());
        jobj.put("providerFaviconUrl", providerParser.getFaviconUrl());

        JSONArray jDealsArray = new JSONArray();

        int availableDeals = getTodayAvailableDeals();
        DealParser d = this.providerParser.iterator().next();
        for (int i = 1; i < availableDeals; i++) {
            JSONObject jDeal = new JSONObject();

            jDeal.put("title", webscrape(d.getTitleRegex(), i));
            jDeal.put("description", webscrape(d.getDescriptionRegex(), i));
            jDeal.put("image_url", getRealLink(d.getImageRegex(), i));
            jDeal.put("price", tryParsePrice(webscrape(d.getPriceRegex(), i)));
            jDeal.put("oldprice", tryParsePrice(webscrape(d.getOldPriceRegex(), i)));
            jDeal.put("link", getRealLink(d.getLinkRegex(), i));

            jDealsArray.put(jDeal);
        }

        jobj.put("deals", jDealsArray);

        return jobj;
    }

    private String getRealLink(String linkRegex, int i) {
        String urlRegexed = webscrape(linkRegex, i);
        try {
            if (!urlRegexed.startsWith("/")) {
                return null;
            }

            String strURL = this.providerParser.getUrl();
            Log.d(TAG, "strURL:" + strURL);
            URL url = new URL(strURL);
            StringBuilder b = new StringBuilder();
            b.append(url.getProtocol());
            b.append("://");
            b.append(url.getHost());
            b.append(urlRegexed);

            Log.d(TAG, "url rebuilt: " + b.toString());
            return b.toString();

        } catch (MalformedURLException e) {
            return null;
        }
    }

    private void downloadHtmlPage() throws IOException, ParserConfigurationException {
        Request request = new Request.Builder()
                .url(this.providerParser.getUrl())
                .build();

        Response response = client.newCall(request).execute();
        htmlBody = response.body().string();
        Log.d(TAG, "raw: " + htmlBody);
    }

    private String webscrape(String regex, int occurrence) {
        String result = null;

        Log.d(TAG, "regex: " + regex);

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        Matcher matcher = pattern.matcher(htmlBody);

        for (int i = 0; i < occurrence - 1; i++) {
            matcher.find();
        }
        if (matcher.find()) {
            // we start at 1 because group 0 contains all the regex
            for (int i = 1; i <= matcher.groupCount(); i++) {
                result = matcher.group(i) + " ";
            }
            Log.d(TAG, "clean: " + result);

            result = beautifyRegex(result);
        }

        Log.d(TAG, "result regexed: " + result);

        return result;
    }

    /**
     * Beautify ugly regex with multiple method
     * like unescape html entities
     *
     * @param text
     * @return cleaned text
     */
    private String beautifyRegex(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        text = StringEscapeUtils.unescapeHtml4(text);
        text = text.trim();
        return text;
    }

    private double tryParsePrice(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            Log.w(TAG, e.toString());
            return -1;
        }
    }

    /**
     * Assuming that all deals in the HTML page are structured the same way,
     * we can iterate over the regex title to retrieve all the today deals
     *
     * @return the number of deals found today for the given provider
     */
    private int getTodayAvailableDeals() {
        DealParser d = this.providerParser.iterator().next();

        String titleRegex = d.getTitleRegex();
        Pattern pattern = Pattern.compile(titleRegex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        Matcher matcher = pattern.matcher(htmlBody);

        int count = 0;
        while (matcher.find())
            count++;

        return count;
    }
}
