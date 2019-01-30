package com.example.movieapp.activities.activities;

import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.movieapp.R;
import com.example.movieapp.activities.Model.AppDatabase;
import com.example.movieapp.activities.Model.Constraints;
import com.example.movieapp.activities.Model.Movie;
import com.example.movieapp.activities.Model.Useful;
import com.example.movieapp.activities.interfaces.DatabaseInterface;
import com.squareup.picasso.Picasso;

public class MovieExpandedActivity extends AppCompatActivity {
    private LinearLayout linearLayoutStars;
    private TextView title;
    private Movie movie;
    private ImageView image;
    private TextView releaseDate;
    private TextView genres;
    private TextView summary;
    private ImageView isSavedImage;
    private DatabaseInterface databaseInterface;
    private Thread backgroundThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_expanded);

        initializeDatabase();

        initializeViews();

        updateUI();




    }

    private void initializeDatabase() {
        AppDatabase appDatabase = Room.databaseBuilder(this,
                AppDatabase.class,"movies_database").build();
        databaseInterface = appDatabase.databaseInterface();
    }

    private void updateUI() {
        movie = getIntent().getParcelableExtra(Constraints.KEY_MOVIE);
        title.setText(movie.getTitle());
        releaseDate.setText(movie.getReleaseDate());
        summary.setText(movie.getOverview());
        genres.setText(movie.getGenres());

        loadRating();
        loadImage();

        Useful.makeActivityFullscreen(getWindow());

        checkMovieInDatabase();

        configureImageListener();
    }

    private void checkMovieInDatabase() {
        backgroundThread = new Thread(
                ()-> {
                    if (databaseInterface.getMovie(movie.getMovieID())
                            != null) {
                        movie.setSaved(true);
                       runOnUiThread(()-> isSavedImage.setImageResource(R.drawable.ic_favorite_yellow_30dp));
                    }
                });
        backgroundThread.start();
    }


    private void configureImageListener() {
        isSavedImage.setOnClickListener(view ->{
            if(movie.isSaved()){
                //delete saved movie from database
                deleteMovieFromDatabase();
                movie.setSaved(false);
                isSavedImage.setImageResource(R.drawable.ic_favorite_border_white_30dp);

            }else {
                insertMovieIntoDatabase();
                movie.setSaved(true);
                isSavedImage.setImageResource(R.drawable.ic_favorite_yellow_30dp);
            }
        });
    }

    private void insertMovieIntoDatabase() {
       backgroundThread = new Thread(
               ()-> databaseInterface.insertMovie(movie));

       backgroundThread.start();
    }

    private void deleteMovieFromDatabase() {
        backgroundThread = new Thread(
                ()-> databaseInterface.deleteMovieById(movie.getMovieID()));
        backgroundThread.start();
    }


    private void loadImage() {
        Picasso.get()
                .load(movie.getPosterPath())
                .placeholder(R.drawable.rsz_no_image)
                .into(image);
    }

    private void loadRating() {
        int position =0;
        while(position < (int)movie.getRating()){
            ImageView star = (ImageView) linearLayoutStars.getChildAt(position);
            star.setImageResource(R.drawable.ic_star_white_24dp);
            position++;
        }
        position--;
        if(movie.getRating() != (int)movie.getRating()) {
            ImageView star = (ImageView) linearLayoutStars.getChildAt(position);
            star.setImageResource(R.drawable.ic_star_half_white_24dp);
        }

    }

    private void initializeViews() {
        isSavedImage = findViewById(R.id.isFavorite_image_expanded);
        summary = findViewById(R.id.summary_expanded_movie);
        title = findViewById(R.id.title_expanded_activity);
        linearLayoutStars = findViewById(R.id.linear_layout_stars);
        image = findViewById(R.id.image_movie_expanded);
        releaseDate = findViewById(R.id.release_date_movie_expanded);
        genres = findViewById(R.id.genre_movie_expanded);
        ImageView backButton = findViewById(R.id.back_button_movie_expanded);
        //finish the activity when the imageView is pressed
        backButton.setOnClickListener(button -> finish());

    }
}

