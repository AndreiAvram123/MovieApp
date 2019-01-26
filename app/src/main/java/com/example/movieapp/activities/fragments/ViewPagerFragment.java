package com.example.movieapp.activities.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.movieapp.R;
import com.example.movieapp.activities.Model.Constraints;
import com.example.movieapp.activities.Model.Movie;

import java.util.ArrayList;

public class ViewPagerFragment extends Fragment {

    private ViewPager viewPager;

    /**
     *  YOU NEED TO PUT THE ARRAYS INTO A BUNDLE
     *  BECAUSE YOU ARE NOT SET THEM TO FIELDS VARIABLES
     *  NON STATIC VARIABLES CANNOT BE ACCESSED FROM
     *  A STATIC CONTEXT
     * @param upcomingMovies
     * @param popularMovies
     * @return
     */
    public static ViewPagerFragment newInstance(ArrayList<Movie> upcomingMovies,ArrayList<Movie> popularMovies){
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(Constraints.KEY_POPULAR_MOVIES_ARRAY,popularMovies);
        bundle.putParcelableArrayList(Constraints.KEY_UPCOMING_MOVIES_ARRAY,upcomingMovies);
        ViewPagerFragment viewPagerFragment = new ViewPagerFragment();
        viewPagerFragment.setArguments(bundle);
        return viewPagerFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_view_pager_fragment,container,false);
        viewPager = view.findViewById(R.id.viewPager);
        ArrayList<Movie> upcomingMovies = getArguments().getParcelableArrayList(Constraints.KEY_UPCOMING_MOVIES_ARRAY);
        ArrayList<Movie> popularMovies = getArguments().getParcelableArrayList(Constraints.KEY_POPULAR_MOVIES_ARRAY);
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {

            @Override
            public Fragment getItem(int i) {
                switch (i){
                    case 0: return BaseFragment.newInstance(popularMovies);
                    case 1: return BaseFragment.newInstance(upcomingMovies);
                    default: return null;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position){
                    case 0 :return "Popular Movies";
                    case 1: return "Upcoming Movies";
                    default: return null;
                }
            }
        });
        return view;
    }
}
