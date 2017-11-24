package com.example.root.tvapp.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.root.tvapp.interfaces.SeriesIdInterface;
import com.example.root.tvapp.interfaces.SeriesInterface;
import com.example.root.tvapp.interfaces.TokenInterface;
import com.example.root.tvapp.model.Serie;
import com.example.root.tvapp.singleton.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class APIServices {

    public static final String API_URL = "https://api.thetvdb.com/";

    final String  REQUEST_TAG = "com.androidtutorialpoint.volleyJsonArrayRequest";

    private Context mContext;

    public APIServices(Context c){
        this.mContext = c;
    }

    public void authentificate(String apikey, String userkey, String username, final TextView errorLog, final TextView errorRequired, final TokenInterface callback){
        String url = API_URL + "login";

        //PREPARE PARAMS
        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("apikey", "EFDC7A6838F30979");
        postParam.put("userkey", userkey);//0656600E9EF55BC5
        postParam.put("username", username);//Bitoux


        JsonObjectRequest tokenRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // the response is already constructed as a JSONObject!
                try {
                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("TokenAPI", response.getString("token"));
                    editor.commit();
                    callback.onSuccess(response.getString("token"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                errorLog.setVisibility(View.VISIBLE);
                errorRequired.setVisibility(View.INVISIBLE);
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        AppSingleton.getInstance(this.mContext).addToRequestQueue(tokenRequest, REQUEST_TAG);
    }

    public void getUpdateSeries(final SeriesIdInterface callback){


        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DAY_OF_WEEK, -1);
        Date date = calendar.getTime();
        long fromTime = date.getTime()/1000;
        String url = API_URL + "updated/query?fromTime="+fromTime;

        JsonObjectRequest updatedSeries = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response) {
                //EXPORT ARRAY OF SERIES ID
                try{
                    JSONArray data = response.getJSONArray("data");
                    int[] seriesIDs = new int[data.length()];
                    for(int i = 0 ; i < data.length() ; i++){
                        seriesIDs[i] = data.getJSONObject(i).getInt("id");
                    }
                    callback.onSuccess(seriesIDs);
                } catch (JSONException e){
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //Authorization: Bearer yourjwttoken
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext);
                String auth_token_string = settings.getString("TokenAPI", ""/*default value*/);
                String auth = "Bearer " + auth_token_string;
                headers.put("Authorization", auth);
                return headers;
            }
        };

        AppSingleton.getInstance(this.mContext).addToRequestQueue(updatedSeries, REQUEST_TAG);
    }

    public void getSeriesListView(int[] seriesIDs, final SeriesInterface seriesInterface){
        ArrayList<Serie> seriesList = new ArrayList<Serie>();
        for(int i = 0; i < seriesIDs.length ; i++){
            String url = API_URL + "series/" + seriesIDs[i];

            JsonObjectRequest getSerieByID = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>(){

                @Override
                public void onResponse(JSONObject response) {
                    System.out.println("THE SERIE");
                    try {
//                        private long id;
//                        private String name;
//                        private String[] genre;
//                        private String overview;
//                        private float rating;
//                        private String banner;
                        JSONObject data = response.getJSONObject("data");
                        long id = data.getLong("id");
                        String name = data.getString("seriesName");
                        String overview = data.getString("overview");
                        String rating = data.getString("siteRating");
                        String banner = data.getString("banner");
                        //String[] genre = data.getString("genre");

//                        Serie serie = new Serie(data.getLong("id"), );

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    //Authorization: Bearer yourjwttoken
                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext);
                    String auth_token_string = settings.getString("TokenAPI", ""/*default value*/);
                    String auth = "Bearer " + auth_token_string;
                    headers.put("Authorization", auth);
                    return headers;
                }
            };

            AppSingleton.getInstance(this.mContext).addToRequestQueue(getSerieByID, REQUEST_TAG);
        }
    }
}
