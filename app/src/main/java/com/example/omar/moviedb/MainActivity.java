package com.example.omar.moviedb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ListFragment.OnFragmentInteractionListener {

    private final String url = "http://api.themoviedb.org/3/";
    private static final String API_KEY = "a24006249d677ed0e13a4020aa1dc5e6";

    private ResponseReceiver receiver;

    ListView movieList;


    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(receiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieList = (ListView)findViewById(R.id.moviesList);

        //registering a local broadcast receiver that is activated when "movies_fetched"
        //action happens
        IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ResponseReceiver();
        registerReceiver(receiver, filter);


        // starting an intent service that will fetch the list of movies from the URL below
        String complete_url = url+ "movie/now_playing?api_key="+API_KEY+"&language=en-US&page=1";
        Intent msgIntent = new Intent(this, DownloadJSON.class);
        msgIntent.setAction(DownloadJSON.ACTION_DOWNLOAD);
        msgIntent.putExtra(DownloadJSON.URL, complete_url);
        startService(msgIntent);

        // after IntentService is done we will receive a broadcast telling us that it is time to fetch the list of movies from the db
    }


    //this is called when we know that the list ready
    public void displayMovies(){
        DatabaseManager manager = new DatabaseManager(this);

        manager.open();

        List<Movie> arrayOfMovies = manager.getAllRecords();

// Create the adapter to convert the array to views

        MovieAdapter adapter = new MovieAdapter(this, (ArrayList<Movie>)arrayOfMovies);

// Attach the adapter to a ListView

        ListView listView = (ListView) findViewById(R.id.moviesList);

        listView.setAdapter(adapter);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    // this adapter takes an ArrayList of movies and outputs it into a ListView
    public class MovieAdapter extends ArrayAdapter<Movie> {

        public MovieAdapter(Context context, ArrayList<Movie> movies) {

            super(context, 0, movies);

        }



        //this is where you would add the like button
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {



            // Get the data item for this position

            Movie movie = getItem(position);

            // Check if an existing view is being reused, otherwise inflate the view

            if (convertView == null) {
                //the like button would need to be in single_movie.xml
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_movie, parent, false);

            }

            // Lookup view for data population

            TextView title = (TextView) convertView.findViewById(R.id.title);

            TextView date = (TextView) convertView.findViewById(R.id.date);

            TextView rating = (TextView) convertView.findViewById(R.id.rating);

            Button button = (Button) convertView.findViewById(R.id.button);

            // Per each view we fetch info from the corresponding movie and set it
            title.setText(movie.getTitle());

            date.setText(movie.getDate());

            rating.setText(movie.getRating() + "");

            // Return the completed view to render on screen

            return convertView;

        }

    }

    // this BroadcastReceiver is waiting for DownloadJSON (IntentService) to issue a broadcast

    public class ResponseReceiver extends BroadcastReceiver {
        public static final String ACTION_RESP =
                "movies_fetched";

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("demoapp","movies are fetched");

            displayMovies();
        }
    }

}