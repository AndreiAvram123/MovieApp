package com.example.movieapp.activities.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.movieapp.activities.Model.Constraints;
import com.example.movieapp.activities.Model.Movie;

import java.util.ArrayList;

public class UpcomingMoviesFragment extends BaseFragment {

    public static UpcomingMoviesFragment newInstance(ArrayList<Movie> movies){
        Bundle bundle  = new Bundle();
        bundle.putParcelableArrayList(KEY_MOVIES_ARRAY,movies);
        UpcomingMoviesFragment upcomingMoviesFragment = new UpcomingMoviesFragment();
        upcomingMoviesFragment.setArguments(bundle);
        return upcomingMoviesFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.setFragmentName(Constraints.UPCOMING_MOVIES_FRAGMENT);
        View view = super.onCreateView(inflater, container, savedInstanceState);


        return view;
    }
}
