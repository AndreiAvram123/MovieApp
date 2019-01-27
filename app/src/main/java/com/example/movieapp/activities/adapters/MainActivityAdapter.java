package com.example.movieapp.activities.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.movieapp.R;
import com.example.movieapp.activities.Model.Constraints;
import com.example.movieapp.activities.Model.Movie;
import com.example.movieapp.activities.activities.MovieExpandedActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivityAdapter extends RecyclerView.Adapter {
    private ArrayList<Movie> movies = new ArrayList<>();
    private boolean isLoading = false;
    private final int VIEW_TYPE_ITEM = 0;
    private int VIEW_TYPE_LOADING = 1;
    private MainActivityAdapterInterface mainActivityAdapterInterface;
    private Context context;
    private String fragmentName;

    public MainActivityAdapter( RecyclerView recyclerView, Activity activity,String fragmentName)
    {
        this.fragmentName = fragmentName;
        mainActivityAdapterInterface = (MainActivityAdapterInterface) activity;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if(linearLayoutManager != null && !isLoading &&
                        linearLayoutManager.findLastCompletelyVisibleItemPosition() == movies.size()-1)
                {
                    isLoading = true;
                    loadMore();
                }
            }
        });
    }
    public MainActivityAdapter(){

    }

    public void setLoading(boolean loading){
        isLoading = loading;
    }
    private void loadMore() {
        mainActivityAdapterInterface.requestLoading(movies.size(),fragmentName);
    }

    public void addMovies(ArrayList<Movie> movies){
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    public void addMovie(Movie movie){
        movies.add(movie);
        notifyDataSetChanged();
    }
    public void removeMovie(int position){
        movies.remove(position);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    //we get the viewType when we override the method
    //see below
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        this.context = viewGroup.getContext();
        if( viewType== VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.simple_item_list, viewGroup, false);
            return  new ItemViewHolder(view);
        }else {
            if(viewType == VIEW_TYPE_LOADING) {
                View view = LayoutInflater.from(context).
                        inflate(R.layout.loading_item_list, viewGroup, false);
                return new LoadingViewHolder(view);
            }
            }
            return null;
    }

    @Override
    public int getItemViewType(int position) {
        if(movies.get(position)!= null)
            return VIEW_TYPE_ITEM;
        else
            return VIEW_TYPE_LOADING;
    }


    /**
     * This is the method which returns the viewType
     * @param viewHolder
     * @param position
     */


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
          /*
            Check whether we have to create a viewHolder of type ItemViewHolder or
            LoadingViewHolder
           */
          if(viewHolder instanceof ItemViewHolder) {
              ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
              itemViewHolder.overview.setText(movies.get(position).getShortOverview());
              itemViewHolder.title.setText(movies.get(position).getTitle());
              Picasso.get()
                      .load(movies.get(position).getPosterPath())
                      .placeholder(R.drawable.rsz_no_image)
                      .into(itemViewHolder.poster_image);

              itemViewHolder.layout.setOnClickListener(view -> startMovieExpandedActivity(position));

          }else {
              if(viewHolder instanceof LoadingViewHolder){
                  LoadingViewHolder loadingViewHolder = (LoadingViewHolder) viewHolder;
                  loadingViewHolder.progressBar.setVisibility(View.VISIBLE);
              }
          }
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

    /**
     * This is the ItemViewHolder for a simple item
     */
    class ItemViewHolder extends RecyclerView.ViewHolder{
      private ImageView poster_image;
      private TextView overview;
      private TextView title;
      private View layout;

     public ItemViewHolder(@NonNull View itemView) {
         super(itemView);
         poster_image = itemView.findViewById(R.id.image_simple_list);
         overview = itemView.findViewById(R.id.summary_simple_list);
         title = itemView.findViewById(R.id.title_simple_list);
         layout = itemView;
     }


 }

     class LoadingViewHolder extends RecyclerView.ViewHolder{

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView){
            super(itemView);
            progressBar = itemView.findViewById(R.id.loading_more_progress_bar);


        }
    }
    public interface MainActivityAdapterInterface {

        void requestLoading(int currentSize,String fragmentName);
    }

}
