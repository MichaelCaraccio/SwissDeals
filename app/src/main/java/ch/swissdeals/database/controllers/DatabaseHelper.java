package ch.swissdeals.database.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ch.swissdeals.database.models.ModelDeals;
import ch.swissdeals.database.models.ModelProviders;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = DatabaseHelper.class.getSimpleName();

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "swissdealsdb";

    // Table Names
    private static final String TABLE_DEALS = "tbl_deals";
    private static final String TABLE_PROVIDERS = "tbl_providers";

    // Deals Table - column names
    private static final String KEY_DEALS_ID = "deal_id";
    private static final String KEY_DEALS_FK_PROVIDER_ID = "fk_provider_id";
    private static final String KEY_DEALS_TITLE = "title";
    private static final String KEY_DEALS_DESCRIPTION = "description";
    private static final String KEY_DEALS_IMAGE_URL = "image_url";
    private static final String KEY_DEALS_LINK = "link";
    private static final String KEY_DEALS_PRICE = "price";
    private static final String KEY_DEALS_OLD_PRICE = "old_price";

    // Providers Table - column names
    private static final String KEY_PROVIDERS_ID = "provider_id";
    private static final String KEY_PROVIDERS_NAME = "name";
    private static final String KEY_PROVIDERS_URL = "url";
    private static final String KEY_PROVIDERS_FAVICON_URL = "favicon_url";
    private static final String KEY_PROVIDERS_SUBSCRIBED = "subscribed";

    // Table Create Statements
    private static final String CREATE_TABLE_DEALS = "CREATE TABLE " + TABLE_DEALS + " ( "
            + KEY_DEALS_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, "
            + KEY_DEALS_FK_PROVIDER_ID + " INTEGER NOT NULL, "
            + KEY_DEALS_TITLE + " TEXT NOT NULL, "
            + KEY_DEALS_DESCRIPTION + " TEXT, "
            + KEY_DEALS_IMAGE_URL + " TEXT, "
            + KEY_DEALS_LINK + " TEXT NOT NULL, "
            + KEY_DEALS_PRICE + " REAL NOT NULL, "
            + KEY_DEALS_OLD_PRICE + " REAL, "
            + " FOREIGN KEY(" + KEY_DEALS_FK_PROVIDER_ID + " ) REFERENCES " + TABLE_PROVIDERS + "( " + KEY_PROVIDERS_ID + " ))";

    private static final String CREATE_TABLE_PROVIDERS = "CREATE TABLE " + TABLE_PROVIDERS + " ( "
            + KEY_PROVIDERS_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, "
            + KEY_PROVIDERS_NAME + " TEXT NOT NULL UNIQUE, "
            + KEY_PROVIDERS_URL + " TEXT NOT NULL, "
            + KEY_PROVIDERS_FAVICON_URL + " TEXT, "
            + KEY_PROVIDERS_SUBSCRIBED + " INTEGER)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_DEALS);
        db.execSQL(CREATE_TABLE_PROVIDERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEALS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROVIDERS);

        // create new tables
        onCreate(db);
    }

    /**
     * Close Database
     */
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    // *********************************************************************************************
    // DEALS CRUD
    // *********************************************************************************************

    /**
     * Create deal
     *
     * @param deal
     * @return long deal_id
     */
    public synchronized long createDeal(ModelDeals deal) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DEALS_FK_PROVIDER_ID, deal.getFk_provider_id());
        values.put(KEY_DEALS_TITLE, deal.getTitle());
        values.put(KEY_DEALS_DESCRIPTION, deal.getDescription());
        values.put(KEY_DEALS_IMAGE_URL, deal.getImage_url());
        values.put(KEY_DEALS_LINK, deal.getLink());
        values.put(KEY_DEALS_PRICE, deal.getPrice());
        values.put(KEY_DEALS_OLD_PRICE, deal.getOld_price());

        // insert row - return confirmation id
        return db.insert(TABLE_DEALS, null, values);
    }

    /**
     * Get deal
     *
     * @param deal_id
     * @return ModelDeals
     */
    public synchronized ModelDeals getDeal(long deal_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = String.format("SELECT * FROM %s WHERE %s = %d", TABLE_DEALS, KEY_DEALS_ID, deal_id);

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        return getModelDeal(c);
    }

    /**
     * Get all deals
     *
     * @return List of ModelDeals
     */
    public synchronized List<ModelDeals> getAllDeals() {
        String selectQuery = "SELECT * FROM " + TABLE_DEALS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        return getModelDeals(c);
    }

    /**
     * Get deals from provider name
     *
     * @param provider_name
     * @return List of ModelDeals
     */
    public synchronized List<ModelDeals> getDealsFromProvider(String provider_name) {

        // Get provider id by name
        long provider_id = getProviderIDFromName(provider_name);

        // If providers'id is found
        if (provider_id != -1) {

            List<ModelDeals> deals = new ArrayList<ModelDeals>();
            String selectQuery = "SELECT * FROM " + TABLE_DEALS + " WHERE "
                    + KEY_DEALS_FK_PROVIDER_ID + " = " + provider_id;

            Log.e(LOG, selectQuery);

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            return getModelDeals(c);
        }

        // TODO si retourne null cela signifie que le provider_name n'est pas inscrit dans la base
        // de donnée. Peut-être qu'il est mal orthographié?
        return null;
    }

    /**
     * Update Deal
     *
     * @param deal
     * @return
     */
    public synchronized int updateDeal(ModelDeals deal) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DEALS_FK_PROVIDER_ID, deal.getFk_provider_id());
        values.put(KEY_DEALS_TITLE, deal.getTitle());
        values.put(KEY_DEALS_DESCRIPTION, deal.getDescription());
        values.put(KEY_DEALS_IMAGE_URL, deal.getImage_url());
        values.put(KEY_DEALS_LINK, deal.getLink());
        values.put(KEY_DEALS_PRICE, deal.getPrice());
        values.put(KEY_DEALS_OLD_PRICE, deal.getOld_price());

        // updating row
        return db.update(TABLE_DEALS, values, KEY_DEALS_ID + " = ?",
                new String[]{String.valueOf(deal.getDeal_id())});
    }

    /**
     * Delete deal
     *
     * @param deal_id
     */
    public synchronized void deleteDeal(long deal_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DEALS, KEY_DEALS_ID + " = ?",
                new String[]{String.valueOf(deal_id)});
    }

    public synchronized void deleteAllDeals() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_DEALS);
    }


    // *********************************************************************************************
    // PROVIDERS CRUD
    // *********************************************************************************************

    /**
     * Create provider
     *
     * @param provider
     * @return long provider_id
     */
    public synchronized long createProvider(ModelProviders provider) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PROVIDERS_NAME, provider.getName());
        values.put(KEY_PROVIDERS_URL, provider.getUrl());
        values.put(KEY_PROVIDERS_FAVICON_URL, provider.getFavicon_url());
        values.put(KEY_PROVIDERS_SUBSCRIBED, provider.isUserSubscribed());

        // insert row - return confirmation id
        return db.insert(TABLE_PROVIDERS, null, values);
    }

    /**
     * Get provider
     *
     * @param provider_name
     * @return ModelProviders
     */
    public synchronized ModelProviders getProvider(String provider_name) {
        int providerId = getProviderIDFromName(provider_name);
        return getProvider(providerId);
    }

    /**
     * Get provider with id
     *
     * @param provider_id
     * @return ModelProviders
     */
    public synchronized ModelProviders getProvider(int provider_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_PROVIDERS + " WHERE "
                + KEY_PROVIDERS_ID + " = " + '"' + provider_id + '"';

        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        //TODO: check if c.getCount() > 0 ?
        if (c != null)
            c.moveToFirst();

        return getModelProvider(c);
    }

    public synchronized List<ModelProviders> getSubscribedProviders() {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_PROVIDERS + " WHERE "
                + KEY_PROVIDERS_SUBSCRIBED + " != 0 ";

        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        return getModelProviders(c);
    }

    public synchronized List<ModelProviders> getUnsubscribedProviders() {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_PROVIDERS + " WHERE "
                + KEY_PROVIDERS_SUBSCRIBED + " = 0 ";

        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        return getModelProviders(c);
    }

    public void subscribeProvider(String providerName) {
        int providerId = getProviderIDFromName(providerName);
        subscribeProvider(providerId);
    }

    public void subscribeProvider(int providerId) {
        updateSubscribed(providerId, true);
    }

    public void unsubscribeProvider(String providerName) {
        int providerId = getProviderIDFromName(providerName);
        unsubscribeProvider(providerId);
    }

    public void unsubscribeProvider(int providerId) {
        updateSubscribed(providerId, false);
    }

    /**
     * Get provider
     *
     * @param provider_name
     * @return ModelProviders
     */
    public synchronized int getProviderIDFromName(String provider_name) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT " + KEY_PROVIDERS_ID + " FROM " + TABLE_PROVIDERS + " WHERE "
                + KEY_PROVIDERS_NAME + " = '" + provider_name + "'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        return c != null && c.getCount() > 0 ? c.getInt(c.getColumnIndex(KEY_PROVIDERS_ID)) : ModelProviders.DEFAULT_ID;
    }

    // TODO Compter le nombre de d'inscription

    public synchronized String getProviderNameFromID(int provider_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT " + KEY_PROVIDERS_NAME + " FROM " + TABLE_PROVIDERS + " WHERE "
                + KEY_PROVIDERS_ID + " = '" + provider_id + "'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        return c != null && c.getCount() > 0 ? c.getString(c.getColumnIndex(KEY_PROVIDERS_NAME)) : String.valueOf(ModelProviders.DEFAULT_ID);
    }

    /**
     * Get all providers
     *
     * @return List of ModelProviders
     */
    public synchronized List<ModelProviders> getAllProviders() {
        String selectQuery = "SELECT * FROM " + TABLE_PROVIDERS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        return getModelProviders(c);
    }

    /**
     * Update ModelProviders
     *
     * @param provider
     * @return
     */
    public synchronized int updateProvider(ModelProviders provider) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PROVIDERS_NAME, provider.getName());
        values.put(KEY_PROVIDERS_URL, provider.getUrl());
        values.put(KEY_PROVIDERS_FAVICON_URL, provider.getFavicon_url());

        /*
        We specifically don't want to update the subscribed column.
        To do it, you must use the ProviderManager class or the (un)subscribe methods of this class.
        */
        //values.put(KEY_PROVIDERS_SUBSCRIBED, provider.isUserSubscribed());

        // updating row
        return db.update(TABLE_PROVIDERS, values, KEY_PROVIDERS_ID + " = ?",
                new String[]{String.valueOf(provider.getProvider_id())});
    }

    public synchronized long createOrUpdateProvider(ModelProviders provider) {
        int id = getProviderIDFromName(provider.getName());
        Log.d(LOG, "providerID: " + id);
        if (id == ModelProviders.DEFAULT_ID) {
            Log.d(LOG, "create");
            return createProvider(provider);
        } else {
            Log.d(LOG, "update");
            return updateProvider(provider);
        }
    }

    /**
     * Delete provider
     *
     * @param provider_name
     */
    public synchronized void deleteProvider(String provider_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PROVIDERS, KEY_PROVIDERS_ID + " = ?",
                new String[]{provider_name});
    }

    /**
     * Delete providers from list. If cascadeRemove is set to true, deals will be deleted too.
     *
     * @param listProviderName
     * @param cascadeRemove
     */
    public synchronized void deleteProviders(List<String> listProviderName, boolean cascadeRemove) {
        if (listProviderName.isEmpty())
            return;

        SQLiteDatabase db = this.getWritableDatabase();

        StringBuilder listName = new StringBuilder();

        // Concatenate providers in the same string
        String delimiter = "'";
        for (String name : listProviderName) {
            listName.append(delimiter).append(name);
            delimiter = "','";
        }
        listName.append("'");

        if (cascadeRemove) {

            StringBuilder listNameID = new StringBuilder();

            // Concatenate providers in the same string
            delimiter = "'";
            for (String name : listProviderName) {
                listNameID.append(delimiter).append(Integer.toString(getProviderIDFromName(name)));
                delimiter = "','";
            }
            listNameID.append("'");

            String queryCascade = String.format("DELETE FROM %s WHERE %s IN (%s)", TABLE_DEALS, KEY_DEALS_FK_PROVIDER_ID, listNameID);
            db.execSQL(queryCascade);
        }

        String queryDelete = String.format("DELETE FROM %s WHERE %s IN (%s)", TABLE_PROVIDERS, KEY_PROVIDERS_NAME, listName);
        db.execSQL(queryDelete);
    }

    public void deleteAllProvidersExcept(List<String> providersToKeep, boolean cascadeRemove) {
        List<String> providerToRemove = new ArrayList<>();
        providerToRemove.addAll(getAllProvidersName());
        providerToRemove.removeAll(providersToKeep);

        deleteProviders(providerToRemove, cascadeRemove);
    }


    // *********************************************************************************************
    // PRIVATE METHODS
    // *********************************************************************************************

    /**
     * Warning only works if SELECT * XXXX is used because we rely on the column's position
     * in the query
     *
     * @param c cursor on a SELECT * XXXX
     * @return true if the user is subscribed for this provider, false otherwise
     */
    private boolean isUserSubscribed(Cursor c) {
        boolean isUserSubscribed = false;
        int subscribedCol = c.getColumnIndex(KEY_PROVIDERS_SUBSCRIBED);
        if (!c.isNull(subscribedCol) && c.getInt(subscribedCol) != 0) {
            isUserSubscribed = true;
        }
        return isUserSubscribed;
    }

    private void updateSubscribed(int providerId, boolean isUserSubscribed) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        int isUserSubscribedInteger = isUserSubscribed ? 1 : 0;
        values.put(KEY_PROVIDERS_SUBSCRIBED, isUserSubscribedInteger);

        db.update(TABLE_PROVIDERS, values, KEY_PROVIDERS_ID + " = ?",
                new String[]{String.valueOf(providerId)});
    }

    private List<ModelProviders> getModelProviders(Cursor c) {
        List<ModelProviders> providers = new ArrayList<>();

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                ModelProviders td = getModelProvider(c);
                providers.add(td);
            } while (c.moveToNext());
        }
        return providers;
    }

    private ModelProviders getModelProvider(Cursor c) {
        ModelProviders td = new ModelProviders();
        td.setProvider_id(c.getInt(c.getColumnIndex(KEY_PROVIDERS_ID)));
        td.setName(c.getString(c.getColumnIndex(KEY_PROVIDERS_NAME)));
        td.setUrl(c.getString(c.getColumnIndex(KEY_PROVIDERS_URL)));
        td.setFavicon_url(c.getString(c.getColumnIndex(KEY_PROVIDERS_FAVICON_URL)));

        boolean isUserSubscribed = isUserSubscribed(c);
        td.setUserSubscribed(isUserSubscribed);
        return td;
    }


    private List<ModelDeals> getModelDeals(Cursor c) {
        List<ModelDeals> deals = new ArrayList<>();

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                ModelDeals md = getModelDeal(c);
                deals.add(md);
            } while (c.moveToNext());
        }
        return deals;
    }

    private ModelDeals getModelDeal(Cursor c) {
        ModelDeals td = new ModelDeals();
        assert c != null;
        td.setDeal_id(c.getInt(c.getColumnIndex(KEY_DEALS_ID)));
        td.setFk_provider_id(c.getInt(c.getColumnIndex(KEY_DEALS_FK_PROVIDER_ID)));
        td.setTitle(c.getString(c.getColumnIndex(KEY_DEALS_TITLE)));
        td.setDescription(c.getString(c.getColumnIndex(KEY_DEALS_DESCRIPTION)));
        td.setImage_url(c.getString(c.getColumnIndex(KEY_DEALS_IMAGE_URL)));
        td.setLink(c.getString(c.getColumnIndex(KEY_DEALS_LINK)));
        td.setPrice(c.getInt(c.getColumnIndex(KEY_DEALS_PRICE)));
        td.setOld_price(c.getInt(c.getColumnIndex(KEY_DEALS_OLD_PRICE)));

        return td;
    }

    private List<String> getAllProvidersName() {
        List<String> allProvidersName = new ArrayList<>();

        String selectQuery = "SELECT " + KEY_PROVIDERS_NAME + " FROM " + TABLE_PROVIDERS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                String providerName = c.getString(c.getColumnIndex(KEY_PROVIDERS_NAME));
                allProvidersName.add(providerName);
            } while (c.moveToNext());
        }

        return allProvidersName;
    }
}