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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.movieapp.R;
import com.example.movieapp.activities.Model.Constraints;
import com.example.movieapp.activities.Model.CustomDivider;
import com.example.movieapp.activities.Model.Movie;
import com.example.movieapp.activities.adapters.MainAdapter;

import java.util.ArrayList;
import java.util.List;

public class SavedMoviesFragment  extends Fragment{

    private ArrayList<Movie> movies;
    private RecyclerView recyclerView;
    private MainAdapter mainAdapter;
    private TextView noMoviesMessage;

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
        if(!movies.isEmpty()) {
            recyclerView = view.findViewById(R.id.recyclerView_saved_movies);
            mainAdapter = new MainAdapter(recyclerView, getActivity(),
                    Constraints.SAVED_MOVIES_FRAGMENT);
            mainAdapter.addMovies(movies);
            initializeRecyclerView(container.getContext());
        }else {
            noMoviesMessage = view.findViewById(R.id.no_movies_saved_fragment);
            noMoviesMessage.setVisibility(View.VISIBLE);
        }
        return view;
    }

    private void initializeRecyclerView(Context context) {
        recyclerView.setAdapter(mainAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration( new CustomDivider(20));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
