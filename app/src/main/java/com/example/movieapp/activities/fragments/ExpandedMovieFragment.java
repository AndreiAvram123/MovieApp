package com.example.movieapp.activities.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.movieapp.R;
import com.example.movieapp.activities.Model.Movie;
import com.squareup.picasso.Picasso;

public class ExpandedMovieFragment extends Fragment {

    private static final String KEY_MOVIE = "KEY_MOVIE";
    private LinearLayout linearLayoutStars;
    private TextView title;
    private Movie movie;
    private ImageView image;
    private TextView releaseDate;
    private TextView genres;
    private TextView summary;
    private ImageView saveMovieImage;
    private ExpandedMovieFragmentInterface expandedMovieFragmentInterface;
    private View layout;

    public static ExpandedMovieFragment newInstance(Movie movie) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_MOVIE, movie);
        ExpandedMovieFragment expandedMovieFragment = new ExpandedMovieFragment();
        expandedMovieFragment.setArguments(bundle);
        return expandedMovieFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layout = inflater.inflate(R.layout.fragment_movie_expanded, container, false);

        initializeUI();

        movie = getArguments().getParcelable(KEY_MOVIE);

        if (movie != null) {
            updateUI();
        }

        return layout;
    }

    private void initializeUI() {
        initializeViews();
        configureBackButton();
        expandedMovieFragmentInterface = (ExpandedMovieFragmentInterface) getActivity();
    }

    private void configureBackButton() {
        ImageView backButton = layout.findViewById(R.id.back_button_movie_expanded);
        backButton.setOnClickListener(button -> getFragmentManager().popBackStack());
    }

    private void initializeViews() {

        saveMovieImage = layout.findViewById(R.id.isFavorite_image_expanded);
        summary = layout.findViewById(R.id.summary_expanded_movie);
        title = layout.findViewById(R.id.title_expanded_activity);
        linearLayoutStars = layout.findViewById(R.id.linear_layout_stars);
        image = layout.findViewById(R.id.image_movie_expanded);
        releaseDate = layout.findViewById(R.id.release_date_movie_expanded);
        genres = layout.findViewById(R.id.genre_movie_expanded);

    }

    private void updateUI() {
        title.setText(movie.getTitle());
        releaseDate.setText(movie.getReleaseDate());
        summary.setText(movie.getOverview());
        genres.setText(movie.getGenres());
        loadRating();
        loadImage();
        configureSaveImage();
        configureImageListener();
    }

    private void configureSaveImage() {
        if (movie.isSaved()) {
            saveMovieImage.setImageResource(R.drawable.ic_favorite_yellow_30dp);
        } else {
            saveMovieImage.setImageResource(R.drawable.ic_favorite_border_white_30dp);
        }
    }

    /**
     * This method uses the Picasso library
     * to directly download and load an image
     * into a imageView
     */
    private void loadImage() {
        Picasso.get()
                .load(movie.getPosterPath())
                .placeholder(R.drawable.rsz_no_image)
                .into(image);
    }


    //TODO
    //explain method and refactor
    private void loadRating() {
        int position = 0;
        while (position < (int) movie.getRating()) {
            ImageView star = (ImageView) linearLayoutStars.getChildAt(position);
            star.setImageResource(R.drawable.ic_star_yellow_24dp);
            position++;
        }
        position--;
        if (movie.getRating() != (int) movie.getRating()) {
            ImageView star = (ImageView) linearLayoutStars.getChildAt(position);
            star.setImageResource(R.drawable.ic_star_half_yellow_24dp);
        }

    }

    private void configureImageListener() {
        saveMovieImage.setOnClickListener(view -> {
            if (movie.isSaved()) {
                expandedMovieFragmentInterface.deleteMovie(movie);
                movie.setSaved(false);
                saveMovieImage.setImageResource(R.drawable.ic_favorite_border_white_30dp);
                showSnackbar(getString(R.string.movie_removed));
            } else {
                expandedMovieFragmentInterface.saveMovie(movie);
                movie.setSaved(true);
                saveMovieImage.setImageResource(R.drawable.ic_favorite_yellow_30dp);
                showSnackbar(getString(R.string.movie_added));
            }
        });
    }

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public interface ExpandedMovieFragmentInterface {
        void saveMovie(Movie movie);

        void deleteMovie(Movie movie);
    }


}
