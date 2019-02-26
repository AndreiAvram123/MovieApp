package com.example.movieapp.activities.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.movieapp.R;
import com.example.movieapp.activities.Model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {

    private  ArrayList<Movie> savedMovies;

    public GridAdapter (ArrayList<Movie> savedMovies){
        this.savedMovies = savedMovies;
    }

    @Override
    public int getCount() {
        return savedMovies.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
       Movie movie = savedMovies.get(i);

       if(view == null){
           view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_grid_view,
                   viewGroup,false);
       }


        ImageView image = view.findViewById(R.id.image_grid_view);

         Picasso.get().
                 load(movie.getPosterPath())
                 .placeholder(R.drawable.rsz_no_image)
                 .into(image);

        return view;
    }

    /**
     * Use this method to add an item
     * to the list
     * @param movie
     */
    public void addMovie(Movie movie){
            savedMovies.add(movie);
            notifyDataSetChanged();
    }

    /**
     * Use this method to delete
     * a movie from the list
     * @param movie
     */
    public void removeMovie(Movie movie) {

        savedMovies.remove(movie);
        notifyDataSetChanged();
    }


    public boolean isListEmpty() {
        return savedMovies.isEmpty();
    }
}
