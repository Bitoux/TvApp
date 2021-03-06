package com.example.root.tvapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by root on 16/01/18.
 */

public abstract class DAOBase {
    protected final static int VERSION = 1;
    protected final static String NOM = "AppTVDB";

    protected SQLiteDatabase mDB = null;
    protected DatabaseHelper mHandler = null;

    public DAOBase(Context context){
        this.mHandler = new DatabaseHelper(context);
    }

    public SQLiteDatabase open(){
        Log.d("DAOBase", "Open");
        mDB = mHandler.getWritableDatabase();
        return mDB;
    }

    public void close() {
        mDB.close();
    }

    public SQLiteDatabase getDb() {
        return mDB;
    }
}
