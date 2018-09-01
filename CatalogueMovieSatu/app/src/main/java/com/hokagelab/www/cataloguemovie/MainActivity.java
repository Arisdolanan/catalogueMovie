package com.hokagelab.www.cataloguemovie;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hokagelab.www.cataloguemovie.adapter.MovieAdapter;
import com.hokagelab.www.cataloguemovie.api.ApiClient;
import com.hokagelab.www.cataloguemovie.api.MovieClient;
import com.hokagelab.www.cataloguemovie.model.MovieResponse;
import com.hokagelab.www.cataloguemovie.model.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//By Aris Kurniawan

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "MainActivity";
    private final static String API_KEY = "007c868395e80dc2e4a833416b24efa5";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

//    private ImageButton imageButton;
    private Button btnHome, btnCari;
    private EditText editText;
    static final String EXTRAS_MOVIE = "EXTRAS_MOVIE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        editText = (EditText) findViewById(R.id.edt_search);

        btnCari = (Button) findViewById(R.id.btn_cari);
        btnCari.setOnClickListener(this);
//        imageButton = (ImageButton) findViewById(R.id.img_button);
//        imageButton.setOnClickListener(this);

        btnHome = (Button) findViewById(R.id.btn_home);
        btnHome.setOnClickListener(this);

//        String movie = editText.getText().toString();
//        Bundle bundle = new Bundle();
//        bundle.putString(EXTRAS_MOVIE,movie);

        getAllMovie();

    }

    private void getAllMovie(){
        MovieClient apiService = ApiClient.getRetrofit().create(MovieClient.class);
        Call<MovieResponse> call = apiService.reposMovieList(API_KEY);

        // panggil progressdialog biar enak kayak nunggu jodoh
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMax(50);
        progressDialog.setMessage("Harap Bersabar XD");
        progressDialog.setTitle("Catalogue Movie");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();


        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                progressDialog.dismiss();

                List<Result> MovieList = response.body().getResults();
                Log.d(TAG, "Jumlah data Movie: " + String.valueOf(MovieList.size()));
                //lempar data ke adapter
                mAdapter = new MovieAdapter(MovieList);
                mRecyclerView.setAdapter(mAdapter);

//                String data;
//                Log.d(TAG,"Datanya ada gak yaa = "+response);
//                MovieResponse movies = response.body();
//                List<Result> list = movies.getResults();
//
//                Result firstMovie = list.get(12);
//                TextView textView = findViewById(R.id.tv_helloworld);
//                textView.setText(firstMovie.getTitle());
//
//                Toast.makeText(MainActivity.this, "Data Movies = "+ list.size(), Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "Data Movies = " + firstMovie);

            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, t.toString());
                Toast.makeText(MainActivity.this, "koneksi error :(", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void getSearchMovie(String EXTRAS_MOVIES){
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMax(50);
        progressDialog.setMessage("Harap Bersabar XD");
        progressDialog.setTitle("Catalogue Movie");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        MovieClient apiService = ApiClient.getRetrofit().create(MovieClient.class);
        Call<MovieResponse> call = apiService.reposSearch(API_KEY, EXTRAS_MOVIES);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                progressDialog.dismiss();
                List<Result> MovieList = response.body().getResults();
                Log.d(TAG, "Jumlah data Movie: " + String.valueOf(MovieList.size()));
                //lempar data ke adapter
                mAdapter = new MovieAdapter(MovieList);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, t.toString());
                Toast.makeText(MainActivity.this, "koneksi error :(", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(MainActivity.this, "On Klik", Toast.LENGTH_SHORT).show();
        if (v.getId() == R.id.btn_cari){
            String EXTRAS_MOVIES = editText.getText().toString();
            if (EXTRAS_MOVIES.isEmpty()){
                EXTRAS_MOVIES = "tidak ada";
                Log.e(TAG,"Data = "+EXTRAS_MOVIES);
                Toast.makeText(MainActivity.this, "Ada filmnya gak ? "+EXTRAS_MOVIES, Toast.LENGTH_SHORT).show();
                getSearchMovie(EXTRAS_MOVIES);

            }else {
                Log.e(TAG,"Data = "+EXTRAS_MOVIES);
                Toast.makeText(MainActivity.this, "Ada filmnya gak ? ada ini, semua film "+EXTRAS_MOVIES, Toast.LENGTH_SHORT).show();
                getSearchMovie(EXTRAS_MOVIES);
            }

        }

        if (v.getId() == R.id.btn_home){
            getAllMovie();
        }

    }
}
