package com.example.movieapp.activities.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class MainAdapter extends RecyclerView.Adapter {

    private ArrayList<Movie> movies;
    private ArrayList<Movie> currentLoadedMovies;
    private boolean isLoading = false;
    private final int VIEW_TYPE_ITEM = 1;
    private int VIEW_TYPE_LOADING = 0;
    private Context context;
    //the default number of items to load after the current list
    //has been shown
    private int numberOfItemsToLoadOnRequest = 6;
    private int numberOfItemsToLoadFirst = 6;


    /**
     * Use this constructor if you want the adapter to load more
     * items.IMPLEMENT THE
     * @param recyclerView
    */

     public MainAdapter(RecyclerView recyclerView,ArrayList<Movie>movies)
    {   this.movies = movies;
         currentLoadedMovies = new ArrayList<>();
        //set the initial movies
        for(int i =0;i< numberOfItemsToLoadFirst && i <movies.size();i++)
            currentLoadedMovies.add(movies.get(i));

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
                        linearLayoutManager.findLastCompletelyVisibleItemPosition() == currentLoadedMovies.size()-1)
                {
                    isLoading = true;
                    loadMore();
                }
            }
        });
    }



    /**
     * Use this method to set a custom amount of items to load
     * The default amount  is 6
     */
    public void setNumberOfItemsToLoadOnRequest(int numberOfItemsToLoadOnRequest){
           this.numberOfItemsToLoadOnRequest = numberOfItemsToLoadOnRequest;
    }



    private void loadMore() {
        currentLoadedMovies.add(null);
        notifyDataSetChanged();
        Handler handler = new Handler();
        handler.postDelayed(()->{
            currentLoadedMovies.remove(null);
            int currentPosition = currentLoadedMovies.size();
            int endPosition = currentPosition + numberOfItemsToLoadOnRequest;
            while(currentPosition < endPosition &&
                    currentPosition < movies.size()){
                currentLoadedMovies.add(movies.get(currentPosition));
                currentPosition++;
            }
            isLoading = false;
            notifyDataSetChanged();
        },3000);

    }

    /**
     * This method is used to add an ArrayList<Movie> to
     * the field variable movies
     * IF THERE ARE NOT ENOUGH MOVIES LOADED,WE CALL THE
     * METHOD loadMore()
     * @param newMovies
     */
    public void addMovies(ArrayList<Movie> newMovies){
        movies.addAll(newMovies);
        if(currentLoadedMovies.size() < numberOfItemsToLoadFirst){
            loadMore();
        }
    }

    /**
     * Use this method to add a Movie object to the
     * movies field
     * IF THERE ARE NOT ENOUGH MOVIES LOADED,WE CALL THE
     * METHOD loadMore()

     * @param movie
     */
    public void addMovie(Movie movie){
        movies.add(movie);
        notifyDataSetChanged();
    }
    public void removeMovie(Movie movie){
        movies.remove(movie);
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

    /**
     * This method is called when the
     * adapter decides on the type
     * of ViewHolder to create
     * @param position
     * @return 1 for non null values
     *         0 for null values
     */
    @Override
    public int getItemViewType(int position) {
        if(currentLoadedMovies.get(position)!= null)
            return VIEW_TYPE_ITEM;
        else
            return VIEW_TYPE_LOADING;
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
          /*
            Check whether we have to create a viewHolder of type ItemViewHolder or
            LoadingViewHolder
           */
          if(viewHolder instanceof ItemViewHolder) {
              ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
              itemViewHolder.overview.setText(currentLoadedMovies.get(position).getShortOverview());
              itemViewHolder.title.setText(currentLoadedMovies.get(position).getTitle());

              //Load the image using the Picasso library
              Picasso.get()
                      .load(currentLoadedMovies.get(position).getPosterPath())
                      .placeholder(R.drawable.rsz_no_image)
                      .into(itemViewHolder.poster_image);

               //expand the current selected movie
              itemViewHolder.layout.setOnClickListener(view -> startMovieExpandedActivity(position));

          }else {

              if(viewHolder instanceof LoadingViewHolder){
                  LoadingViewHolder loadingViewHolder = (LoadingViewHolder) viewHolder;
                  loadingViewHolder.progressBar.setVisibility(View.VISIBLE);
              }
          }
    }

    private void startMovieExpandedActivity(int position) {
        Intent startExtendedMovieActivityIntent = new Intent(context, MovieExpandedActivity.class);
        startExtendedMovieActivityIntent.putExtra(Constraints.KEY_MOVIE,currentLoadedMovies.get(position));
        context.startActivity(startExtendedMovieActivityIntent);
    }

    @Override
    public int getItemCount() {
        return currentLoadedMovies.size();
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



}
