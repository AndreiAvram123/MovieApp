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

import com.example.movieapp.R;
import com.example.movieapp.activities.Model.CustomDivider;
import com.example.movieapp.activities.Model.Movie;
import com.example.movieapp.activities.adapters.MainActivityAdapter;

import java.util.ArrayList;

/**
 * This is a base fragment used
 * to create other fragments
 */
public class BaseFragment extends Fragment {
     public static final String KEY_MOVIES_ARRAY ="KEY_MOVIES_ARRAY";
     private RecyclerView recyclerView;
    public static BaseFragment newInstance(ArrayList<Movie> movies){
        Bundle bundle  = new Bundle();
        bundle.putParcelableArrayList(KEY_MOVIES_ARRAY,movies);
        BaseFragment baseFragment = new BaseFragment();
        baseFragment.setArguments(bundle);
        return baseFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_base_fragment,container,false);
        recyclerView = view.findViewById(R.id.recycler_view_base_fragment);
        ArrayList<Movie> movies = getArguments().getParcelableArrayList(KEY_MOVIES_ARRAY);
        recyclerView.setAdapter(new MainActivityAdapter(movies));
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        recyclerView.addItemDecoration( new CustomDivider(15));
        recyclerView.setHasFixedSize(true);
        return view;
    }
}
