package com.android.fablix;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class singleMovie extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_movie2);
        Bundle bundle = getIntent().getExtras();
        String id=bundle.getString("id");
        connectToServer(id);


    }
    private void handleResult(String result,String id) throws JSONException {
        View tview=findViewById(R.id.title);
        TextView tid=findViewById(R.id.id);
        tid.setText(id);
        parseResult(result);

    }
    private void parseResult(String result) throws JSONException {
        JSONArray objArray = new JSONArray(result);
        List<Movie> movies= new ArrayList<Movie>();
        JSONObject obj = objArray.getJSONObject(0);
        //handle easy part
        String title = obj.getString("title");
        String dir = obj.getString("director");
        String year = obj.getString("year");
        String rating = obj.getString("rating");

        //handle trouble part
        JSONArray Jgenres = obj.getJSONArray("genres");
        JSONArray Jstars = obj.getJSONArray("stars");
        List<String> stars = getStars(Jstars);
        List<String> genres = getGenres(Jgenres);

        updateResult(title,dir,year,rating,stars,genres);
    }

    private void updateResult(String title, String dir, String year, String rating,List<String>stars,List<String>genres){
        //prepare all of views ready to fill in
        TextView titleView = (TextView)findViewById(R.id.title);
        TextView rateView = (TextView)findViewById(R.id.rate);
        TextView directorView = (TextView)findViewById(R.id.director);
        TextView yearView = (TextView)findViewById(R.id.year);
        TextView genreView = (TextView)findViewById(R.id.genre);
        TextView starsView = (TextView)findViewById(R.id.stars);

        //get all of parts from movie class
        titleView.setText(title);
        directorView.setText(dir);
        yearView.setText(year);
        rateView.setText(rating);
        handleListView(genres,genreView);
        handleListView(stars,starsView);
    }
    private List<String> getStars(JSONArray Stars) throws JSONException{
        List<String> stars= new ArrayList<String>();
        for(int i=0 ;i<Stars.length();i++){
            //Log.wtf("star name: ",);
            stars.add(Stars.getJSONArray(i).getString(0));
        }
        return stars;
    }
    private List<String> getGenres(JSONArray Genres) throws JSONException {
        List<String> genres= new ArrayList<String>();
        for(int i=0 ;i<Genres.length();i++){
            //Log.wtf("star name: ",stars.getJSONArray(i).getString(0));
            genres.add(Genres.getString(i));
        }
        return genres;
    }
    public void connectToServer(final String id) {
        Log.wtf("into connection", "damn!!!");

        final RequestQueue queue = NetworkManager.sharedManager(this).queue;

        String url="https://ec2-13-59-241-2.us-east-2.compute.amazonaws.com:8443/Project1/project1/single_movie?id="+id;
        Log.wtf("connection1", url);


        final StringRequest searchRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("login.success", response);
                        try {
                            handleResult(response,id);
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
        );
        queue.add(searchRequest);
    }
    private void handleListView(List<String> l, TextView view){
        String result=new String();
        for(int i=0; i<l.size();i++){
            if(i==l.size()-1){
                result+=l.get(i);
            }
            else{
                result+=l.get(i)+", ";
            }
        }
        view.setText(result);
    }
}
