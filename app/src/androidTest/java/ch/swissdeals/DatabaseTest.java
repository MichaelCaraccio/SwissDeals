package ch.swissdeals;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.util.Log;

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

    public void testCreateDB() throws Exception{
        SQLiteDatabase sqdb = db.getWritableDatabase();
        assertTrue(sqdb.isOpen());
        sqdb.close();
        Log.d("testCreateDB", "Pass");
    }

    public void testInsertDeals() throws Exception{

        // Deals
        ModelDeals d1 = new ModelDeals(1,1,"Titre 1", "Description 1", "http://www.image.com/monimage1.png", "http://www.image.com",99,120);
        ModelDeals d2 = new ModelDeals(2,2,"Titre 2", "Description 2", "http://www.image.com/monimage2.png", "http://www.image.com",112,199);
        ModelDeals d3 = new ModelDeals(3,3,"Titre 3", "Description 3", "http://www.image.com/monimage3.png", "http://www.image.com",45,90);

        long id1 = db.createDeal(d1);
        long id2 = db.createDeal(d2);
        long id3 = db.createDeal(d3);

        ModelDeals returnModel1 = db.getDeal(id1);
        ModelDeals returnModel2 = db.getDeal(id2);
        ModelDeals returnModel3 = db.getDeal(id3);

        assertEquals(d1.getDeal_id(), returnModel1.getDeal_id());
        assertEquals(d1.getDescription(), returnModel1.getDescription());
        assertEquals(d1.getFk_provider_id(), returnModel1.getFk_provider_id());
        assertEquals(d1.getImage_url(), returnModel1.getImage_url());
        assertEquals(d1.getLink(), returnModel1.getLink());
        assertEquals(d1.getOld_price(), returnModel1.getOld_price());
        assertEquals(d1.getPrice(), returnModel1.getPrice());
        assertEquals(d1.getTitle(), returnModel1.getTitle());

        assertEquals(d2.getDeal_id(), returnModel2.getDeal_id());
        assertEquals(d2.getDescription(), returnModel2.getDescription());
        assertEquals(d2.getFk_provider_id(), returnModel2.getFk_provider_id());
        assertEquals(d2.getImage_url(), returnModel2.getImage_url());
        assertEquals(d2.getLink(), returnModel2.getLink());
        assertEquals(d2.getOld_price(), returnModel2.getOld_price());
        assertEquals(d2.getPrice(), returnModel2.getPrice());
        assertEquals(d2.getTitle(), returnModel2.getTitle());

        assertEquals(d3.getDeal_id(), returnModel3.getDeal_id());
        assertEquals(d3.getDescription(), returnModel3.getDescription());
        assertEquals(d3.getFk_provider_id(), returnModel3.getFk_provider_id());
        assertEquals(d3.getImage_url(), returnModel3.getImage_url());
        assertEquals(d3.getLink(), returnModel3.getLink());
        assertEquals(d3.getOld_price(), returnModel3.getOld_price());
        assertEquals(d3.getPrice(), returnModel3.getPrice());
        assertEquals(d3.getTitle(), returnModel3.getTitle());


        // Providers
        ModelProviders p1 = new ModelProviders(1, "Qoqa.ch", "www.qoqa.ch", null);
        ModelProviders p2 = new ModelProviders(2, "Qoqa2.ch", "www.qoqa2.ch", null);
        ModelProviders p3 = new ModelProviders(3, "Qoqa3.ch", "www.qoqa3.ch", null);

        db.createProvider(p1);
        db.createProvider(p2);
        db.createProvider(p3);

        ModelProviders returnModelP1 = db.getProvider(p1.getName());
        ModelProviders returnModelP2 = db.getProvider(p2.getName());
        ModelProviders returnModelP3 = db.getProvider(p3.getName());

        assertEquals(p1.getProvider_id(), returnModelP1.getProvider_id());
        assertEquals(p1.getName(), returnModelP1.getName());
        assertEquals(p1.getFavicon_url(), returnModelP1.getFavicon_url());
        assertEquals(p1.getUrl(), returnModelP1.getUrl());

        assertEquals(p2.getProvider_id(), returnModelP2.getProvider_id());
        assertEquals(p2.getName(), returnModelP2.getName());
        assertEquals(p2.getFavicon_url(), returnModelP2.getFavicon_url());
        assertEquals(p2.getUrl(), returnModelP2.getUrl());

        assertEquals(p3.getProvider_id(), returnModelP3.getProvider_id());
        assertEquals(p3.getName(), returnModelP3.getName());
        assertEquals(p3.getFavicon_url(), returnModelP3.getFavicon_url());
        assertEquals(p3.getUrl(), returnModelP3.getUrl());
        
    }

    public void testGetAllDeals() throws Exception {
        ModelDeals d1 = new ModelDeals(1,1,"Titre 1", "Description 1", "http://www.image.com/monimage1.png", "http://www.image.com",99,120);
        ModelDeals d2 = new ModelDeals(2,2,"Titre 2", "Description 2", "http://www.image.com/monimage2.png", "http://www.image.com",112,199);
        ModelDeals d3 = new ModelDeals(3,3,"Titre 3", "Description 3", "http://www.image.com/monimage3.png", "http://www.image.com",45,90);

        db.createDeal(d1);
        db.createDeal(d2);
        db.createDeal(d3);

        List<ModelDeals> listDeals = db.getAllDeals();

        ModelDeals returnD1 = listDeals.get(0);

        assertEquals(d1.getDeal_id(), returnD1.getDeal_id());
        assertEquals(d1.getDescription(), returnD1.getDescription());
        assertEquals(d1.getFk_provider_id(), returnD1.getFk_provider_id());
        assertEquals(d1.getImage_url(), returnD1.getImage_url());
        assertEquals(d1.getLink(), returnD1.getLink());
        assertEquals(d1.getOld_price(), returnD1.getOld_price());
        assertEquals(d1.getPrice(), returnD1.getPrice());
        assertEquals(d1.getTitle(), returnD1.getTitle());
    }
}