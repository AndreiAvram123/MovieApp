package com.example.movieapp.activities.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.movieapp.R;
import com.example.movieapp.activities.Model.CustomDivider;
import com.example.movieapp.activities.Model.Movie;
import com.example.movieapp.activities.adapters.MainAdapter;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView error_message_search;
    public static final String KEY_SEARCHED_MOVIES = "KEY_SEARCHED_MOVIES";

    public static SearchFragment newInstance(ArrayList<Movie> searchedMovies) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_SEARCHED_MOVIES, searchedMovies);
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.setArguments(bundle);
        return searchFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_search, container, false);
        initializeUI(layout);
        ArrayList<Movie> searchedMovies = getArguments().getParcelableArrayList(KEY_SEARCHED_MOVIES);
        if (searchedMovies == null) {
            displayErrorMessage(getString(R.string.no_internet_connection));
        } else {
            if (searchedMovies.isEmpty()) {
                displayErrorMessage(getString(R.string.error_no_result));
            } else {
                createRecyclerView(searchedMovies);
            }
        }

        return layout;
    }

    private void displayErrorMessage(String message) {
        error_message_search.setVisibility(View.VISIBLE);
        error_message_search.setText(message);
    }

    private void createRecyclerView(ArrayList<Movie> searchedMovies) {

        recyclerView.setAdapter(new MainAdapter(recyclerView, searchedMovies, getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new CustomDivider(20));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    private void initializeUI(View layout) {
        recyclerView = layout.findViewById(R.id.recycler_view_search);
        error_message_search = layout.findViewById(R.id.error_message_search);
        ImageView back_image = layout.findViewById(R.id.back_image_seach);
        back_image.setOnClickListener(view -> getFragmentManager().popBackStack());
    }


}
