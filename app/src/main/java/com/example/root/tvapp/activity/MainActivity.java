package com.example.root.tvapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.root.tvapp.R;
import com.example.root.tvapp.interfaces.TokenInterface;
import com.example.root.tvapp.service.APIServices;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //GET VIEW ELEMENTS
        Button btn = (Button) findViewById(R.id.go_btn);
        final EditText username = (EditText) findViewById(R.id.username);
        final EditText userkey = (EditText) findViewById(R.id.userkey);
        final TextView errosText = (TextView) findViewById(R.id.errors);
        final TextView errorLog = (TextView) findViewById(R.id.logerror);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if((username.getText().toString().matches("")) || (userkey.getText().toString().matches(""))){
                    errosText.setVisibility(View.VISIBLE);
                }else{
                    //GET TOKEN
                    APIServices apiServices = new APIServices(getApplicationContext());
                    apiServices.authentificate("EFDC7A6838F30979", userkey.getText().toString(), username.getText().toString(), errorLog, errosText, new TokenInterface(){
                        @Override
                        public void onSuccess(String token) {
                            //REDIRECT HOME ACTIVITY
                            Intent myIntent = new Intent(MainActivity.this, HomeActivity.class);
                            //myIntent.putExtra("key", value); //Optional parameters
                            MainActivity.this.startActivity(myIntent);
                        }
                    });
                }
            }
        });
    }
}
