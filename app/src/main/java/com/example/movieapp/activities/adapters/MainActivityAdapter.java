package com.example.movieapp.activities.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.movieapp.R;
import com.example.movieapp.activities.Model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.ViewHolder> {
    private ArrayList<Movie> movies;

    public MainActivityAdapter(ArrayList<Movie> movies){
        this.movies = movies;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.simple_item_list,viewGroup,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
          viewHolder.overview.setText(movies.get(i).getOverview());
          viewHolder.title.setText(movies.get(i).getTitle());
        Picasso.get()
                .load(movies.get(i).getPosterPath())
                .placeholder(R.drawable.rsz_no_image)
                .into(viewHolder.poster_image);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
      private ImageView poster_image;
      private TextView overview;
      private TextView title;
     public ViewHolder(@NonNull View itemView) {
         super(itemView);
         poster_image = itemView.findViewById(R.id.image_simple_list);
         overview = itemView.findViewById(R.id.summary_simple_list);
         title = itemView.findViewById(R.id.title_simple_list);

     }
 }
}
