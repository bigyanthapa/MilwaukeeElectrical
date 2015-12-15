package bigyan.com.milwaukeeelectrical.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import bigyan.com.milwaukeeelectrical.model.Model;
import bigyan.com.milwaukeeelectrical.utils.AlertDialog;

/**
 * Created by bigyanthapa on 12/14/15.
 */
public class SQliteHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "milwaukee.db";

    // search data table name
    private static final String TABLE_WEATHER_DATA = "weatherData";

    // searchData Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_CITY = "cityName";

    public SQliteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_WEATHER_TABLE = "CREATE TABLE " + TABLE_WEATHER_DATA + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CITY + " TEXT"+")";
        db.execSQL(CREATE_WEATHER_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER_DATA);

        // Create tables again
        onCreate(db);
    }


    // Adding new search
    public void addCity(Context context, String city) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (!checkIfExist(city)) {
            ContentValues values = new ContentValues();
            values.put(KEY_CITY, city);
            // Inserting Row
            db.insert(TABLE_WEATHER_DATA, null, values);
            db.close(); // Closing database connection
        } else {
            AlertDialog alertDialog = new AlertDialog();
            alertDialog.redundantDataAlert(context, "Redundant Data", "The City yo are trying to insert already exists.");
        }


    }

    public List<Model> getAllSearch() {
        List<Model> weatherList = new ArrayList<Model>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_WEATHER_DATA;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Model weatherModel = new Model();

                weatherModel.setName(cursor.getString(cursor.getColumnIndex(KEY_CITY)));

                // Adding descriptionModel to list
                weatherList.add(weatherModel);
            } while (cursor.moveToNext());
        }

        // return search list
        return weatherList;
    }

    public boolean checkIfExist(String city) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_WEATHER_DATA, new String[]{KEY_ID,
                        KEY_CITY}, KEY_CITY + "=?",
                new String[]{city}, null, null, null, null);


        if (cursor.getCount() > 0)
            return true;

        else
            return false;

    }

    public List<String> getAllCities(){
        List<String> searchList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_WEATHER_DATA;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                searchList.add(cursor.getString(cursor.getColumnIndex(KEY_CITY)));

            } while (cursor.moveToNext());
        }

        // return search list
        return searchList;
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_WEATHER_DATA);
        // db.execSQL("TRUNCATE table" + TABLE_SEARCHDATA);
        db.close();
    }


}
