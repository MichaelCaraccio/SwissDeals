package ch.swissdeals.service;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProviderParserFactory {

    private static final String TAG = ProviderParserFactory.class.getSimpleName();

    public static ProviderParser fromJSON(JSONObject jobj) throws JSONException {
        String providerID = jobj.getString("name");
        String url = jobj.getString("url");
        ProviderParser provider = new ProviderParser(providerID, url);

        JSONArray jDeals = jobj.getJSONArray("deals");
        for (int i = 0; i < jDeals.length(); i++) {
            DealParser dealParser = new DealParser();
            JSONObject jDeal = jDeals.getJSONObject(i);

            dealParser.setTitleXPath(getOrNullAttr(jDeal, "title_xpath"));
            dealParser.setTitleRegex(getOrNullAttr(jDeal, "title_regex"));

            dealParser.setDescriptionXPath(getOrNullAttr(jDeal, "description_xpath"));
            dealParser.setDescriptionRegex(getOrNullAttr(jDeal, "description_regex"));

            dealParser.setImageXPath(getOrNullAttr(jDeal, "image_xpath"));
            dealParser.setImageRegex(getOrNullAttr(jDeal, "image_regex"));

            dealParser.setLinkXPath(getOrNullAttr(jDeal, "link_xpath"));
            dealParser.setLinkRegex(getOrNullAttr(jDeal, "link_regex"));

            dealParser.setPriceXPath(getOrNullAttr(jDeal, "price_xpath"));
            dealParser.setPriceRegex(getOrNullAttr(jDeal, "price_regex"));

            dealParser.setOldPriceXPath(getOrNullAttr(jDeal, "oldprice_xpath"));
            dealParser.setOldPriceRegex(getOrNullAttr(jDeal, "oldprice_regex"));

            provider.addDeal(dealParser);
        }
        return provider;
    }

    private static String getOrNullAttr(JSONObject j, String attr) {
        String value = null;
        try {
            value = j.getString(attr);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return value;
    }
}
