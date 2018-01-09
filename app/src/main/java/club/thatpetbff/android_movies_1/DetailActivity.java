package club.thatpetbff.android_movies_1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Movie movie = (Movie) getIntent().getSerializableExtra("MyClass");

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
//        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        TextView userRating = findViewById(R.id.userRating);
        userRating.setText("User Rating : " + movie.getVote_average());

        TextView releaseDate = findViewById(R.id.releaseDate);
        releaseDate.setText("Release Date : " + movie.getRelease_date());

        TextView originalTitle = findViewById(R.id.originalTitle);
        originalTitle.setText("Original Title : " + movie.getOriginal_title());

        TextView overview = findViewById(R.id.overview);
        overview.setText("Synposis : " + movie.getOverview());

        ImageView thumbnail = findViewById(R.id.thumbnail);

        Picasso.with(this).load("http://image.tmdb.org/t/p/w185/" + movie.getPoster()).into(thumbnail);

        System.out.println("Movie Mopvie = " + movie.getTitle());

    }
}
