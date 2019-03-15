package com.android.fablix;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class moviesView extends ArrayAdapter<Movie> {
    private List<Movie> movies;

    public moviesView(Context ctx, List<Movie> movies){
        super(ctx, R.layout.movie_row, movies);
        this.movies = movies;
    }

    @NonNull
    @Override

    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view= inflater.inflate(R.layout.movie_row,parent,false);

        Movie m=movies.get(position);

        //prepare all of views ready to fill in
        TextView titleView = (TextView)view.findViewById(R.id.movie_title);
        TextView directorView = (TextView)view.findViewById(R.id.movie_director);
        TextView yearView = (TextView)view.findViewById(R.id.movie_year);
        TextView genreView = (TextView)view.findViewById(R.id.movie_genres);
        TextView starsView = (TextView)view.findViewById(R.id.movie_stars);

        //get all of parts from movie class
        titleView.setText(m.getTitle());
        directorView.setText(m.getDirector());
        yearView.setText(m.getYear());
        handleListView(m.getGenres(),genreView);
        handleListView(m.getStars(),starsView);

        return view;
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
