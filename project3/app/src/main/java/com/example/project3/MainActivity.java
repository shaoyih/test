package com.example.project3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RequestQueue queue=NetWorkManager.sharedManager(this).queue;

        final StringRequest afterLoginRequest = new StringRequest(Request.Method.GET, "https://192.168.1.94:8443/Project1/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("username.reponse", response);
                        try {
                            JSONObject response_json = new JSONObject(response);
                            String str_value=response_json.getString("status");

                            if(str_value.equals("login")){
                                Intent goToIntent = new Intent(getApplicationContext(), MainPage.class);

                                startActivity(goToIntent);

                            }
                            else{
                                Log.d("notlogin.reponse", response);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("username.error", error.toString());
                    }
                }
        );
        queue.add(afterLoginRequest);

    }
    public void connectToTomcat(View view) {


        final RequestQueue queue=NetWorkManager.sharedManager(this).queue;

        final StringRequest loginRequest= new StringRequest(Request.Method.POST,  "https://192.168.1.94:8443/Project1/api/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("login.success",response);

                        System.out.println(response);
                        try {
                            JSONObject response_json = new JSONObject(response);
                            String str_value=response_json.getString("status");

                            if(str_value.equals("fail")){

                                Toast.makeText(view.getContext(), response_json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                            else{

                                Intent goToIntent = new Intent(view.getContext(), MainPage.class);

                                startActivity(goToIntent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("login.error", error.toString());


                    }

            }){
            @Override
            protected Map<String, String> getParams() {
                // Post request form data
                final Map<String, String> params = new HashMap<String, String>();
                params.put("username", ((EditText)findViewById(R.id.email)).getText().toString());
                params.put("password", ((EditText)findViewById(R.id.password)).getText().toString());

                return params;
            }
        };



        // !important: queue.add is where the login request is actually sent
        queue.add(loginRequest);


    }
}

