package com.kepler22b.articles;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.kepler22b.articles.Adapter.RecyclerViewAdapter;

import com.kepler22b.articles.Interface.ApiInterface;
import com.kepler22b.articles.model.Article;
import com.kepler22b.articles.model.ResponseModel;
import com.kepler22b.articles.rests.ApiClient;


import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mFaceAdapter;

    ProgressBar progressBar;
    Realm realm;
    ArrayList<Article> mArticleList;
    private static final String API_KEY = "6946d0c07a1c4555a4186bfcade76398";
    Button retry;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


      /*  ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                 if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {*/



            retry=(Button)findViewById(R.id.retry);
            retry.setVisibility(View.GONE);


            progressBar = (ProgressBar) findViewById(R.id.progressbar);
            progressBar.setVisibility(View.VISIBLE);
            mArticleList = new ArrayList<Article>();

            mRecyclerView = (RecyclerView) findViewById(R.id.rv_gallery);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mFaceAdapter = new RecyclerViewAdapter(this);
            mRecyclerView.setAdapter(mFaceAdapter);

            realm = Realm.getDefaultInstance();


            //On click adapter position
            mFaceAdapter.setListener(new recyclerViewOnClickListener() {
                @Override
                public void onClick(int position) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("list", mArticleList);
                    bundle.putInt("position", position);

                    //launch the fragment
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    ViewPagerFragment viewPagerFragment = ViewPagerFragment.newInstance();
                    viewPagerFragment.setArguments(bundle);
                    viewPagerFragment.show(fragmentTransaction, "SLIDE_SHOW");

                }

                @Override
                public void onLongClick(int position) {

                }
            });


            List<Article> articleitems = realm.copyFromRealm(realm.where(Article.class).findAll());

            if (articleitems.size() <= 0) {

                fetchMovieInfo();
            } else {
                mArticleList.addAll(articleitems);
                mFaceAdapter.setMovieInfoList(mArticleList);
                mFaceAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                //Compare time from first item with current time
                //if time from article is greeter then current time - 5 minutes

                Article publishedAt=realm.copyFromRealm(realm.where(Article.class).equalTo("publishedAt",articleitems.get(0).publishedAt).findFirst());
                String time=publishedAt.publishedAt;
                DateTime dateTime;
                dateTime= DateTime.parse(time);
                DateTime currenttime=new DateTime();
                currenttime=currenttime.minusMinutes(5);


        if (dateTime.isBefore(currenttime)){

             fetchMovieInfo();

            mFaceAdapter.notifyDataSetChanged();


                }


                System.out.println("vrimee"+ dateTime + currenttime);

            }


        //}





        //DO IF THERE IS NO INTERNET CONNECTION
        //THIS PART CAN ALSO BE MANAGED IN onFailure

     /*   else if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {

            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {

                 retry=(Button)findViewById(R.id.retry);
                retry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //restart activity
                        finish();
                        startActivity(getIntent());
                    }
                });

                progressBar = (ProgressBar) findViewById(R.id.progressbar);
                progressBar.setVisibility(View.GONE);

                //set background
                RelativeLayout relativeLayout;
                relativeLayout=(RelativeLayout)findViewById(R.id.activity_main);
                relativeLayout.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.network) );
            } else {

                 retry=(Button)findViewById(R.id.retry);
                retry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //restart activity

                        finish();
                        startActivity(getIntent());
                    }
                });

                progressBar = (ProgressBar) findViewById(R.id.progressbar);
                progressBar.setVisibility(View.GONE);

                //set background
                RelativeLayout relativeLayout;
                relativeLayout=(RelativeLayout)findViewById(R.id.activity_main);
                relativeLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.network));
            }


        }*/


    }

    public void fetchMovieInfo() {

        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseModel> call = apiService.getLatestNews("bbc-news",API_KEY);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.body().getStatus().equals("ok")) {
                     List<Article> articleList = response.body().getArticles();


                     mArticleList.clear();
                     mArticleList.addAll(articleList);

                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(mArticleList);
                    realm.commitTransaction();

                  
                    mFaceAdapter.setMovieInfoList(mArticleList);
                    mFaceAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("GREŠKA")
                        .setMessage("Ups,došlo je do pogreške"+"\n"+ "Trenutni prikaz van mreže ")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue
                            }
                        }).show();



            }
        });

    }

    public interface recyclerViewOnClickListener {

        void onClick(int position);

        void onLongClick(int position);

    }
        }

