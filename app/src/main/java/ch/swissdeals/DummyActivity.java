package ch.swissdeals;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.swissdeals.service.DealDownloaderService;


public class DummyActivity extends AppCompatActivity {

    private static final String TAG = DummyActivity.class.getSimpleName();
    private TextView tvLabel;
    private Button btnStartService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);

        this.tvLabel = (TextView) findViewById(R.id.tvLabel);
        this.tvLabel.setText("YOLOOOOO");


        this.btnStartService = (Button) findViewById(R.id.btnStartService);
        this.btnStartService.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startService(new Intent(getApplicationContext(), DealDownloaderService.class));
                        Toast.makeText(getApplicationContext(), "service started !", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        String providerID = "QoQa.ch";

        //////////////////////////
        // DealWebscrapper
        //////////////////////////
//        ProviderManager providerManager = ProviderManager.getInstance();
//        try {
//            providerManager.load(getApplicationContext());
//
//
//            ProviderParser qoqaParser = providerManager.getProviderParser(providerID);
//
//            for (DealParser d : qoqaParser) {
//                Log.d(TAG, d.toString());
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (ProviderManager.NotLoadedException e) {
//            e.printStackTrace();
//        }

        try {
            ProviderManager providerManager = ProviderManager.getInstance();
            providerManager.load(getApplicationContext());
            providerManager.subscribe("QoQa.ch");
            providerManager.subscribe("digitec.ch");
            providerManager.subscribe("QWine.ch");
            providerManager.subscribe("QSport.ch");
//            providerManager.subscribe("QoQa.ch");
//            providerManager.subscribe("microspotYOLO.ch");
//            providerManager.subscribe("deindeal.ch");

            providerManager.saveUserProviders(getApplicationContext());

            Log.d(TAG, providerManager.getSubscribedProviders().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        new AsyncTask<String, Void, Void>() {
//
//            @Override
//            protected Void doInBackground(String... params) {
//                try {
//                    String providerID = params[0];
//                    ProviderManager.getInstance().load(getApplicationContext());
//                    DealsWebscrapper webscrapper = new DealsWebscrapper(providerID);
//                    JSONObject jDeals = webscrapper.getDeals();
//
//                    Log.d(TAG, "jDeals: " + jDeals.toString(4));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                } catch (ProviderManager.NotLoadedException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (ParserConfigurationException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//        }.execute(providerID);

        //////////////////////////
        // Regex only
        //////////////////////////
//        new RegexAsyncTask().execute();

    }

    private class RegexAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String result = null;

            try {
                String url = "http://www.qoqa.ch/";
                //String regex = "<title>QoQa.ch \\| ([\\w ]+)";
                String regex = "<meta property=\"og:description\" content=\"(.+)\"";


                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();
                String raw = response.body().string();
                Log.d(TAG, "raw: " + raw);

                Log.d(TAG, "regex: " + regex);

                Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
                Matcher matcher = pattern.matcher(raw);

                if (matcher.find()) {
                    Log.d(TAG, "clean: " + matcher.group(1));

                    // TODO: beautify regexed result
                    result = matcher.group(1);
                    result = beautifyRegex(result);

                }

                Log.d(TAG, "result regexed: " + result);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            tvLabel.setText(result);
        }


        private String beautifyRegex(String text) {
            text = StringEscapeUtils.unescapeHtml4(text);
            return text;
        }
    }
}
