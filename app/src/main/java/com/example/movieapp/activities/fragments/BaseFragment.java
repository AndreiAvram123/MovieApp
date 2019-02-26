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
    public static final String KEY_MOVIES_ARRAY = "KEY_MOVIES_ARRAY";
    public RecyclerView recyclerView;
    public MainAdapter mainAdapter;
    public ArrayList<Movie> movies;

    public static BaseFragment newInstance(ArrayList<Movie> movies) {
        BaseFragment baseFragment = new BaseFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_MOVIES_ARRAY, movies);
        baseFragment.setArguments(bundle);
        return baseFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_base_fragment, container, false);
        movies = getArguments().getParcelableArrayList(KEY_MOVIES_ARRAY);
        recyclerView = view.findViewById(R.id.recycler_view_base_fragment);
        mainAdapter = new MainAdapter(recyclerView, movies, getActivity());

        initializeRecyclerView(container.getContext());
        return view;
    }


    public void initializeRecyclerView(Context context) {
        recyclerView.setAdapter(mainAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new CustomDivider(20));
        recyclerView.setHasFixedSize(true);
    }

}
