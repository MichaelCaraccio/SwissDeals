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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import ch.swissdeals.ProviderManager;


/**
 * The purpose of this class is to extract deals from the provider's website
 * using webscraping and basically regexes.
 * <p>
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

        for (DealParser d : this.providerParser) {
            JSONObject jDeal = new JSONObject();

            jDeal.put("title", webscrape(d.getTitleRegex()));
            jDeal.put("description", webscrape(d.getDescriptionRegex()));
            jDeal.put("image_url", webscrape(d.getImageRegex()));
            jDeal.put("price", tryParsePrice(webscrape(d.getPriceRegex())));
            jDeal.put("oldprice", tryParsePrice(webscrape(d.getOldPriceRegex())));
            jDeal.put("link", webscrape(d.getLinkRegex()));

            jDealsArray.put(jDeal);
        }

        jobj.put("deals", jDealsArray);

        return jobj;
    }

    private void downloadHtmlPage() throws IOException, ParserConfigurationException {
        Request request = new Request.Builder()
                .url(this.providerParser.getUrl())
                .build();

        Response response = client.newCall(request).execute();
        htmlBody = response.body().string();
        Log.d(TAG, "raw: " + htmlBody);
    }

    private String webscrape(String regex) {
        String result = null;

        Log.d(TAG, "regex: " + regex);

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        Matcher matcher = pattern.matcher(htmlBody);

        if (matcher.find()) {
            Log.d(TAG, "clean: " + matcher.group(1));

            result = matcher.group(1);
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
        text = StringEscapeUtils.unescapeHtml4(text);
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
}
