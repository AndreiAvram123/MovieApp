package com.example.movieapp.activities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.example.movieapp.R;
import com.example.movieapp.activities.Model.Constraints;
import com.example.movieapp.activities.Model.Movie;
import com.example.movieapp.activities.activities.MovieExpandedActivity;
import com.example.movieapp.activities.adapters.GridAdapter;

import java.util.ArrayList;
import java.util.List;

public class SavedMoviesFragment  extends Fragment{

    private  TextView noMoviesMessage;
    private  GridAdapter gridAdapter;
    private  ArrayList<Movie> movies;

    public static SavedMoviesFragment newInstance(List<Movie> savedMovies){
        SavedMoviesFragment savedMoviesFragment = new SavedMoviesFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constraints.KEY_SAVED_MOVIES,new ArrayList<>(savedMovies));
        savedMoviesFragment.setArguments(bundle);
        return savedMoviesFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_saved_movies_fragment,container,
                false);

        movies  = getArguments().getParcelableArrayList(Constraints.KEY_SAVED_MOVIES);

        noMoviesMessage= view.findViewById(R.id.no_movies_saved_fragment);
        GridView gridView = view.findViewById(R.id.gridview);
        gridAdapter = new GridAdapter(movies);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener((adapterView, view1, i, l) -> {
            Intent intent = new Intent(container.getContext(), MovieExpandedActivity.class);
            intent.putExtra(Constraints.KEY_MOVIE,movies.get(i));
            startActivity(intent);
        });

        if(!movies.isEmpty()) {

        }else {

            noMoviesMessage.setVisibility(View.VISIBLE);
        }
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

    }

}
