package ch.swissdeals;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

import ch.swissdeals.database.controllers.DatabaseHelper;
import ch.swissdeals.database.models.ModelDeals;
import ch.swissdeals.database.models.ModelProviders;

/**
 * Created by michaelcaraccio on 21/10/15.
 */
public class DatabaseTest extends AndroidTestCase {
    private DatabaseHelper db;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        db = new DatabaseHelper(context);
    }

    @Override
    public void tearDown() throws Exception {
        db.close();
        super.tearDown();
    }

    public void testCreateDB() throws Exception {
        SQLiteDatabase sqdb = db.getWritableDatabase();
        assertTrue(sqdb.isOpen());
        sqdb.close();
        Log.d("testCreateDB", "Pass");
    }

    public void testInsertDeals() throws Exception {

        // Deals
        ModelDeals d1 = new ModelDeals(1, "Titre 1", "Description 1", "http://www.image.com/monimage1.png", "http://www.image.com", 99, 120);
        ModelDeals d2 = new ModelDeals(2, "Titre 2", "Description 2", "http://www.image.com/monimage2.png", "http://www.image.com", 112, 199);
        ModelDeals d3 = new ModelDeals(3, "Titre 3", "Description 3", "http://www.image.com/monimage3.png", "http://www.image.com", 45, 90);

        long id1 = db.createDeal(d1);
        long id2 = db.createDeal(d2);
        long id3 = db.createDeal(d3);

        ModelDeals returnModel1 = db.getDeal(id1);
        ModelDeals returnModel2 = db.getDeal(id2);
        ModelDeals returnModel3 = db.getDeal(id3);

        assertEquals(1, returnModel1.getDeal_id());
        assertEquals(d1.getDescription(), returnModel1.getDescription());
        assertEquals(d1.getFk_provider_id(), returnModel1.getFk_provider_id());
        assertEquals(d1.getImage_url(), returnModel1.getImage_url());
        assertEquals(d1.getLink(), returnModel1.getLink());
        assertEquals(d1.getOld_price(), returnModel1.getOld_price());
        assertEquals(d1.getPrice(), returnModel1.getPrice());
        assertEquals(d1.getTitle(), returnModel1.getTitle());

        assertEquals(2, returnModel2.getDeal_id());
        assertEquals(d2.getDescription(), returnModel2.getDescription());
        assertEquals(d2.getFk_provider_id(), returnModel2.getFk_provider_id());
        assertEquals(d2.getImage_url(), returnModel2.getImage_url());
        assertEquals(d2.getLink(), returnModel2.getLink());
        assertEquals(d2.getOld_price(), returnModel2.getOld_price());
        assertEquals(d2.getPrice(), returnModel2.getPrice());
        assertEquals(d2.getTitle(), returnModel2.getTitle());

        assertEquals(3, returnModel3.getDeal_id());
        assertEquals(d3.getDescription(), returnModel3.getDescription());
        assertEquals(d3.getFk_provider_id(), returnModel3.getFk_provider_id());
        assertEquals(d3.getImage_url(), returnModel3.getImage_url());
        assertEquals(d3.getLink(), returnModel3.getLink());
        assertEquals(d3.getOld_price(), returnModel3.getOld_price());
        assertEquals(d3.getPrice(), returnModel3.getPrice());
        assertEquals(d3.getTitle(), returnModel3.getTitle());


        // Providers
        // TODO doit être mis a jour
        ModelProviders p1 = new ModelProviders("Qoqa.ch", "www.qoqa.ch", "","","");
        ModelProviders p2 = new ModelProviders("Qoqa2.ch", "www.qoqa2.ch", "","","");
        ModelProviders p3 = new ModelProviders("Qoqa3.ch", "www.qoqa3.ch", "","","");

        db.createProvider(p1);
        db.createProvider(p2);
        db.createProvider(p3);

        ModelProviders returnModelP1 = db.getProvider(p1.getName());
        ModelProviders returnModelP2 = db.getProvider(p2.getName());
        ModelProviders returnModelP3 = db.getProvider(p3.getName());

        assertEquals(1, returnModelP1.getProvider_id());
        assertEquals(p1.getName(), returnModelP1.getName());
        assertEquals(p1.getFavicon_url(), returnModelP1.getFavicon_url());
        assertEquals(p1.getUrl(), returnModelP1.getUrl());

        assertEquals(2, returnModelP2.getProvider_id());
        assertEquals(p2.getName(), returnModelP2.getName());
        assertEquals(p2.getFavicon_url(), returnModelP2.getFavicon_url());
        assertEquals(p2.getUrl(), returnModelP2.getUrl());

        assertEquals(3, returnModelP3.getProvider_id());
        assertEquals(p3.getName(), returnModelP3.getName());
        assertEquals(p3.getFavicon_url(), returnModelP3.getFavicon_url());
        assertEquals(p3.getUrl(), returnModelP3.getUrl());

    }

    public void testGetAllDeals() throws Exception {

        // Create deals
        ModelDeals d1 = new ModelDeals(1, "Titre 1", "Description 1", "http://www.image.com/monimage1.png", "http://www.image.com", 99, 120);
        ModelDeals d2 = new ModelDeals(2, "Titre 2", "Description 2", "http://www.image.com/monimage2.png", "http://www.image.com", 112, 199);
        ModelDeals d3 = new ModelDeals(3, "Titre 3", "Description 3", "http://www.image.com/monimage3.png", "http://www.image.com", 45, 90);

        db.createDeal(d1);
        db.createDeal(d2);
        db.createDeal(d3);

        List<ModelDeals> listDeals = db.getAllDeals();

        ModelDeals returnD1 = listDeals.get(0);

        assertEquals(1, returnD1.getDeal_id());
        assertEquals(d1.getDescription(), returnD1.getDescription());
        assertEquals(d1.getFk_provider_id(), returnD1.getFk_provider_id());
        assertEquals(d1.getImage_url(), returnD1.getImage_url());
        assertEquals(d1.getLink(), returnD1.getLink());
        assertEquals(d1.getOld_price(), returnD1.getOld_price());
        assertEquals(d1.getPrice(), returnD1.getPrice());
        assertEquals(d1.getTitle(), returnD1.getTitle());


    }

    public void testDeleteProviders() {

        // Create deals
        ModelDeals d1 = new ModelDeals(1, "Titre 1", "Description 1", "http://www.image.com/monimage1.png", "http://www.image.com", 99, 120);
        ModelDeals d2 = new ModelDeals(2, "Titre 2", "Description 2", "http://www.image.com/monimage2.png", "http://www.image.com", 112, 199);
        ModelDeals d3 = new ModelDeals(3, "Titre 3", "Description 3", "http://www.image.com/monimage3.png", "http://www.image.com", 45, 90);

        db.createDeal(d1);
        db.createDeal(d2);
        db.createDeal(d3);

        // Create providers
        // TODO doit être mis a jour
        ModelProviders p1 = new ModelProviders("Qoqa.ch", "www.qoqa.ch", "","","");
        ModelProviders p2 = new ModelProviders("Qoqa2.ch", "www.qoqa2.ch", "","","");
        ModelProviders p3 = new ModelProviders("Qoqa3.ch", "www.qoqa3.ch", "","","");

        db.createProvider(p1);
        db.createProvider(p2);
        db.createProvider(p3);

        // Providers to delete
        List<String> listProviderName = Arrays.asList("Qoqa.ch", "Qoqa2.ch");

        // Call delete methods
        db.deleteProviders(listProviderName, false);

        // Get providers and see if Qoqa3.ch still in the database
        List<ModelProviders> providers = db.getAllProviders();

        for (ModelProviders provider : providers) {
            assertEquals(3, provider.getProvider_id());
            assertEquals(p3.getName(), provider.getName());
            assertEquals(p3.getFavicon_url(), provider.getFavicon_url());
            assertEquals(p3.getUrl(), provider.getUrl());
        }
    }

    public void testDeleteProvidersCascade() {

        // Create deals
        ModelDeals d1 = new ModelDeals(1, "Titre 1", "Description 1", "http://www.image.com/monimage1.png", "http://www.image.com", 99, 120);
        ModelDeals d2 = new ModelDeals(2, "Titre 2", "Description 2", "http://www.image.com/monimage2.png", "http://www.image.com", 112, 199);
        ModelDeals d3 = new ModelDeals(3, "Titre 3", "Description 3", "http://www.image.com/monimage3.png", "http://www.image.com", 45, 90);

        db.createDeal(d1);
        db.createDeal(d2);
        db.createDeal(d3);

        // Create providers
        // TODO doit être mis a jour
        ModelProviders p1 = new ModelProviders("Qoqa.ch", "www.qoqa.ch", "","","");
        ModelProviders p2 = new ModelProviders("Qoqa2.ch", "www.qoqa2.ch", "","","");
        ModelProviders p3 = new ModelProviders("Qoqa3.ch", "www.qoqa3.ch", "","","");

        db.createProvider(p1);
        db.createProvider(p2);
        db.createProvider(p3);

        // Providers to delete
        List<String> listProviderName = Arrays.asList("Qoqa.ch", "Qoqa2.ch");

        // Call delete methods
        db.deleteProviders(listProviderName, true);

        // Get providers and see if Qoqa3.ch still in the database
        List<ModelProviders> providers = db.getAllProviders();

        for (ModelProviders provider : providers) {
            assertEquals(3, provider.getProvider_id());
            assertEquals(p3.getName(), provider.getName());
            assertEquals(p3.getFavicon_url(), provider.getFavicon_url());
            assertEquals(p3.getUrl(), provider.getUrl());
        }

        // Get providers and see if Qoqa3.ch still in the database
        List<ModelDeals> deals = db.getAllDeals();

        for (ModelDeals deal : deals) {
            assertEquals(3, deal.getDeal_id());
            assertEquals(d3.getDescription(), deal.getDescription());
            assertEquals(d3.getFk_provider_id(), deal.getFk_provider_id());
            assertEquals(d3.getImage_url(), deal.getImage_url());
            assertEquals(d3.getLink(), deal.getLink());
            assertEquals(d3.getOld_price(), deal.getOld_price());
            assertEquals(d3.getPrice(), deal.getPrice());
            assertEquals(d3.getTitle(), deal.getTitle());
        }
    }
}