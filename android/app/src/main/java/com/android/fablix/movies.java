package com.android.fablix;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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
import java.util.List;

public class movies extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        Bundle bundle = getIntent().getExtras();


        try {
            final List<Movie> movies=parseMovies(bundle.getString("result"),bundle);
            moviesView row= new moviesView(this,movies);
            ListView listView=(ListView) findViewById(R.id.movie_list);
            listView.setAdapter(row);
            //listView.setAdapter();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Movie m=movies.get(position);
                    Intent goToMovies=new Intent(view.getContext(), singleMovie.class);
                    goToMovies.putExtra("id",m.getId());
                    startActivity(goToMovies);
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setButtonStatus(final Bundle bundle, int totalPage){

        Button nextBtn = findViewById(R.id.next_btn);
        Button prevBtn = findViewById(R.id.prev_btn);
        final int currentPage=bundle.getInt("currentPage");
        final String title=bundle.getString("title");

        Log.wtf("c", String.valueOf(currentPage));
        Log.wtf("t", String.valueOf(totalPage));


        //status
        if(totalPage>1 && currentPage<totalPage/10){
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
                int newCur=currentPage+1;

                Intent goToMovies;new Intent(v.getContext(), movies.class);
                String url=getUrl(title,null,null,null,newCur);
                connectToServer(v.getRootView(),url,title,newCur);

            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newCur=currentPage-1;

                Intent goToMovies;new Intent(v.getContext(), movies.class);
                String url=getUrl(title,null,null,null,newCur);
                connectToServer(v.getRootView(),url,title,newCur);

            }
        });
    }




    private List<Movie> parseMovies(String result,Bundle bundle) throws JSONException {
        JSONArray objArray = new JSONArray(result);
        List<Movie> movies= new ArrayList<Movie>();

        setButtonStatus(bundle,objArray.getJSONObject(0).getInt("totalPage"));
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
        return "https://10.0.2.2:8443/Project1/project1/movies?"+
                "by=search&title=" + title+
                "&year="+ year+
                "&director="+ director+
                "&stars="+ star+
                "&order="+ order+
                "&limit="+ limit+
                "&page="+ offset;
    }

    public void connectToServer(View view, final String url,final String title, final int newCur) {
        Log.wtf("into connection", "damn!!!");

        final RequestQueue queue = NetworkManager.sharedManager(this).queue;


        Log.wtf("connection1", url);


        final StringRequest searchRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("login.success", response);
                        handleResult(response,title,newCur);

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

    private void handleResult(String result, String title, int CurrentPage){
        Intent goToMovies = new Intent(this, movies.class);
        goToMovies.putExtra("result", result);
        goToMovies.putExtra("currentPage", CurrentPage);
        goToMovies.putExtra("title", title);
        startActivity(goToMovies);

    }


}
