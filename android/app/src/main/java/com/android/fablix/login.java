package com.android.fablix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


    }

    public void signIn(View view) throws JSONException {
       // String msg = ((EditText) findViewById(R.id.red_2_blue_message)).getText().toString();

        Intent goToIntent = new Intent(this, Main.class);
        String email=((EditText) findViewById(R.id.emailLog)).getText().toString();
        String password=((EditText) findViewById(R.id.passwordLog)).getText().toString();
        connectToServer(view,email,password,goToIntent);

    }

    private void handle_result(Intent goToIntent, String status, final String email, final String password) throws JSONException {
        //goToIntent.putExtra("email",email);
        //goToIntent.putExtra("password",password);
        //goToIntent.putExtra("message", "Cannot connect to server");
        if(status.length()>0 && status.contains("status")) {
            JSONObject obj = new JSONObject(status);
            String rs = obj.getString("status");
            String message = obj.getString("message");

            if (rs.equals("success")){
                startActivity(goToIntent);
            }
            else{
                goToIntent.putExtra("message", message);
                Toast.makeText(this, message + ".", Toast.LENGTH_LONG).show();
            }

        }

    }
    public void connectToServer(View view, final String email, final String password, final Intent goToIntent) {
        Log.wtf("into connection","damn!!!");

        final RequestQueue queue = NetworkManager.sharedManager(this).queue;




        final StringRequest loginRequest = new StringRequest(Request.Method.POST, "https://10.0.2.2:8443/Project1/api/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("login.success", response);
                        //((TextView) findViewById(R.id.login_response)).setText(response);
                        // Add the request to the RequestQueue.
                        try {
                            handle_result(goToIntent,response,email,password);
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
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                // Post request form data
                final Map<String, String> params = new HashMap<String, String>();
                params.put("username", email);
                params.put("password", password);

                return params;
            }

        };

        // !important: queue.add is where the login request is actually sent
        queue.add(loginRequest);

    }



}
