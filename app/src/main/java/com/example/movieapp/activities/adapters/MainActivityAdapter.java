package com.example.movieapp.activities.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.movieapp.R;
import com.example.movieapp.activities.Model.Constraints;
import com.example.movieapp.activities.Model.Movie;
import com.example.movieapp.activities.activities.MovieExpandedActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.ViewHolder> {
    private ArrayList<Movie> movies;
    private Context context;

    public MainActivityAdapter(ArrayList<Movie> movies, Context context){
        this.movies = movies;
        this.context = context;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.simple_item_list,viewGroup,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
          viewHolder.overview.setText(movies.get(i).getShortOverview());
          viewHolder.title.setText(movies.get(i).getTitle());
          Picasso.get()
                .load(movies.get(i).getPosterPath())
                .placeholder(R.drawable.rsz_no_image)
                .into(viewHolder.poster_image);

          viewHolder.layout.setOnClickListener(view -> startMovieExpandedActivity(i));
    }

    private void startMovieExpandedActivity(int position) {
        Intent startMovieActivityIntent = new Intent(context, MovieExpandedActivity.class);
        startMovieActivityIntent.putExtra(Constraints.KEY_MOVIE,movies.get(position));
        context.startActivity(startMovieActivityIntent);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
      private ImageView poster_image;
      private TextView overview;
      private TextView title;
      private View layout;

     public ViewHolder(@NonNull View itemView) {
         super(itemView);
         poster_image = itemView.findViewById(R.id.image_simple_list);
         overview = itemView.findViewById(R.id.summary_simple_list);
         title = itemView.findViewById(R.id.title_simple_list);
         layout = itemView;
     }
 }
}
