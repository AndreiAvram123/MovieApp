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
import android.widget.TextView;

import com.example.movieapp.R;
import com.example.movieapp.activities.Model.Movie;

import java.util.ArrayList;

public class ViewPagerFragment extends Fragment {

    public static final String KEY_UPCOMING_MOVIES_ARRAY = "KEY_UPCOMING_MOVIES_ARRAY";
    public static final String KEY_POPULAR_MOVIES_ARRAY = "KEY_POPULAR_MOVIES_ARRAY";
    private ViewPager viewPager;
    private BaseFragment popularMoviesFragment;
    private BaseFragment upcomingMoviesFragment;


    /**
     * YOU NEED TO PUT THE ARRAYS INTO A BUNDLE
     * BECAUSE YOU ARE NOT SET THEM TO FIELDS VARIABLES
     * NON STATIC VARIABLES CANNOT BE ACCESSED FROM
     * A STATIC CONTEXT
     *
     * @param upcomingMovies
     * @param popularMovies
     * @return
     */
    public static ViewPagerFragment newInstance(ArrayList<Movie> upcomingMovies, ArrayList<Movie> popularMovies) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_POPULAR_MOVIES_ARRAY, popularMovies);
        bundle.putParcelableArrayList(KEY_UPCOMING_MOVIES_ARRAY, upcomingMovies);
        ViewPagerFragment viewPagerFragment = new ViewPagerFragment();
        viewPagerFragment.setArguments(bundle);
        return viewPagerFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.view_pager_fragment, container, false);

        ArrayList<Movie> upcomingMovies = getArguments().getParcelableArrayList(KEY_UPCOMING_MOVIES_ARRAY);
        ArrayList<Movie> popularMovies = getArguments().getParcelableArrayList(KEY_POPULAR_MOVIES_ARRAY);

        if (upcomingMovies != null && popularMovies != null) {
            popularMoviesFragment = BaseFragment.newInstance(popularMovies);
            upcomingMoviesFragment = BaseFragment.newInstance(upcomingMovies);
            viewPager = layout.findViewById(R.id.viewPager);
            initializeViewPager();
        } else {
            TextView noInternetMessage = layout.findViewById(R.id.error_text_view_pager);
            noInternetMessage.setVisibility(View.VISIBLE);
        }
        return layout;
    }

    private void initializeViewPager() {
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {

            @Override
            public Fragment getItem(int i) {
                switch (i) {
                    case 0:
                        return popularMoviesFragment;
                    case 1:
                        return upcomingMoviesFragment;
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "Popular Movies";
                    case 1:
                        return "Upcoming Movies";
                    default:
                        return null;
                }
            }
        });
    }
}
