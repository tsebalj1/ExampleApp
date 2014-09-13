package com.repsly.tomislav.applicationexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Tomislav on 12.9.2014..
 */
public class ExampleDatabaseAdapter {

   ExampleHelper exampleHelper;

    public ExampleDatabaseAdapter(Context context){

        exampleHelper = new ExampleHelper(context);
    }

    public long inputUsername(String name){

        SQLiteDatabase exampleDatabase = exampleHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ExampleHelper.NAME, name);
        long id = exampleDatabase.insert(ExampleHelper.TABLE_NAME, ExampleHelper.ADDRESS ,contentValues);
        return id;
    }

    /**
     * postavlja sve id u 0 i potom briše sve iz liste. postavljanje u 0 bitno kod postavljanja layouta na loginu
     * @return vraca broj obrisanih usera
     */
    public int deleteAll(){


        SQLiteDatabase exampleDatabase = exampleHelper.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(BetaPlusHelper.ID, 0);
//
//        betaPlusDatabase.update(BetaPlusHelper.TABLE_NAME, contentValues,null,null);
        int count = exampleDatabase.delete(ExampleHelper.TABLE_NAME,null,null);
        return count;
    }

    /**
     * mjenja staro ime lokacije sa određenim id-em za novo
     * @param id je id lokacije kojemu se hoce promjeniti ime
     * @param name je novo ime
     * @return broj lokacija kojima se promjenilo ime
     */
    public int updateLocationNameById(long id,  String name){

        SQLiteDatabase exampleDatabase = exampleHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ExampleHelper.NAME, name);
        String[] arguments = {Long.toString(id)};
        int count = exampleDatabase.update(ExampleHelper.TABLE_NAME, contentValues, ExampleHelper.ID + " =?", arguments);
        return count;
    }

    /**
     * mjenja staru adresu lokacije sa određenim id-em za novu
     * @param id je id lokacije kojoj se mjenja adresa
     * @param address je String sa novom adresom
     * @return broj lokacija kojima se promjenila adresa
     */
    public int updateAddressById(long id,  String address){

        SQLiteDatabase exampleDatabase = exampleHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ExampleHelper.ADDRESS, address);
        String[] arguments = {Long.toString(id)};
        int count = exampleDatabase.update(ExampleHelper.TABLE_NAME, contentValues, ExampleHelper.ID + " =?", arguments);
        return count;
    }

    public int updateLatitudeById(long id,  String lat){

        SQLiteDatabase exampleDatabase = exampleHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ExampleHelper.LATITUDE, lat);
        String[] arguments = {Long.toString(id)};
        int count = exampleDatabase.update(ExampleHelper.TABLE_NAME, contentValues, ExampleHelper.ID + " =?", arguments);
        return count;
    }

    public int updateLongitudeById(long id,  String lng){

        SQLiteDatabase exampleDatabase = exampleHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ExampleHelper.LONGITUDE, lng);
        String[] arguments = {Long.toString(id)};
        int count = exampleDatabase.update(ExampleHelper.TABLE_NAME, contentValues, ExampleHelper.ID + " =?", arguments);
        return count;
    }


    /**
     * kreira jedan novi red u bazi, tj jedna nova lokacija sa svim null atributima osim autoincrement index-a
     * @return vraca id nove lokacije
     */
    public long inputData(){

        SQLiteDatabase exampleDatabase = exampleHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long id = exampleDatabase.insert(ExampleHelper.TABLE_NAME, ExampleHelper.NAME , contentValues);
        return id;
    }

    /**
     * pretražuje bazu i vraca ime posljednje lokacije u bazi
     * @return vraca ime posljednje lokacije u bazi
     */
    public String getLocationName(){

        SQLiteDatabase exampleDatabase = exampleHelper.getWritableDatabase();
        String[] columns = {ExampleHelper.NAME};
        Cursor cursor = exampleDatabase.query(ExampleHelper.TABLE_NAME, columns, null, null, null, null, null);

        String name = new String() ;
        while(cursor.moveToNext()){
            int nameIndex = cursor.getColumnIndex(ExampleHelper.NAME);
            name = cursor.getString(nameIndex);
        }
        return name;
    }

    public String getAddress(){

        SQLiteDatabase exampleDatabase = exampleHelper.getWritableDatabase();
        String[] columns = {ExampleHelper.ADDRESS};
        Cursor cursor = exampleDatabase.query(ExampleHelper.TABLE_NAME, columns, null, null, null, null, null);

        String address = new String() ;
        while(cursor.moveToNext()){
            int addressIndex = cursor.getColumnIndex(ExampleHelper.ADDRESS);
            address = cursor.getString(addressIndex);
        }
        return address;
    }

    public String getLatitude(){

        SQLiteDatabase exampleDatabase = exampleHelper.getWritableDatabase();
        String[] columns = {ExampleHelper.LATITUDE};
        Cursor cursor = exampleDatabase.query(ExampleHelper.TABLE_NAME, columns, null, null, null, null, null);

        String latitude = new String() ;
        while(cursor.moveToNext()){
            int latitudeIndex = cursor.getColumnIndex(ExampleHelper.LATITUDE);
            latitude = cursor.getString(latitudeIndex);
        }
        return latitude;
    }

    public String getLongitude(){

        SQLiteDatabase exampleDatabase = exampleHelper.getWritableDatabase();
        String[] columns = {ExampleHelper.LONGITUDE};
        Cursor cursor = exampleDatabase.query(ExampleHelper.TABLE_NAME, columns, null, null, null, null, null);

        String longitude = new String() ;
        while(cursor.moveToNext()){
            int longitudeIndex = cursor.getColumnIndex(ExampleHelper.LONGITUDE);
            longitude = cursor.getString(longitudeIndex);
        }
        return longitude;
    }

    /**
     * pretražuje bazu i vraca index posljednje lokacije u bazi
     * @return vraca index posljednje lokacije u bazi
     */
    public int getIndex(){ //vraca index zadnjeg korisnika u bazi

        SQLiteDatabase exampleDatabase = exampleHelper.getWritableDatabase();
        String[] columns = {ExampleHelper.ID};
        Cursor cursor = exampleDatabase.query(ExampleHelper.TABLE_NAME, columns, null, null, null, null, null);

        int uid = 0;
        while(cursor.moveToNext()){
            int idIndex = cursor.getColumnIndex(ExampleHelper.ID);
            uid = cursor.getInt(idIndex);
        }
        return uid;
    }

    /**
     * pretražuje bazu i vraca sve podatke o svim lokacijama u bazi
     * @return sve podatke o svim lokacijama u bazi
     */
    public String getAllData(){

        SQLiteDatabase exampleDatabase = exampleHelper.getWritableDatabase();

        String[] columns = {ExampleHelper.ID, ExampleHelper.NAME, ExampleHelper.ADDRESS, ExampleHelper.LATITUDE,
                ExampleHelper.LONGITUDE};
        Cursor cursor = exampleDatabase.query(ExampleHelper.TABLE_NAME, columns, null, null, null, null, null);
        StringBuilder sb = new StringBuilder();

        while(cursor.moveToNext()){

            int idIndex = cursor.getColumnIndex(ExampleHelper.ID);
            int nameIndex = cursor.getColumnIndex(ExampleHelper.NAME);
            int addressIndex = cursor.getColumnIndex(ExampleHelper.ADDRESS);
            int latitudeIndex = cursor.getColumnIndex(ExampleHelper.LATITUDE);
            int longitudeIndex = cursor.getColumnIndex(ExampleHelper.LONGITUDE);


            int uid = cursor.getInt(idIndex);
            String name = cursor.getString(nameIndex);
            String address = cursor.getString(addressIndex);
            String latitude = cursor.getString(latitudeIndex);
            String longitude = cursor.getString(longitudeIndex);


            sb.append(uid+ " " +name+ " " +address+ " " +latitude+ " " +longitude+ "\n");

        }
        return sb.toString();
    }

    class ExampleHelper extends SQLiteOpenHelper{

        private static final String DATABASE_NAME = "exampledatabase";
        private static final String TABLE_NAME = "EXAMPLETABLE";
        private static final String ID = "_id";
        private static final String NAME = "name";
        private static final String ADDRESS = "address";
        private static final String LATITUDE = "latitude";
        private static final String LONGITUDE = "longitude";
        private static final int DATABASE_VERSION = 1;

        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NAME + " VARCHAR(30)," + ADDRESS + " VARCHAR(30)," + LATITUDE + " VARCHAR(30)," + LONGITUDE + " VARCHAR(30));";
        private static final String DROP_TABLE =" DROP TABLE IF EXISTS " + TABLE_NAME;

        private Context context;
        public ExampleHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;

        }

        @Override
        public void onCreate(SQLiteDatabase exampleDatabase) {

            try {
                exampleDatabase.execSQL(CREATE_TABLE);
                Toast.makeText(context, "pozvao se onCreate", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase exampleDatabase, int i, int i2) {

            try {
                exampleDatabase.execSQL(DROP_TABLE);
                onCreate(exampleDatabase);
                Toast.makeText(context, "pozvao se onUpgrade", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
