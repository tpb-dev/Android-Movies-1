//https://developer.android.com/training/basics/firstapp/starting-activity.html

package club.thatpetbff.android_movies_1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MoviesAdapter mAdapter;
    boolean sorty = false;
    boolean popularity = false;

    OkHttpClient client = new OkHttpClient();

    public String url= "https://api.themoviedb.org/3/movie/popular?api_key=8332a17f0431de65798214096334a4b6";

    public static class MovieViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView imageView;
        public MovieViewHolder(View itemView)
        {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mAdapter = new MoviesAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        OkHttpHandler okHttpHandler= new OkHttpHandler();
        okHttpHandler.execute(url);




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Sorting", Snackbar.LENGTH_LONG).show();
                sortArray();
            }
        });
    }

    public void sortArray() {
        System.out.println("Reached sort");
        List<Movie> temp = mAdapter.getmMovieList();
        System.out.println(temp);
        if(!popularity)
            Collections.sort(temp, (m1, m2) -> m1.getPopularity().compareTo(m2.getPopularity()));
        else
            Collections.sort(temp, (m1, m2) -> m1.getVote_average().compareTo(m2.getVote_average()));
        System.out.println(temp);
        mAdapter = new MoviesAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setMovieList(temp);
        popularity = !popularity;

    }

    public class OkHttpHandler extends AsyncTask {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(Object... params) {

            Request.Builder builder = new Request.Builder();
            builder.url((String)params[0]);
            Request request = builder.build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object s) {
            String a = (String)s;
            super.onPostExecute(a);
            processJSON(a);
        }

    }

    public void processJSON(String json) {
        System.out.println(json);
        Gson gson = new Gson();
        MovieResponse obj = gson.fromJson(json, MovieResponse.class);
        for(Movie a : obj.getResults()) {
            System.out.println("Movie: " + a.getTitle());
        }

        mAdapter.setMovieList(obj.getResults());
        System.out.println("Set the list to adapter");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
