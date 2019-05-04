package com.nyit.datausagemonitor;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class PackageInfoDatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "packageInfo.db";


    public static abstract class AllPackagesTable {

        public static final String TABLE_PACKAGES = "allpackages";
        public static final String COL_NAME = "nameforall";
        public static final String COL_UID = "uidforall";
        public static final String COL_LISTNAME = "listnameforall";

    }


    public static abstract class SelectedPackagesTable {

        public static final String TABLE_PACKAGES = "selectedpackages";
        public static final String COL_NAME = "nameforselected";
        public static final String COL_UID = "uidforselected";
        public static final String COL_LISTNAME = "listnameforselected";

    }



    private PackageInfoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static PackageInfoDatabaseHelper sInstance;

    public static PackageInfoDatabaseHelper getInstance(Context context){
        if(sInstance == null){
            sInstance = new PackageInfoDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }



    private static final String SQL_DROP_ALL_PACKAGES_TABLE = "DROP TABLE IF EXISTS "+ AllPackagesTable.TABLE_PACKAGES;

    private static final String SQL_DROP_SELECTED_PACKAGES_TABLE = "DROP TABLE IF EXISTS "+ SelectedPackagesTable.TABLE_PACKAGES;

    private static final String SQL_CREATE_ALL_PACKAGES_TABLE =
            "CREATE TABLE allpackages ("
                    + AllPackagesTable.COL_NAME+" TEXT PRIMARY KEY,"
                    + AllPackagesTable.COL_UID +" INTEGER,"
                    + AllPackagesTable.COL_LISTNAME + " TEXT"
                    +")";
    private static final String SQL_CREATE_SELECTED_PACKAGES_TABLE =
            "CREATE TABLE selectedpackages ("
                    + SelectedPackagesTable.COL_NAME+" TEXT PRIMARY KEY,"
                    + SelectedPackagesTable.COL_UID +" INTEGER,"
                    + SelectedPackagesTable.COL_LISTNAME + " TEXT"
                    +")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ALL_PACKAGES_TABLE);
        db.execSQL(SQL_CREATE_SELECTED_PACKAGES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DROP_ALL_PACKAGES_TABLE);
        db.execSQL(SQL_DROP_SELECTED_PACKAGES_TABLE);
        onCreate(db);
    }

    public void insertAllPackages(String packageName, int uID, String listname){
        // Get a reference to the database
        SQLiteDatabase db = getWritableDatabase();

        // create a new content value to store values
        ContentValues values = new ContentValues();
        values.put(AllPackagesTable.COL_NAME, packageName);
        values.put(AllPackagesTable.COL_UID, uID);
        values.put(AllPackagesTable.COL_LISTNAME, listname);

        // Insert the row into the allpackage table
        db.insert(AllPackagesTable.TABLE_PACKAGES, null, values);
        db.close();
    }

    public void insertSelectedPackages(String packageName, int uID, String listname){
        // Get a reference to the database
        SQLiteDatabase db = getWritableDatabase();

        // create a new content value to store values
        ContentValues values = new ContentValues();
        values.put(SelectedPackagesTable.COL_NAME, packageName);
        values.put(SelectedPackagesTable.COL_UID, uID);
        values.put(SelectedPackagesTable.COL_LISTNAME, listname);

        // Insert the row into the selectedpackage table
        db.insert(SelectedPackagesTable.TABLE_PACKAGES, null, values);
        db.close();
    }

    public void deletSelectedPackages(int uID){
        // Get a reference to the database
        SQLiteDatabase db = getWritableDatabase();

        // create a new content value to store values
        String selection = "uidforselected = ?";
        String [] selectionArgs = new String[] {String.valueOf(uID)};


        // Insert the row into the selectedpackage table
        db.delete(SelectedPackagesTable.TABLE_PACKAGES, selection,selectionArgs);
        db.close();
    }





    public PackageInfoItem getUid(String packageName){

        SQLiteDatabase db = getReadableDatabase();

        String[] projection = new String[]{ AllPackagesTable.COL_NAME, AllPackagesTable.COL_UID, AllPackagesTable.COL_LISTNAME};

        String selection = "nameforall = ?";

        String[] selectionArgs = new String[]{ packageName };

        Cursor cursor = db.query(AllPackagesTable.TABLE_PACKAGES, projection, selection, selectionArgs, null, null, null, null);

        if(cursor.moveToFirst()) {

            String name = cursor.getString(cursor.getColumnIndex(AllPackagesTable.COL_NAME));
            int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(AllPackagesTable.COL_UID)));
            String listname = cursor.getString(cursor.getColumnIndex(AllPackagesTable.COL_LISTNAME));
            return new PackageInfoItem(name,id,listname);
        }
        return null;

    }


    public boolean allPackagesTableIsEmpty() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(AllPackagesTable.TABLE_PACKAGES, null, null, null, null, null, null);
        boolean isEmpty = !cursor.moveToFirst();
        cursor.close();
        return isEmpty;
    }


    public boolean selectedPackagesTableIsEmpty() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(SelectedPackagesTable.TABLE_PACKAGES, null, null, null, null, null, null);
        boolean isEmpty = !cursor.moveToFirst();
        cursor.close();
        return isEmpty;
    }


    public List<PackageInfoItem> getAllPackageInfoItems(){
        List<PackageInfoItem> packageInfoItems = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(AllPackagesTable.TABLE_PACKAGES,
                null, null, null, null, null, null);
        if(cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {

                String name = cursor.getString(cursor.getColumnIndex(AllPackagesTable.COL_NAME));
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(AllPackagesTable.COL_UID)));
                String listname = cursor.getString(cursor.getColumnIndex(AllPackagesTable.COL_LISTNAME));

                packageInfoItems.add(new PackageInfoItem(name, id, listname));
                cursor.moveToNext();
            }
        }
        cursor.close();

        return packageInfoItems;
    }


    public List<PackageInfoItem> getSelectedPackageInfoItems(){
        List<PackageInfoItem> packageInfoItems = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(SelectedPackagesTable.TABLE_PACKAGES,
                null, null, null, null, null, null);
        if(cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {

                String name = cursor.getString(cursor.getColumnIndex(SelectedPackagesTable.COL_NAME));
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SelectedPackagesTable.COL_UID)));
                String listname = cursor.getString(cursor.getColumnIndex(SelectedPackagesTable.COL_LISTNAME));

                packageInfoItems.add(new PackageInfoItem(name, id, listname));
                cursor.moveToNext();
            }
        }
        cursor.close();

        return packageInfoItems;
    }


    public List<String> getListNamesFromSelectedPackageInfoItems(){
        List<String> selectedNames = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(SelectedPackagesTable.TABLE_PACKAGES,
                null, null, null, null, null, null);
        if(cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {

                String listname = cursor.getString(cursor.getColumnIndex(SelectedPackagesTable.COL_LISTNAME));

                selectedNames.add(listname);
                cursor.moveToNext();
            }
        }
        cursor.close();

        return selectedNames;
    }

    public List<Integer> getUidFromSelectedPackageInfoItems(){
        List<Integer> selectedNames = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(SelectedPackagesTable.TABLE_PACKAGES,
                null, null, null, null, null, null);
        if(cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {

                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(SelectedPackagesTable.COL_UID)));
                selectedNames.add(id);
                cursor.moveToNext();
            }
        }
        cursor.close();

        return selectedNames;
    }




}
