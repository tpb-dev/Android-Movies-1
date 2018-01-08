package club.thatpetbff.android_movies_1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by randalltom on 1/3/18.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder>  {



    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }

    private final OnItemClickListener listener;

    public List<Movie> getmMovieList() {
        return mMovieList;
    }

    public void setmMovieList(List<Movie> mMovieList) {
        this.mMovieList = mMovieList;
    }

    public LayoutInflater getmInflater() {
        return mInflater;
    }

    public void setmInflater(LayoutInflater mInflater) {
        this.mInflater = mInflater;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    private List<Movie> mMovieList;
    private LayoutInflater mInflater;
    private Context mContext;

    public MoviesAdapter(Context context, OnItemClickListener listener)
    {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mMovieList = new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public MainActivity.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.row_movie, parent, false);
        MainActivity.MovieViewHolder viewHolder = new MainActivity.MovieViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MoviesAdapter.ViewHolder holder, int position)
    {
        Movie movie = mMovieList.get(position);
        holder.bind(movie, listener);

        // This is how we use Picasso to load images from the internet.
        Picasso.with(mContext)
                .load("http://image.tmdb.org/t/p/w185/" + movie.getPoster())
                .into(holder.imageView);
        System.out.println("Tried Picasso = " + position + ", " + movie.getPoster());
    }

    @Override
    public int getItemCount()
    {
        return (mMovieList == null) ? 0 : mMovieList.size();
    }

    public void setMovieList(List<Movie> movieList)
    {
        this.mMovieList.clear();
        this.mMovieList.addAll(movieList);
        // The adapter needs to know that the data has changed. If we don't call this, app will crash.
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;

        private ImageView imageView;

        public ViewHolder(View itemView)
        {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }


        public void bind(final Movie item, final OnItemClickListener listener) {
            name.setText(item.getTitle());
            Picasso.with(itemView.getContext()).load(item.getPoster_path()).into(imageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }

    }
}
