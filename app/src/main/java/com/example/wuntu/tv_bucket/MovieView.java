package com.example.wuntu.tv_bucket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.wuntu.tv_bucket.Adapters.CastDetailAdapter;
import com.example.wuntu.tv_bucket.Adapters.MoviesAdapter_OnClickListener;
import com.example.wuntu.tv_bucket.Models.Cast;
import com.example.wuntu.tv_bucket.Models.MovieDetailFull;
import com.example.wuntu.tv_bucket.Models.TvExampleModel;
import com.example.wuntu.tv_bucket.Utils.AppSingleton;
import com.example.wuntu.tv_bucket.Utils.UrlConstants;
import com.example.wuntu.tv_bucket.Utils.Utility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MovieView extends AppCompatActivity {

    String image_url;
    Toolbar toolbar;
    RecyclerView recycler_view;
    CastDetailAdapter castDetailAdapter;
    UrlConstants urlConstants = UrlConstants.getSingletonRef();
    private Gson gson;
    MovieDetailFull movieDetailFull;
    CoordinatorLayout coordinator_layout_movie_view;
    LinearLayout season_layout;
    LinearLayout overview_layout,facts_layout;

    TvExampleModel tvExampleModel;
    TextView play_trailer;

    ArrayList<Cast> castArrayList;
    ArrayList<Cast> subCastArrayList;

    TextView ratings,overview,status,release_date,genre1,budget,revenue,runtime,homepage;
    ImageView backdrop_image,image_play_trailer;
    TextView budget_title,revenue_title,current_season,season_number,current_season_year,current_season_episodes,current_season_tagline,view_all_seasons;
    TextView release_date_title,overview_title,status_title,genres_title,runtime_title,homepage_title,facts;
    String url2;
    String ID;
    int i;
    StringBuilder sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_view);

        initviews();



        movieDetailFull = new MovieDetailFull();
        castArrayList = new ArrayList<>();
        subCastArrayList = new ArrayList<>();
        tvExampleModel = new TvExampleModel();






        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ID = getIntent().getStringExtra("ID");


        play_trailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MovieView.this,YoutubeActivity.class);
                intent.putExtra("ID",ID);
                startActivity(intent);
            }
        });

        castDetailAdapter = new CastDetailAdapter(MovieView.this,castArrayList,subCastArrayList);

        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(castDetailAdapter);


        recycler_view.addOnItemTouchListener(
                new MoviesAdapter_OnClickListener(MovieView.this, recycler_view ,new MoviesAdapter_OnClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position)
                    {
                        if (position == subCastArrayList.size() - 1)
                        {

                        }
                        else
                        {
                            Intent intent = new Intent(MovieView.this,CastViewActivity.class);
                            intent.putExtra("ID",subCastArrayList.get(position).getId());
                            intent.putExtra("EVENT","TOUCH EVENT");
                            startActivity(intent);
                        }



                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever

                    }
                })
        );

        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        final String view = getIntent().getStringExtra("VIEW");
        if (view.equals("MOVIE"))
        {
            season_layout.setVisibility(View.GONE);
            budget_title.setVisibility(View.VISIBLE);
            budget.setVisibility(View.VISIBLE);
            revenue_title.setVisibility(View.VISIBLE);
            revenue.setVisibility(View.VISIBLE);
            release_date_title.setText("Release Date");
            String url = urlConstants.Movie_1st_URL + ID + urlConstants.Movie_2nd_URL;
            prepareMovieData(url);
        }
        else if (view.equals("TV"))
        {
            season_layout.setVisibility(View.VISIBLE);
            budget_title.setVisibility(View.GONE);
            budget.setVisibility(View.GONE);
            revenue.setVisibility(View.GONE);
            revenue_title.setVisibility(View.GONE);
            String url = urlConstants.TV_1st_URL + ID + urlConstants.TV_2nd_URL;
            release_date_title.setText("First Air Date");
            prepareTvData(url);
        }

        play_trailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View View) {
                if (view.equals("MOVIE"))
                {
                    Intent intent = new Intent(MovieView.this,YoutubeActivity.class);
                    intent.putExtra("ID",ID);
                    startActivity(intent);
                }
                else Toast.makeText(MovieView.this, "Under Development", Toast.LENGTH_SHORT).show();

            }
        });

        view_all_seasons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MovieView.this, "Under Development", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void initviews()
    {
        image_play_trailer= (ImageView)findViewById(R.id.image_play_trailer);
        coordinator_layout_movie_view = (CoordinatorLayout) findViewById(R.id.coordinator_layout_movie_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        season_layout = (LinearLayout)findViewById(R.id.season_layout);
        play_trailer = (TextView) findViewById(R.id.text_play_trailer);
        ratings = (TextView) findViewById(R.id.ratings);
        overview = (TextView) findViewById(R.id.overview);
        status = (TextView) findViewById(R.id.status);
        release_date = (TextView) findViewById(R.id.release_date);

        genre1 = (TextView) findViewById(R.id.genres);
        budget = (TextView) findViewById(R.id.budget);
        revenue = (TextView) findViewById(R.id.revenue);
        runtime = (TextView) findViewById(R.id.runtime);
        homepage = (TextView) findViewById(R.id.homepage);

        backdrop_image = (ImageView) findViewById(R.id.backdrop_image);


        recycler_view = (RecyclerView)findViewById(R.id.recycler_view);

        budget_title = (TextView) findViewById(R.id.budget_title);
        revenue_title = (TextView) findViewById(R.id.revenue_title);

        current_season = (TextView) findViewById(R.id.current_season);
        season_number = (TextView) findViewById(R.id.season_number);
        current_season_year = (TextView) findViewById(R.id.current_season_year);
        current_season_episodes = (TextView) findViewById(R.id.current_season_episodes);
        current_season_tagline = (TextView) findViewById(R.id.current_season_tagline);
        view_all_seasons = (TextView) findViewById(R.id.view_all_seasons);

        release_date_title = (TextView) findViewById(R.id.release_date_title);
        overview_layout = (LinearLayout) findViewById(R.id.overview_layout);

        overview_title = (TextView)findViewById(R.id.overview_title);

        facts_layout = (LinearLayout)findViewById(R.id.facts_layout);

        status_title = (TextView) findViewById(R.id.status_title);
        genres_title = (TextView) findViewById(R.id.genres_title);
        runtime_title = (TextView) findViewById(R.id.runtime_title);
        homepage_title= (TextView) findViewById(R.id.homepage_title);

        facts = (TextView)findViewById(R.id.facts);

    }

    private void prepareTvData(String url)
    {
        String tag_json_obj = "json_obj_req1";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                tvExampleModel = gson.fromJson(response,TvExampleModel.class);

                toolbar.setTitle(tvExampleModel.getName());
                overview.setText(tvExampleModel.getOverview());
                ratings.setText(String.valueOf(tvExampleModel.getVoteAverage()));
                status.setText(tvExampleModel.getStatus());
                release_date.setText(tvExampleModel.getFirstAirDate());

                //Season Layout

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                String date = dateFormat.format(calendar.getTime());
                String last_air_Date_string = tvExampleModel.getLastAirDate();
                String last_season_air_date_string;
                int size = tvExampleModel.getSeasons().size();
                last_season_air_date_string = tvExampleModel.getSeasons().get(size - 1).getAirDate();

                String last_season_year = last_season_air_date_string.substring(0,4);
                current_season_year.setText(last_season_year);

                season_number.setText(String.valueOf(tvExampleModel.getSeasons().get(size - 1).getSeasonNumber()));

                current_season_episodes.setText(String.valueOf(tvExampleModel.getSeasons().get(size - 1).getEpisodeCount()));

                try {
                    Date today_date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);
                    Date last_air_date = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH).parse(last_air_Date_string);
                    Date last_season_air_date = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH).parse(last_season_air_date_string);

                    if (last_air_date.compareTo(today_date) < 0)
                    {
                        current_season.setText("Last Season");
                    }
                    else if (last_air_date.compareTo(today_date) > 0)
                    {
                        if (last_season_air_date.compareTo(today_date) > 0)
                        {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Season ");
                            stringBuilder.append(String.valueOf(tvExampleModel.getSeasons().get(size - 1).getSeasonNumber()));
                            stringBuilder.append(" of ");
                            stringBuilder.append(tvExampleModel.getName());
                            stringBuilder.append(" will be premiered on ");
                            stringBuilder.append(last_season_air_date_string);
                            current_season_tagline.setText(stringBuilder);
                            current_season.setText("Upcoming Season");
                        }
                        else if (last_season_air_date.compareTo(today_date) < 0)
                        {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Season ");
                            stringBuilder.append(String.valueOf(tvExampleModel.getSeasons().get(size - 1).getSeasonNumber()));
                            stringBuilder.append(" of ");
                            stringBuilder.append(tvExampleModel.getName());
                            stringBuilder.append(" premiered on ");
                            stringBuilder.append(last_season_air_date_string);
                            current_season_tagline.setText(stringBuilder);
                            current_season.setText("Current Season");
                        }
                    }

                } catch (ParseException e) {
                    Toast.makeText(MovieView.this, "try catch error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }


                sb = new StringBuilder();
                String prefix = "";
                for (i = 0;i<tvExampleModel.getGenres().size();i++)
                {
                    sb.append(prefix);
                    prefix = ",";
                    sb.append(tvExampleModel.getGenres().get(i).getName());
                }
                genre1.setText(sb);



                runtime.setText(String.valueOf(tvExampleModel.getEpisodeRunTime()));
                homepage.setText(tvExampleModel.getHomepage());

               if (tvExampleModel.getBackdropPath()!= null)
               {
                   image_url = urlConstants.URL_Image + tvExampleModel.getBackdropPath();
               }


                                Target target = new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from)
                                {
                                    backdrop_image.setImageBitmap(bitmap);

                                    Palette.from(bitmap)
                                            .generate(new Palette.PaletteAsyncListener() {
                                                @Override
                                                public void onGenerated(Palette palette) {
                                                    Palette.Swatch textSwatch = palette.getDominantSwatch();
                                                    if (textSwatch == null ) {
                                                        Toast.makeText(MovieView.this, "Null swatch :(", Toast.LENGTH_SHORT).show();
                                                        overview_layout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                                        facts_layout.setBackgroundColor(Color.GRAY);
                                                        return;
                                                    }
                                                    overview_layout.setBackgroundColor(textSwatch.getRgb());
                                                    overview.setTextColor(textSwatch.getTitleTextColor());
                                                    overview_title.setTextColor(textSwatch.getTitleTextColor());
                                                    facts_layout.setBackgroundColor(textSwatch.getRgb());
                                                    facts.setTextColor(textSwatch.getTitleTextColor());
                                                    status_title.setTextColor(textSwatch.getTitleTextColor());
                                                    status.setTextColor(textSwatch.getTitleTextColor());
                                                    release_date_title.setTextColor(textSwatch.getTitleTextColor());
                                                    release_date.setTextColor(textSwatch.getTitleTextColor());
                                                    genres_title.setTextColor(textSwatch.getTitleTextColor());
                                                    runtime.setTextColor(textSwatch.getTitleTextColor());
                                                    runtime_title.setTextColor(textSwatch.getTitleTextColor());
                                                    homepage_title.setTextColor(textSwatch.getTitleTextColor());
                                                    homepage.setTextColor(textSwatch.getTitleTextColor());
                                                    genre1.setTextColor(textSwatch.getTitleTextColor());
                                                }
                                            });
                                }

                                @Override
                                public void onBitmapFailed(Drawable errorDrawable)
                                {
                                    overview_layout.setBackgroundColor(Color.GRAY);
                                    facts_layout.setBackgroundColor(Color.GRAY);
                                    backdrop_image.setImageDrawable(errorDrawable);

                                }

                                @Override
                                public void onPrepareLoad(Drawable placeHolderDrawable)
                                {


                                }
                            };

                    Picasso.with(MovieView.this)
                            .load(image_url)
                            .placeholder(R.drawable.not_available)
                            .into(target);

                    backdrop_image.setTag(target);


                    //Making Arraylist for RecyclerView
                    int i;
                    for (i=0;i<tvExampleModel.getCredits().getCast().size();i++)
                    {
                        Cast cast = new Cast();
                        cast.setName(tvExampleModel.getCredits().getCast().get(i).getName());
                        cast.setCharacter(tvExampleModel.getCredits().getCast().get(i).getCharacter());
                        cast.setId(tvExampleModel.getCredits().getCast().get(i).getId());
                        cast.setProfilePath(tvExampleModel.getCredits().getCast().get(i).getProfilePath());
                        castArrayList.add(i,cast);
                        castDetailAdapter.notifyDataSetChanged();
                    }

                    castDetailAdapter.notifyDataSetChanged();

                    subCastArrayList.clear();

                    if (castArrayList.size() > 8)
                    {
                        subCastArrayList.addAll(castArrayList.subList(0,8));
                        castDetailAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        subCastArrayList.addAll(castArrayList);
                        castDetailAdapter.notifyDataSetChanged();
                    }


                    pDialog.hide();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppSingleton.getInstance(this).addToRequestQueue(stringRequest, tag_json_obj);
    }



    private void prepareMovieData(String url)
    {

        String tag_json_obj = "json_obj_req1";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();


        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {

                //Setting text to the textviews
                movieDetailFull = gson.fromJson(response,MovieDetailFull.class);
                String hours = String.valueOf(movieDetailFull.getRuntime() / 60);
                String minutes = String.valueOf(movieDetailFull.getRuntime() % 60);
                toolbar.setTitle(movieDetailFull.getTitle());
                ratings.setText(String.valueOf(movieDetailFull.getVoteAverage()));
                overview.setText(movieDetailFull.getOverview());
                status.setText(movieDetailFull.getStatus());
                release_date.setText(movieDetailFull.getReleaseDate());
                budget.setText("$" + String.valueOf(movieDetailFull.getBudget()));
                revenue.setText("$" + String.valueOf(movieDetailFull.getRevenue()));
                runtime.setText(hours + "h " + minutes  +"m");
                homepage.setText(movieDetailFull.getHomepage());

                //Setting text to genres
                int value = movieDetailFull.getGenres().size();

                sb = new StringBuilder();
                String prefix = "";
                for (i = 0;i<value;i++)
                {
                    sb.append(prefix);
                    prefix = ",";
                    sb.append(movieDetailFull.getGenres().get(i).getName());
                }
                genre1.setText(sb);

                //Setting Image to BAckDrop Imageview
                if (movieDetailFull.getBelongsToCollection() == null)
                {
                    url2 = movieDetailFull.getBackdropPath();
                }
                else
                {
                    url2 = movieDetailFull.getBelongsToCollection().getBackdropPath();
                }

                String url3 = urlConstants.URL_Image + url2;



                Target target = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from)
                    {
                        backdrop_image.setImageBitmap(bitmap);

                        Palette.from(bitmap)
                                .generate(new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(Palette palette) {
                                        Palette.Swatch textSwatch = palette.getDominantSwatch();
                                        if (textSwatch == null ) {
                                            Toast.makeText(MovieView.this, "Null swatch :(", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        overview_layout.setBackgroundColor(textSwatch.getRgb());
                                        overview.setTextColor(textSwatch.getTitleTextColor());
                                        overview_title.setTextColor(textSwatch.getTitleTextColor());
                                        facts_layout.setBackgroundColor(textSwatch.getRgb());
                                        facts.setTextColor(textSwatch.getTitleTextColor());
                                        status_title.setTextColor(textSwatch.getTitleTextColor());
                                        status.setTextColor(textSwatch.getTitleTextColor());
                                        release_date_title.setTextColor(textSwatch.getTitleTextColor());
                                        release_date.setTextColor(textSwatch.getTitleTextColor());
                                        genres_title.setTextColor(textSwatch.getTitleTextColor());
                                        runtime.setTextColor(textSwatch.getTitleTextColor());
                                        runtime_title.setTextColor(textSwatch.getTitleTextColor());
                                        homepage_title.setTextColor(textSwatch.getTitleTextColor());
                                        homepage.setTextColor(textSwatch.getTitleTextColor());
                                        budget_title.setTextColor(textSwatch.getTitleTextColor());
                                        budget.setTextColor(textSwatch.getTitleTextColor());
                                        revenue.setTextColor(textSwatch.getTitleTextColor());
                                        revenue_title.setTextColor(textSwatch.getTitleTextColor());
                                        genre1.setTextColor(textSwatch.getTitleTextColor());
                                    }
                                });
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable)
                    {
                        overview_layout.setBackgroundColor(Color.GRAY);
                        facts_layout.setBackgroundColor(Color.GRAY);
                        backdrop_image.setImageDrawable(errorDrawable);

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable)
                    {


                    }
                };

                Picasso.with(MovieView.this)
                        .load(url3)
                        .placeholder(R.drawable.not_available)
                        .into(target);

                backdrop_image.setTag(target);



                //Making Arraylist for RecyclerView
                int i;
                for (i=0;i<movieDetailFull.getCredits().getCast().size();i++)
                {
                    Cast cast = new Cast();
                    cast.setName(movieDetailFull.getCredits().getCast().get(i).getName());
                    cast.setCharacter(movieDetailFull.getCredits().getCast().get(i).getCharacter());
                    cast.setId(movieDetailFull.getCredits().getCast().get(i).getId());
                    cast.setProfilePath(movieDetailFull.getCredits().getCast().get(i).getProfilePath());
                    castArrayList.add(i,cast);
                    castDetailAdapter.notifyDataSetChanged();
                }

                castDetailAdapter.notifyDataSetChanged();

                subCastArrayList.clear();

                if (castArrayList.size() > 8)
                {
                    subCastArrayList.addAll(castArrayList.subList(0,8));
                    castDetailAdapter.notifyDataSetChanged();
                }
                else
                {
                    subCastArrayList.addAll(castArrayList);
                    castDetailAdapter.notifyDataSetChanged();
                }





                pDialog.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                boolean b = Utility.isNetworkAvailable(MovieView.this);

                if (!b)
                {
                    Snackbar.make(coordinator_layout_movie_view,"No Internet Connection",Snackbar.LENGTH_LONG).show();
                }
                pDialog.hide();

            }
        });

        AppSingleton.getInstance(this).addToRequestQueue(stringRequest1, tag_json_obj);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}
