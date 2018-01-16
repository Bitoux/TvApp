package com.example.root.tvapp.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.root.tvapp.interfaces.IActorArrayListener;
import com.example.root.tvapp.interfaces.IBooleanListener;
import com.example.root.tvapp.interfaces.ISeriesArrayListener;
import com.example.root.tvapp.interfaces.ISeriesIdListener;
import com.example.root.tvapp.interfaces.ISeriesListener;
import com.example.root.tvapp.interfaces.IStringListener;
import com.example.root.tvapp.interfaces.IUserListener;
import com.example.root.tvapp.model.Actor;
import com.example.root.tvapp.model.Serie;
import com.example.root.tvapp.model.User;
import com.example.root.tvapp.singleton.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class APIServices {

    // API SERVICE PAR MODEL

    public static final String API_URL = "https://api.thetvdb.com/";

    public static final String IMG_URL = "https://www.thetvdb.com/banners/_cache/";

    final String  REQUEST_TAG = "com.androidtutorialpoint.volleyJsonArrayRequest";

    private static final String TAG = "API Services";

    private Context mContext;

    public APIServices(Context c){
        this.mContext = c;
    }

    public void authentificate( String userkey, String username, final IStringListener callback){
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

    public void refreshToken(final IStringListener callback){
        String url = API_URL + "refresh_token";

        JsonObjectRequest refreshToken = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try{
                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("TokenAPI", response.getString("token"));
                    editor.commit();
                    callback.onSuccess(response.getString("token"));
                } catch (JSONException e){
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
        AppSingleton.getInstance(this.mContext).addToRequestQueue(refreshToken, REQUEST_TAG);
    }

    public void getUpdateSeries(final ISeriesIdListener callback){


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

    public String getSerieBanner(String bannerURL) {
        String url = IMG_URL + bannerURL;
        return url;
    }

    public void getSerieActors(int serieId, final IActorArrayListener callback){
        String url = API_URL + "series/" + serieId + "/actors";
        JsonObjectRequest getActors = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray data = response.getJSONArray("data");
                    Actor[] actors = new Actor[data.length()];
                    for(int i = 0; i < data.length(); i++){
                        JSONObject tmpActor = data.getJSONObject(i);
                        actors[i] = new Actor(tmpActor.getInt("id"), tmpActor.getString("image"), tmpActor.getString("name"), tmpActor.getString("role"));
                    }
                    callback.onSuccess(actors);
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){

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

        AppSingleton.getInstance(this.mContext).addToRequestQueue(getActors, REQUEST_TAG);
    }

    public void getUserAuth(final IUserListener callback){
        String url = API_URL + "user";
        JsonObjectRequest getUser = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONObject data = response.getJSONObject("data");
                    User user = new User(data.getString("userName"), data.getString("language"), data.getString("favoritesDisplaymode"));
                    callback.onSuccess(user);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
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

        AppSingleton.getInstance(this.mContext).addToRequestQueue(getUser, REQUEST_TAG);
    }

    public void getUserFavorites(final ISeriesIdListener callback) {
        String url = API_URL + "user/favorites";

        JsonObjectRequest getUserFavs = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONObject data = response.getJSONObject("data");
                    Log.d(TAG, data.toString());
                    JSONArray favorites = data.getJSONArray("favorites");
                    int[] seriesIDs = new int[favorites.length()];
                    for(int i = 0 ; i < favorites.length() ; i++){
                        seriesIDs[i] = favorites.getInt(i);
                    }
                    callback.onSuccess(seriesIDs);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
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

        AppSingleton.getInstance(this.mContext).addToRequestQueue(getUserFavs, REQUEST_TAG);
    }

    public void searchSerieByName(final String serieName, final ISeriesArrayListener callback){
        String url = API_URL + "search/series?name=" + serieName;

        JsonObjectRequest getSearchSerie = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response){
                try{
                    JSONArray data = response.getJSONArray("data");
                    Serie[] seriesName = new Serie[data.length()];
                    for(int i = 0; i < data.length();i++ ){
                        JSONObject serie = data.getJSONObject(i);

                        seriesName[i] = new Serie(serie.getInt("id"), serie.getString("seriesName"), null, null, null, null);
                    }
                    callback.onSuccess(seriesName);
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){

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

        AppSingleton.getInstance(this.mContext).addToRequestQueue(getSearchSerie, REQUEST_TAG);
    }

    public void checkUserFavorites(final String serieID, final IBooleanListener callback) {
        String url = API_URL + "user/favorites";

        JsonObjectRequest getUserFavs = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONObject data = response.getJSONObject("data");
                    JSONArray favorites = data.getJSONArray("favorites");
                    System.out.println(favorites.toString());
                    Boolean checkFavs = false;
                    for(int i = 0 ; i < favorites.length() ; i++){
                        if(favorites.getString(0).equals(serieID)){
                            checkFavs = true;
                        }
                    }
                    callback.onSuccess(checkFavs);
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
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

        AppSingleton.getInstance(this.mContext).addToRequestQueue(getUserFavs, REQUEST_TAG);
    }

    public void addFavToUser(int serieID, final IBooleanListener callback ) {
        String url = API_URL + "user/favorites/" + serieID;

        JsonObjectRequest addFavToUser = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(true);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){

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

        AppSingleton.getInstance(this.mContext).addToRequestQueue(addFavToUser, REQUEST_TAG);
    }

    public void delFavToUser(int serieID, final IBooleanListener callback ) {
        String url = API_URL + "user/favorites/" + serieID;

        JsonObjectRequest delFavToUser = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(true);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){

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

        AppSingleton.getInstance(this.mContext).addToRequestQueue(delFavToUser, REQUEST_TAG);
    }

    public void getSerieById(int serieID, final ISeriesListener listener){

        String url = API_URL + "series/" + serieID;

        JsonObjectRequest getSerieByID = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");
                    long id = data.getLong("id");
                    String name = data.getString("seriesName");
                    String overview = data.getString("overview");
                    String rating = data.getString("siteRating");
                    String banner = data.getString("banner");
                    JSONArray genres = data.getJSONArray("genre");
                    String[] genresArray = new String[genres.length()];
                    for(int i = 0 ; i < genres.length() ; i ++){
                        genresArray[i] = genres.getString(i);
                    }
                    Serie serie = new Serie(id, name, genresArray, overview, rating, banner);
                    listener.onSuccess(serie);
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
