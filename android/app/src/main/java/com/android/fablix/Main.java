package com.android.fablix;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main extends AppCompatActivity {
    ArrayList<String> titleS;
    ArrayList<String> allGenres;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //testLogin();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //set spinner1

        initialTitle();
        Spinner s = (Spinner) findViewById(R.id.browse_by_title);
        setSpinner(s,titleS);

        //set spinner2
        allGenres= new ArrayList<String>();
        getGenre();
        Spinner s2 = (Spinner) findViewById(R.id.browse_by_genre);
        Log.wtf("genres",allGenres.toString());
       setSpinner(s2,allGenres);

        advSearch();


    }
    private void initialTitle(){
        titleS=new ArrayList<String>(Arrays.asList( "A","B","C","D","E","F","G","H","I","J","K","L",
                "M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"
                ,"1","2","3","4","5","6","7","8","9","0"));
    }
    private void setSpinner(Spinner spinner,List<String> list){
        spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,list));
    }

    protected void advSearch(){
        Button b= (Button) findViewById(R.id.search_btn2);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main.this,Pop.class));
            }
        });
    }
    public void clickSerch(View view){
        String query=((EditText) findViewById(R.id.search_bar1)).getText().toString();
        connectToServer(view,query);

    }

    private String getUrl(String title, String year, String director, String star){
        //set default setting first
        String order="";
        String limit="10";
        String offset="1";
        return "https://ec2-13-59-241-2.us-east-2.compute.amazonaws.com:8443/Project1/project1/movies?"+
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
    private void collectGenres(String response) throws JSONException {

        JSONArray objArray = new JSONArray(response);
        for(int i=1; i<objArray.length();i++) {
            JSONObject obj = objArray.getJSONObject(i);
            allGenres.add(obj.getString("name"));
        }


    }
    public void getGenre() {

        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        final StringRequest searchRequest = new StringRequest(Request.Method.GET, "https://10.0.2.2:8443/Project1/project1/get_genres",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("genre.success", response);
                        try {
                            collectGenres(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("genre.error", error.toString());

                    }
                }
        );
        queue.add(searchRequest);
    }











}
