package com.example.worldnews.ui;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.worldnews.Adapter.ListSourceAdapter;
import com.example.worldnews.Common.Common;
import com.example.worldnews.Interface.NewsService;
import com.example.worldnews.Model.WebSite;
import com.example.worldnews.R;
import com.example.worldnews.ui.SignIn.Login;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {
    RecyclerView RV;
    NewsService mService ;
    ListSourceAdapter adapter;
    AlertDialog dialog;
    Button logoutBtn;
    SwipeRefreshLayout swipeRefreshLayout;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    FrameLayout frameLayout;
    public static final int PERMISSION_REQUEST_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //init cache
        Paper.init(this);
        //init Service
        mService= Common.getNewsService();


        swipeRefreshLayout =findViewById(R.id.swipe);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadWebsiteSource(true);
            }
        });
        //init View
        RV =findViewById(R.id.list_source);
        RV.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        RV.setLayoutManager(lm);
//
//        dialog= new SpotsDialog(this);
//        dialog.show();

        loadWebsiteSource(false);

        logoutBtn =findViewById(R.id.Logout_MainBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
//                Intent intent3 = new Intent(MainActivity.this, Login.class);
//                startActivity(intent3);
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor=pref.edit();
                editor.putInt("reg",0).commit();
                if(pref.getInt("reg",1)==0){
                    Intent intent =new Intent(MainActivity.this,Login.class);
                    startActivity(intent);
                }

            }
        });
    }

    private void loadWebsiteSource(boolean isRefreshed) {
        if (!isRefreshed){
            String cache =Paper.book().read("cache");
            if (cache!=null && !cache.isEmpty()&& !cache.equals("null")){
                WebSite webSite=new Gson().fromJson(cache,WebSite.class);
                adapter=new ListSourceAdapter(getBaseContext(),webSite);
                adapter.notifyDataSetChanged();
                RV.setAdapter(adapter);

            }
            else {
                mService.getSources().enqueue(new Callback<WebSite>() {
                    @Override
                    public void onResponse(Call<WebSite> call, Response<WebSite> response) {
                        adapter=new ListSourceAdapter(getBaseContext(),response.body());
                        adapter.notifyDataSetChanged();
                        RV.setAdapter(adapter);

                        Paper.book().write("cache",new Gson().toJson(response.body()));

                    }

                    @Override
                    public void onFailure(Call<WebSite> call, Throwable t) {

                    }
                });
            }
        }
        else{
            swipeRefreshLayout.setRefreshing(true);
            mService.getSources().enqueue(new Callback<WebSite>() {
                @Override
                public void onResponse(Call<WebSite> call, Response<WebSite> response) {
                    adapter=new ListSourceAdapter(getBaseContext(),response.body());
                    adapter.notifyDataSetChanged();
                    RV.setAdapter(adapter);

                    Paper.book().write("cache",new Gson().toJson(response.body()));

                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<WebSite> call, Throwable t) {

                }
            });

        }
    }
}