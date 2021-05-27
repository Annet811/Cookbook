package ru.myitschool.vsu2021.tribunskikh_a_o.cookbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class CookbookStorage {
    private static final String DATABASE_NAME = "cookbook.db";
    private static final int DATABASE_VERSION = 1;

    /*recepies*/
    private static final String RECIPIES_TABLE_NAME = "Recipetable";

    private static final String RECIPIES_COLUMN_ID = "id";
    private static final String RECIPIES_COLUMN_NAME = "Name";
    private static final String RECIPIES_COLUMN_DESCRIPTION = "Description";

    private static final int NUM_RECIPIES_COLUMN_ID = 0;
    private static final int NUM_RECIPIES_COLUMN_NAME = 1;
    private static final int NUM_RECIPIES_COLUMN_DESCRIPTION = 2;


    private SQLiteDatabase mDataBase;

    public CookbookStorage (Context context) {
        OpenHelper mOpenHelper = new OpenHelper(context);
        mDataBase = mOpenHelper.getWritableDatabase();
    }

    public long insert(String name,String description) {
        ContentValues cv=new ContentValues();
        cv.put(INGRIDIENTS_COLUMN_NAME, name);
        cv.put(RECIPIES_COLUMN_DESCRIPTION, description);
        return mDataBase.insert(RECIPIES_TABLE_NAME, null, cv);
    }
    public void insert (Recipe r) {
        r.setId( insert(r.getName(), r.getDescription()));
    }

    public int update(Recipe r) {
        ContentValues cv=new ContentValues();
        cv.put(INGRIDIENTS_COLUMN_NAME, r.getName());
        cv.put(RECIPIES_COLUMN_DESCRIPTION, r.getDescription());
        return mDataBase.update(RECIPIES_TABLE_NAME, cv, INGRIDIENTS_COLUMN_ID + " = ?",new String[] { String.valueOf(r.getId())});
    }

    public void deleteAllRecipies() {
        mDataBase.delete(RECIPIES_TABLE_NAME, null, null);
    }

    public void deleteRecipe(long id) {
        mDataBase.delete(RECIPIES_TABLE_NAME, INGRIDIENTS_COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public Recipe selectRecipe(long id) {
        Cursor mCursor = mDataBase.query(RECIPIES_TABLE_NAME, null, INGRIDIENTS_COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        mCursor.moveToFirst();
        if (mCursor.isAfterLast())
            return null;
        String name = mCursor.getString(NUM_INGRIDIENTS_COLUMN_NAME);
        String description = mCursor.getString(NUM_RECIPIES_COLUMN_DESCRIPTION);
        return new Recipe(id, name, description);
    }

    public ArrayList<Recipe> selectAllRecipe() {
        Cursor mCursor = mDataBase.query(RECIPIES_TABLE_NAME, null, null, null, null, null, null);

        ArrayList<Recipe> arr = new ArrayList<Recipe>();
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                long id = mCursor.getLong(NUM_RECIPIES_COLUMN_ID);
                String name = mCursor.getString(NUM_RECIPIES_COLUMN_NAME);
                String description = mCursor.getString(NUM_RECIPIES_COLUMN_DESCRIPTION);
                arr.add(new Recipe(id, name, description));
            } while (mCursor.moveToNext());
        }
        return arr;
    }
    public void reNameRecipe(long id, String name){
        ContentValues cv = new ContentValues();
        cv.put(RECIPIES_COLUMN_NAME, name);
        mDataBase.update(RECIPIES_TABLE_NAME, cv, RECIPIES_COLUMN_ID + "=?", new String [] {String.valueOf(id)});
    }
    public void delete(long id) {
        mDataBase.delete(RECIPIES_TABLE_NAME, RECIPIES_COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
    }
    public void changeDescription(long id, String description) {
        ContentValues cv = new ContentValues();
        cv.put(RECIPIES_COLUMN_DESCRIPTION, description);
        mDataBase.update(RECIPIES_TABLE_NAME, cv, RECIPIES_COLUMN_ID + "=?", new String[] {String.valueOf(id)});
    }
    public void deleteDescription(long id) {
        mDataBase.delete(RECIPIES_TABLE_NAME, RECIPIES_COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
    }


    /*ingridients*/
    private static final String INGRIDIENTS_TABLE_NAME = "tableMatches";

    private static final String INGRIDIENTS_COLUMN_ID = "id";
    private static final String INGRIDIENTS_COLUMN_NAME = "Name";

    private static final int NUM_INGRIDIENTS_COLUMN_ID = 0;
    private static final int NUM_INGRIDIENTS_COLUMN_NAME = 1;



    public long insert(String name) {
        ContentValues cv=new ContentValues();
        cv.put(INGRIDIENTS_COLUMN_NAME, name);
        return mDataBase.insert(INGRIDIENTS_TABLE_NAME, null, cv);
    }
    public void insert (Ingridient i) { i.setId( insert(i.getName()));}

    public int update(Ingridient i) {
        ContentValues cv=new ContentValues();
        cv.put(INGRIDIENTS_COLUMN_NAME, i.getName());
        return mDataBase.update(INGRIDIENTS_TABLE_NAME, cv, INGRIDIENTS_COLUMN_ID + " = ?",new String[] { String.valueOf(i.getId())});
    }

    public void deleteAllIngridients() {
        mDataBase.delete(INGRIDIENTS_TABLE_NAME, null, null);
    }

    public void deleteIngridient(long id) {
        mDataBase.delete(INGRIDIENTS_TABLE_NAME, INGRIDIENTS_COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public Ingridient selectIngridient(long id) {
        Cursor mCursor = mDataBase.query(INGRIDIENTS_TABLE_NAME, null, INGRIDIENTS_COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        mCursor.moveToFirst();
        if (mCursor.isAfterLast())
            return null;
        String name = mCursor.getString(NUM_INGRIDIENTS_COLUMN_NAME);
        return new Ingridient(id, name);
    }

    public ArrayList<Ingridient> selectAllIngridient() {
        Cursor mCursor = mDataBase.query(INGRIDIENTS_TABLE_NAME, null, null, null, null, null, null);

        ArrayList<Ingridient> arr = new ArrayList<Ingridient>();
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                long id = mCursor.getLong(NUM_INGRIDIENTS_COLUMN_ID);
                String name = mCursor.getString(NUM_INGRIDIENTS_COLUMN_NAME);
                arr.add(new Ingridient(id, name));
            } while (mCursor.moveToNext());
        }
        return arr;
    }


    /*general*/
    private static final String GENERAL_TABLE_NAME = "IngridientsInRecipes";

    private static final String GENERAL_COLUMN_ID = "Id";
    private static final String GENERAL_COLUMN_INGRIDIENT_ID = "IngridientId";
    private static final String GENERAL_COLUMN_RECIPE_ID = "RecipeId";
    private static final String GENERAL_COLUMN_QUANTITY = "Quantity";


    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_INGRIDIENT_COLUMN_ID = 1;
    private static final int NUM_RECIPE_COLUMN_ID = 2;
    private static final int NUM_COLUMN_QUANTITY = 3;

    public void addIngridientToRecipe( long ingridientId, long recipeId, String quantity){
        ContentValues cv=new ContentValues();
        cv.put(GENERAL_COLUMN_QUANTITY, quantity);
        cv.put(GENERAL_COLUMN_INGRIDIENT_ID, ingridientId);
        cv.put(GENERAL_COLUMN_RECIPE_ID, recipeId);
        mDataBase.insert(GENERAL_TABLE_NAME, null, cv);
    }
    public ArrayList<IngridientInRecipe> selectAllIngridientInRecipe(long recipeId) {
        Cursor mCursor = mDataBase.query(GENERAL_TABLE_NAME, null, GENERAL_COLUMN_RECIPE_ID + " = ?", new String[]{String.valueOf(recipeId)}, null, null, null);

        ArrayList<IngridientInRecipe> arr = new ArrayList<IngridientInRecipe>();
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                long id = mCursor.getLong(NUM_COLUMN_ID);
                long ingridientId = mCursor.getLong(NUM_INGRIDIENT_COLUMN_ID);
                String quantity = mCursor.getString(NUM_COLUMN_QUANTITY);
                arr.add(new IngridientInRecipe(id, ingridientId, recipeId, quantity));
            } while (mCursor.moveToNext());
        }
        return arr;
    }

    public void changeQuantity(long id, String value) {
        ContentValues cv = new ContentValues();
        cv.put(GENERAL_COLUMN_QUANTITY, value);
        mDataBase.update(GENERAL_TABLE_NAME, cv, GENERAL_COLUMN_ID + " =?", new String[] {String.valueOf(id)});
    }
    public void deleteIngridientInRecipe(long id) {
        mDataBase.delete(GENERAL_TABLE_NAME, GENERAL_COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
    }

    
    private class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            String recipeQuery = "CREATE TABLE IF NOT EXISTS "  + RECIPIES_TABLE_NAME + " (" +
                    RECIPIES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    RECIPIES_COLUMN_NAME + " TEXT, " +
                    RECIPIES_COLUMN_DESCRIPTION + " TEXT);";
            db.execSQL(recipeQuery);
            String ingridientQuery = "CREATE TABLE IF NOT EXISTS " + INGRIDIENTS_TABLE_NAME + " (" +
                    INGRIDIENTS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    INGRIDIENTS_COLUMN_NAME + " TEXT);";
            db.execSQL(ingridientQuery);
            String generalQuery = "CREATE TABLE IF NOT EXISTS " + GENERAL_TABLE_NAME + " (" +
                    GENERAL_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    GENERAL_COLUMN_INGRIDIENT_ID + " INTEGER, " +
                    GENERAL_COLUMN_RECIPE_ID + " INTEGER, " +
                    GENERAL_COLUMN_QUANTITY + " TEXT);";
            db.execSQL(generalQuery);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + RECIPIES_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + INGRIDIENTS_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + GENERAL_TABLE_NAME);
            onCreate(db);
        }
    }
}

