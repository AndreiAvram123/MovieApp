package com.example.movieapp.activities.activities;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.movieapp.R;
import com.example.movieapp.activities.Model.Constraints;
import com.example.movieapp.activities.Model.Movie;
import com.squareup.picasso.Picasso;

public class MovieExpandedActivity extends AppCompatActivity {
    private LinearLayout linearLayoutStars;
    private TextView title;
    private Movie movie;
    private ImageView image;
    private TextView releaseDate;
    private TextView genre;
    private TextView summary;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_expanded);

        initializeViews();

        movie = getIntent().getParcelableExtra(Constraints.KEY_MOVIE);

        title.setText(movie.getTitle());
        releaseDate.setText(movie.getReleaseDate());
        summary.setText(movie.getOverview());

        loadRating();

        loadImage();

        loadGenres();

    }

    private void loadGenres() {
        //build the genres String
        StringBuilder stringBuilder = new StringBuilder();
        int [] genres = movie.getGenres();
        for(int i =0;i < genres.length -1;i++){
            stringBuilder.append(Constraints.getGenre(genres[i]) + " , ");
        }
        stringBuilder.append(Constraints.getGenre(genres[genres.length-1]));

        genre.setText(stringBuilder.toString());
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
        summary = findViewById(R.id.summary_expanded_movie);
        title = findViewById(R.id.title_expanded_activity);
        linearLayoutStars = findViewById(R.id.linear_layout_stars);
        image = findViewById(R.id.image_movie_expanded);
        releaseDate = findViewById(R.id.release_date_movie_expanded);
        genre = findViewById(R.id.genre_movie_expanded);
        backButton = findViewById(R.id.back_button_movie_expanded);
        //finish the activity when the imageView is pressed
        backButton.setOnClickListener(button -> finish());

    }
}

