package com.example.root.tvapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.root.tvapp.R;
import com.example.root.tvapp.interfaces.IStringListener;
import com.example.root.tvapp.service.APIServices;

public class MainActivity extends AppCompatActivity {

    private EditText userName;
    private Button authBtn;
    private EditText userKey;
    private TextView errorText;
    private TextView errorOffline;
    private CheckBox memoriseToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String auth_token_string = settings.getString("TokenAPI", null);

        errorOffline = (TextView) findViewById(R.id.error_offilne);

        if(isOnline()){

            Log.d("IS ONLINE", "IS ONELINE");
        }else{

            if(auth_token_string != null){
                Intent myIntent = new Intent(MainActivity.this, HomeActivity.class);
                MainActivity.this.startActivity(myIntent);
            }else{
                errorOffline.setVisibility(View.VISIBLE);
                Log.d("NOT ONLINE", "OFFLINE");
            }
        }



        if(auth_token_string != null){
            Log.d("MainActivity", "EVER AUTH");
            System.out.println("AZEAZE");
            APIServices apiServices = new APIServices(getApplicationContext());
            apiServices.refreshToken( new IStringListener(){
                @Override
                public void onSuccess(String token) {
                    //REDIRECT HOME ACTIVITY
                    Intent myIntent = new Intent(MainActivity.this, HomeActivity.class);
                    //myIntent.putExtra("key", value); //Optional parameters
                    MainActivity.this.startActivity(myIntent);
                }
            });
        }

        Log.d("MainActivity", "NEVER AUTH");

        //GET VIEW ELEMENTS
        this.authBtn = (Button) findViewById(R.id.go_btn);
        this.userName = (EditText) findViewById(R.id.username);
        this.userKey = (EditText) findViewById(R.id.userkey);
        this.errorText = (TextView) findViewById(R.id.errors);
        this.memoriseToken = (CheckBox) findViewById(R.id.save_token);

        authBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if((userName.getText().toString().matches("")) || (userKey.getText().toString().matches(""))){
                    errorText.setVisibility(View.VISIBLE);
                }else{
                    //GET TOKEN
                    APIServices apiServices = new APIServices(getApplicationContext());
                    apiServices.authentificate(memoriseToken.isChecked(), userKey.getText().toString(), userName.getText().toString(), new IStringListener(){
                        @Override
                        public void onSuccess(String token) {
                            //REDIRECT HOME ACTIVITY
                            Intent myIntent = new Intent(MainActivity.this, HomeActivity.class);
                            MainActivity.this.startActivity(myIntent);
                        }
                    });
                }
            }
        });
    }


    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
