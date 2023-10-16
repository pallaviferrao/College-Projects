package com.example.pallavi.kshamata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
public class dbAdapter {
    static final String KEY_ROWID = "_id";
    static final String KEY_NAME = "name";
    static final String KEY_DOB = "dob";
    static final String KEY_AGE = "age";
    static final String KEY_PHONE = "phone";


    static final String TAG = "dbAdapter";
    static final String DATABASE_NAME = "MyDB";
    static final String DATABASE_TABLE = "contacts";
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_CREATE =
            "create table contacts (_id integer primary key autoincrement, "
                    + "name text not null, dob text not null, age text not null, phone text not null );";

    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;
    public dbAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try {
                Log.d("==============",DATABASE_CREATE);
                db.execSQL(DATABASE_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS contacts");
            onCreate(db);
        }
    }
    //---opens the database---
    public dbAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }
    //---closes the database---
    public void close()
    {
        DBHelper.close();
    }
    //---insert a contact into the database---
    public long insertContact(String name, String dob, String age, String phone)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_DOB, dob);
        initialValues.put(KEY_AGE, age);
        initialValues.put(KEY_PHONE, phone);

        return db.insert(DATABASE_TABLE, null, initialValues);
    }
    //---deletes a particular contact---
    public boolean deleteContact(long rowId)
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }
    //---retrieves all the contacts---
    public Cursor getAllContacts()
    {

//        SQLiteDatabase SQ = this.db;
//
//        String query = "SELECT * FROM " + DATABASE_TABLE + " WHERE 1" ;
//
//        //String query2 = "SELECT * FROM " + TABLE_NAME + "WHERE " + COLUMN_BG + " = 'B+'";
//
//
//
//        //Using cursors
//
//        Cursor CR = SQ.rawQuery(query, null);
//        return CR;



        return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME, KEY_DOB, KEY_AGE,
                KEY_PHONE}, null, null, null, null, null);
    }


    //---retrieves a particular contact---
    public Cursor getContact(long rowId) throws SQLException
    {
        Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                KEY_NAME, KEY_DOB,KEY_AGE, KEY_PHONE}, KEY_ROWID + "=" + rowId, null,null, null, null, null);
        if (mCursor != null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---updates a contact---
    public boolean updateContact(long rowId, String name, String dob, String age ,String phone)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_DOB, dob);
        args.put(KEY_AGE, age);
        args.put(KEY_PHONE, phone);
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}

