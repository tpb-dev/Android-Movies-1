package club.thatpetbff.android_movies_1;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

/**
 * Created by rtom on 1/8/18.
 */

public class DetailActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String value = intent.getStringExtra("key"); //if it's a string you stored.
    }
}
