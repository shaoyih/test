package com.android.fablix;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class Main extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //testLogin();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }


    }


    public void clickSerch(View view){
        String query=((EditText) findViewById(R.id.search_bar)).getText().toString();
        connectToServer(view,query);

    }

    private String getUrl(String title, String year, String director, String star){
        //set default setting first
        String order="";
        String limit="10";
        String offset="1";
        return "https://10.0.2.2:8443/Project1/project1/movies?"+
                "by=search&title=" + title+
                "&year="+ year+
                "&director="+ director+
                "&stars="+ star+
                "&order="+ order+
                "&limit="+ limit+
                "&page="+ offset;
    }

    private void handleResult(String result, String title){
        Intent goToMovies = new Intent(this, movies.class);
        goToMovies.putExtra("result", result);
        goToMovies.putExtra("currentPage", 1);
        goToMovies.putExtra("title", title);
        startActivity(goToMovies);

    }


    public void connectToServer(View view, final String title) {
        Log.wtf("into connection", "damn!!!");

        final RequestQueue queue = NetworkManager.sharedManager(this).queue;

        String search_url = getUrl(title, null, null, null);
        Log.wtf("connection1", search_url);


        final StringRequest searchRequest = new StringRequest(Request.Method.GET, search_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("login.success", response);
                        handleResult(response,title);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("login.error", error.toString());

                    }
                }
        );
        queue.add(searchRequest);
    }











}
