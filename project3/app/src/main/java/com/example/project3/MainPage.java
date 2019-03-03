package com.example.project3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);



    }
    public void searchForMovies(View view) {
        final RequestQueue queue=NetWorkManager.sharedManager(this).queue;
        String url="https://192.168.1.94:8443/Project1" +
                "/project1/movies?by=search&year=null&director=null&stars=null&order=null&page=1&limit=10"
                +"&title="+((TextView)findViewById(R.id.searchKey)).getText().toString();
        Log.d("url", url);
        final StringRequest searchRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONArray response_json = new JSONArray(response);
                            Log.d("searchRequest", response);
                            final ArrayList<Movie> movies = new ArrayList<>();
                            int range=response_json.getJSONObject(0).getInt("totalPage");
                            for (int i=1;i<range;i++){
                                JSONObject temp=response_json.getJSONObject(i);
                                movies.add(new Movie(temp.getString("title"), temp.getString("year"),temp.getString("director")));
                            }

                            MovieListViewAdapter adapter= new MovieListViewAdapter(movies,view.getContext());
                            ListView listView = (ListView)findViewById(R.id.listview);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Movie movie = movies.get(position);
                                    String message = String.format("Clicked on position: %d, name: %s, %d", position, movie.getTitle(), movie.getYear());
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                }
                            });



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("search.error", error.toString());
                    }
                }
        );
        queue.add(searchRequest);
    }

}
