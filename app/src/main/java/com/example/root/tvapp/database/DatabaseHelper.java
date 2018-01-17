package com.example.root.tvapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by root on 15/01/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    static final String dbName="AppTVDB";

    //SERIES
    public static final String serieTable="SerieTable";
    public static final String serieId="SerieID";
    public static final String serieName="SerieName";
    public static final String serieOverview="SerieOverview";
    public static final String serieRating="SerieRating";
    public static final String serieBanner="SerieBanner";
    public static final String serieGenre="SerieGenre";

    //ACTOR
    static final String actorTable="ActorTable";
    static final String actorId="ActorID";
    static final String actorName="ActorName";
    static final String actorImg="ActorImg";
    static final String actorRole="ActorRole";
    static final String actorSerie="ActorSerie";

    //USER
    static final String userTable="UserTable";
    static final String userId="UserID";
    static final String userName="UserName";
    static final String userLanguage="UserLanguage";
    static final String userDisplayMode="UserDisplayMode";

    public DatabaseHelper(Context context) {
        super(context, dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE "+serieTable+" ("+serieId+ " INTEGER PRIMARY KEY , "+
                serieName+ " TEXT, " + serieOverview + " TEXT, " + serieRating + " TEXT, " + serieBanner + " TEXT);");


        sqLiteDatabase.execSQL("CREATE TABLE " + actorTable + " (" + actorId + " INTEGER PRIMARY KEY , " +
            actorName + " TEXT, " + actorImg + " TEXT, " + actorRole + " TEXT, " + actorSerie + " INTEGER);" );

        sqLiteDatabase.execSQL("CREATE TABLE " + userTable + " (" + userId + " INTEGER PRIMARY KEY ,"+
                userName + " TEXT, " + userLanguage + ", TEXT " + userDisplayMode + " TEXT);" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
