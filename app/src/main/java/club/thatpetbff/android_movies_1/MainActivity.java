package club.thatpetbff.android_movies_1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;

import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MoviesAdapter mAdapter;
    boolean popularity = false;
    Context mContext;

    OkHttpClient client = new OkHttpClient();

    public String url= "https://api.themoviedb.org/3/movie/popular?api_key=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        //mAdapter = new MoviesAdapter(this);
        //mRecyclerView.setAdapter(mAdapter);

        mContext = this;
        mAdapter = new MoviesAdapter(this, new MoviesAdapter.OnItemClickListener() {
            @Override public void onItemClick(Movie item) {
                //Toast.makeText(mContext, "Item Clicked - " + item.getTitle(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(mContext, DetailActivity.class);
                String message = item.getTitle();
                intent.putExtra("MyClass", item);
                startActivity(intent);

            }
        });
        mRecyclerView.setAdapter(mAdapter);

        OkHttpHandler okHttpHandler= new OkHttpHandler();
        okHttpHandler.execute(url);




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt1 = "Sorted by Popularited";
                String txt2 = "Sorted by Ratings";
                Snackbar.make(view, popularity ? txt2 : txt1, Snackbar.LENGTH_LONG).show();
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
        mAdapter = new MoviesAdapter(this, new MoviesAdapter.OnItemClickListener() {
            @Override public void onItemClick(Movie item) {
                //Toast.makeText(mContext, "Item Clicked - " + item.getTitle(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(mContext, DetailActivity.class);
                String message = item.getTitle();
                intent.putExtra("MyClass", item);
                startActivity(intent);

            }
        });
        mAdapter.setMovieList(temp);
        mRecyclerView.setAdapter(mAdapter);
//        mAdapter.setMovieList(temp);
        popularity = !popularity;

    }

    public class OkHttpHandler extends AsyncTask {

        OkHttpClient client = new OkHttpClient();

        public boolean isOnline() {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }

        @Override
        protected String doInBackground(Object... params) {

            Request.Builder builder = new Request.Builder();
            builder.url((String)params[0]);
            Request request = builder.build();

            if(isOnline()) {

                try {
                    Response response = client.newCall(request).execute();
                    return response.body().string();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Snackbar.make(mRecyclerView, "No internet connection detected", Snackbar.LENGTH_LONG).show();
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
        if(obj != null) {
            for (Movie a : obj.getResults()) {
                System.out.println("Movie: " + a.getTitle());
            }

            mAdapter.setMovieList(obj.getResults());
            System.out.println("Set the list to adapter");
        } else {
            Snackbar.make(mRecyclerView, "No internet connection detected", Snackbar.LENGTH_LONG).show();
        }
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
