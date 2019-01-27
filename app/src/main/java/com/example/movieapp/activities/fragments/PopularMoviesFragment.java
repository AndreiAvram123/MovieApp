package com.example.movieapp.activities.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.movieapp.activities.Model.Constraints;
import com.example.movieapp.activities.Model.CustomDivider;
import com.example.movieapp.activities.Model.Movie;
import com.example.movieapp.activities.adapters.MainActivityAdapter;

import java.util.ArrayList;

public class PopularMoviesFragment extends BaseFragment {

    public static PopularMoviesFragment newInstance(ArrayList<Movie> movies){
        Bundle bundle  = new Bundle();
        bundle.putParcelableArrayList(KEY_MOVIES_ARRAY,movies);
        PopularMoviesFragment popularMoviesFragment = new PopularMoviesFragment();
        popularMoviesFragment.setArguments(bundle);
        return popularMoviesFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mainActivityAdapter = new MainActivityAdapter(recyclerView,getActivity(), Constraints.POPULAR_MOVIES_FRAGMENT);
        //add the first 6 movies
        for(int i =0;i<6;i++)
            mainActivityAdapter.addMovie(movies.get(i));

        recyclerView.setAdapter(mainActivityAdapter);

        return view;
    }
}
