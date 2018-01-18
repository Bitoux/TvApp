package com.example.root.tvapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.root.tvapp.model.Actor;

import java.util.ArrayList;

/**
 * Created by root on 18/01/18.
 */

public class DAOActor extends DAOBase {

    public static final String actorTable="ActorTable";
    public static final String actorId="ActorID";
    public static final String actorName="ActorName";
    public static final String actorImg="ActorImg";
    public static final String actorRole="ActorRole";
    public static final String actorSerie="ActorSerie";

    public static final String TABLE_DROP =  "DROP TABLE IF EXISTS " + actorTable + ";";

    public DAOActor(Context context){
        super(context);
    }

    public void addActor(Actor a){
        ContentValues value = new ContentValues();
        value.put(DAOActor.actorId, a.getId());
        value.put(DAOActor.actorName, a.getName());
        value.put(DAOActor.actorImg, a.getImg());
        value.put(DAOActor.actorRole, a.getRole());
        value.put(DAOActor.actorSerie, a.getSerie());
        mDB.insert(DAOActor.actorTable, null, value);
    }

    public void deleteActor(int actorId){
        mDB.delete(DAOActor.actorTable, actorId + " = ?", new String[] {String.valueOf(actorId)});
    }

    public Actor selectActor(int actorId){
        Cursor cursor = null;
        Actor actor = null;
        cursor = mDB.rawQuery("SELECT * FROM " + actorTable + " WHERE " + DAOActor.actorId + " = " + actorId, null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                actor = new Actor(
                    cursor.getInt(cursor.getColumnIndex(DAOActor.actorId)),
                    cursor.getString(cursor.getColumnIndex(DAOActor.actorImg)),
                    cursor.getString(cursor.getColumnIndex(DAOActor.actorName)),
                    cursor.getString(cursor.getColumnIndex(DAOActor.actorRole)),
                    cursor.getInt(cursor.getColumnIndex(DAOActor.actorSerie))
                );
            }
            cursor.close();
        }
        return actor;
    }

    public ArrayList<Actor> getSerieActors(int serieID){
        Cursor cursor = null;
        ArrayList<Actor> actors = new ArrayList<Actor>();
        cursor = mDB.rawQuery("SELECT * FROM " + actorTable + " WHERE " + DAOActor.actorSerie + " = " + serieID, null);
        if(cursor != null){
            Log.d("ACTORS IN DB", "CURSOR NOT NULL");
            while (cursor.moveToNext()){
                Actor actor = new Actor(
                        cursor.getInt(cursor.getColumnIndex(DAOActor.actorId)),
                        cursor.getString(cursor.getColumnIndex(DAOActor.actorImg)),
                        cursor.getString(cursor.getColumnIndex(DAOActor.actorName)),
                        cursor.getString(cursor.getColumnIndex(DAOActor.actorRole)),
                        cursor.getInt(cursor.getColumnIndex(DAOActor.actorSerie))
                );
                Log.d("ACTOR IN DB", actor.toString());
                actors.add(actor);
            }
            cursor.close();
        }
        return actors;
    }
}
