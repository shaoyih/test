package com.android.fablix;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

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

public class movies extends Activity {
    int currentPage;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        Bundle bundle = getIntent().getExtras();




        try {
            currentPage=bundle.getInt("currentPage");
            title=bundle.getString("title");

            final List<Movie> movies=parseMovies(bundle.getString("result"));
            moviesView row= new moviesView(this,movies);
            ListView listView=(ListView) findViewById(R.id.movie_list);
            listView.setAdapter(row);
            //listView.setAdapter();
            movieJump(listView,movies);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void  movieJump(ListView listView, final List<Movie> movies){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie m=movies.get(position);
                Intent goToMovies=new Intent(view.getContext(), singleMovie.class);
                goToMovies.putExtra("id",m.getId());
                startActivity(goToMovies);
            }
        });
    }

    private void refreshList(String result) throws JSONException {
        final List<Movie> movies=parseMovies(result);
        moviesView row= new moviesView(this,movies);
        ListView listView=(ListView) findViewById(R.id.movie_list);
        listView.setAdapter(row);
        movieJump(listView,movies);

    }



    private void setButtonStatus( int totalMovies){

        Button nextBtn = findViewById(R.id.next_btn);
        Button prevBtn = findViewById(R.id.prev_btn);



        Log.wtf("c", String.valueOf(currentPage));
        Log.wtf("t", String.valueOf(totalMovies));


        //status
        if(totalMovies>1 && currentPage<totalMovies/10){
            nextBtn.setEnabled(true);
        }
        else{
            nextBtn.setEnabled(false);
        }
        if(currentPage>1){
            prevBtn.setEnabled(true);
        }
        else{
            prevBtn.setEnabled(false);
        }

        //link to an new activity
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPage+=1;
                String url=getUrl(title,null,null,null,currentPage);
                connectToServer(url);

            }
        });


        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPage-=1;
                String url=getUrl(title,null,null,null,currentPage);
                connectToServer(url);

            }
        });
    }




    private List<Movie> parseMovies(String result) throws JSONException {
        JSONArray objArray = new JSONArray(result);
        List<Movie> movies= new ArrayList<Movie>();

        setButtonStatus(objArray.getJSONObject(0).getInt("totalPage"));
        for(int i=1; i<objArray.length();i++) {
            JSONObject obj = objArray.getJSONObject(i);
            //handle easy part
            String title = obj.getString("title");
            String dir = obj.getString("director");
            String year = obj.getString("year");
            String rating = obj.getString("rating");
            String id = obj.getString("id");


            //handle trouble part
            JSONArray Jgenres = obj.getJSONArray("genres");
            JSONArray Jstars = obj.getJSONArray("stars");
            List<String> stars = getStars(Jstars);
            List<String> genres = getGenres(Jgenres);

            Movie m = new Movie(id, title, dir, year, rating, stars, genres);
            movies.add(m);

            Log.wtf("title", title);
            Log.wtf("director", dir);
        }
        return movies;
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
    private String getUrl(String title, String year, String director, String star, int page){
        //set default setting first
        String order="";
        String limit="10";
        String offset=Integer.toString(page);
        return "https://ec2-13-59-241-2.us-east-2.compute.amazonaws.com:8443/Project1/project1/movies?"+
                "by=search&title=" + title+
                "&year="+ year+
                "&director="+ director+
                "&stars="+ star+
                "&order="+ order+
                "&limit="+ limit+
                "&page="+ offset;
    }

    public void connectToServer(final String url) {
        Log.wtf("into connection", "damn!!!");

        final RequestQueue queue = NetworkManager.sharedManager(this).queue;


        Log.wtf("connection1", url);


        final StringRequest searchRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("login.success", response);
                        try {
                            refreshList(response);
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




}
