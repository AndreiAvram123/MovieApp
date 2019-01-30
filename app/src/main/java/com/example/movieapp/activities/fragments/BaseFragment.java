package com.example.movieapp.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.movieapp.R;
import com.example.movieapp.activities.Model.CustomDivider;
import com.example.movieapp.activities.Model.Movie;
import com.example.movieapp.activities.adapters.MainAdapter;

import java.util.ArrayList;

/**
 *
 */
public class BaseFragment extends Fragment {
     public static final String KEY_MOVIES_ARRAY ="KEY_MOVIES_ARRAY";
     public RecyclerView recyclerView;
     public MainAdapter mainAdapter;
     public ArrayList<Movie> movies;
     private String fragmentName;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_base_fragment,container,false);
        movies = getArguments().getParcelableArrayList(KEY_MOVIES_ARRAY);
        recyclerView = view.findViewById(R.id.recycler_view_base_fragment);
        mainAdapter = new MainAdapter(recyclerView,getActivity(),fragmentName);

        initializeRecyclerView(container.getContext());
        return view;
    }

    public void loadMoreMovies(){
          ArrayList<Movie> moviesToAdd = new ArrayList<>();
          //initialize the index with the number of items
          // in the adapter minus 2 because we are
         //gonna get rid of the null value
          int index = mainAdapter.getItemCount() -2;
          int numberOfItemsToLoad = mainAdapter.getNumberOfItemsToLoad();
          while(index < index + numberOfItemsToLoad && index < movies.size())
          {
              moviesToAdd.add(movies.get(index));
              index ++;
          }
          mainAdapter.finishLoading(moviesToAdd);

    }
    public void setFragmentName(String fragmentName){
        this.fragmentName = fragmentName;
    }

    public void initializeRecyclerView(Context context){
        //add the first 6 movies
        for(int i =0;i< 6;i++)
            mainAdapter.addMovie(movies.get(i));
        recyclerView.setAdapter(mainAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration( new CustomDivider(20));
        recyclerView.setHasFixedSize(true);
    }

}
