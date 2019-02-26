package com.example.movieapp.activities.fragments;

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
import com.example.movieapp.activities.Model.Movie;
import com.example.movieapp.activities.adapters.GridAdapter;

import java.util.ArrayList;
import java.util.List;

public class SavedMoviesFragment extends Fragment {

    private TextView noMoviesMessage;
    private GridAdapter gridAdapter;
    public static final String KEY_SAVED_MOVIES = "KEY_SAVED_MOVIES";
    public static final String FRAGMENT_TAG = "SAVED_MOVIES_FRAGMENT_TAG";


    public static SavedMoviesFragment newInstance(List<Movie> savedMovies) {
        SavedMoviesFragment savedMoviesFragment = new SavedMoviesFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_SAVED_MOVIES, new ArrayList<>(savedMovies));
        savedMoviesFragment.setArguments(bundle);
        return savedMoviesFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.saved_movies_fragment, container,
                false);

        ArrayList<Movie> savedMovies = getArguments().getParcelableArrayList(KEY_SAVED_MOVIES);


        if (savedMovies.isEmpty()) {
            noMoviesMessage = layout.findViewById(R.id.no_movies_saved_fragment);
            noMoviesMessage.setVisibility(View.VISIBLE);
        } else {
            initializeUI(layout, savedMovies);
        }


        return layout;
    }

    private void initializeUI(View layout, ArrayList<Movie> savedMovies) {
        noMoviesMessage = layout.findViewById(R.id.no_movies_saved_fragment);

        GridView gridView = layout.findViewById(R.id.gridview);
        gridAdapter = new GridAdapter(savedMovies);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener((adapterView, view1, i, l) -> getFragmentManager().beginTransaction()
                .replace(R.id.placeholder_layout_main, ExpandedMovieFragment.newInstance(savedMovies.get(i)))
                .addToBackStack(null)
                .commit());

    }

    /**
     * This is just an UI method
     * Once the user has added a movie to his saved
     * ones we should update the list of saved movies in
     * the adapter
     */
    public void addMovie(Movie movie) {
        gridAdapter.addMovie(movie);

    }

    /**
     * This is just an UI method
     * Once the user has removed from his saved
     * ones we should update list of saved movies in
     * the adapter
     */
    public void removeMovie(Movie movie) {
        gridAdapter.removeMovie(movie);
        //if there are no more movies in the adapter then inform
        //the user that there are no saved movies
        if (gridAdapter.isListEmpty()) {
            noMoviesMessage.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onStart() {
        super.onStart();

    }

}
