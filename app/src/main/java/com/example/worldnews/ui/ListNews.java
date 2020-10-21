package com.example.worldnews.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.worldnews.Adapter.ListNewsAdapter;
import com.example.worldnews.Common.Common;
import com.example.worldnews.Interface.NewsService;
import com.example.worldnews.Model.Article;
import com.example.worldnews.Model.News;
import com.example.worldnews.R;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.florent37.diagonallayout.DiagonalLayout;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ListNews extends AppCompatActivity {

    KenBurnsView kbv;
    DiagonalLayout diagonalLayout;
    AlertDialog dialog;
    NewsService mService;
    TextView top_author, top_title;
    SwipeRefreshLayout swipeRefreshLayout;
    ListNewsAdapter adapter;
    RecyclerView RV_listNews;

    String source="",sortBy="",webHotUrl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_news);
        mService= Common.getNewsService();
//        dialog=new SpotsDialog(this,"...",3,false);
        swipeRefreshLayout =findViewById(R.id.swipe_listNews);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNews(source,true);
            }
        });
        diagonalLayout=findViewById(R.id.diagonalLayout);
        diagonalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getBaseContext(),DetailsArticles.class);
                intent.putExtra("webURL",webHotUrl);
                startActivity(intent);

            }
        });
        kbv=findViewById(R.id.top_image);
        top_author=findViewById(R.id.top_author);
        top_title=findViewById(R.id.top_title);

        RV_listNews= findViewById(R.id.RV_listNews);
        RV_listNews.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        RV_listNews.setLayoutManager(lm);

        if (getIntent()!=null){
            source=getIntent().getStringExtra("source");
            if (!source.isEmpty()){
                loadNews(source,false);
            }
        }
    }

    private void loadNews(String source, boolean isRefreshed) {
        if (!isRefreshed) {
            mService.getNewsArticles(Common.getAPIUrl(source, Common.API_KEY)).enqueue(new Callback<News>() {
                @Override
                public void onResponse(Call<News> call, Response<News> response) {

                    if(response.body().getArticles().get(0).getUrlToImage()==null)
                    {
                        Picasso.get().load(response.body().getArticles().get(0).getUrlToImage()).into(kbv);
                    }
                    else { }

                    top_title.setText(response.body().getArticles().get(0).getTitle());
                    top_author.setText(response.body().getArticles().get(0).getAuthor());
                    webHotUrl=response.body().getArticles().get(0).getUrl();

                    List<Article> removeFirstItem = response.body().getArticles();
                    removeFirstItem.remove(0);
                    adapter=new ListNewsAdapter(removeFirstItem,getBaseContext());
                    adapter.notifyDataSetChanged();
                    RV_listNews.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<News> call, Throwable t) {

                }
            });
        }else{
            mService.getNewsArticles(Common.getAPIUrl(source, Common.API_KEY)).enqueue(new Callback<News>() {
                @Override
                public void onResponse(Call<News> call, Response<News> response) {

                    if(response.body().getArticles().get(0).getUrlToImage()!=null) {
                        if(!response.body().getArticles().get(0).getUrlToImage().isEmpty()) {
                            Picasso.get().load(response.body().getArticles().get(0).getUrlToImage()).into(kbv);
                        }
                        else { }
                    }
                    else { }

                    top_title.setText(response.body().getArticles().get(0).getTitle());
                    top_author.setText(response.body().getArticles().get(0).getAuthor());
                    webHotUrl=response.body().getArticles().get(0).getUrl();

                    List<Article> removeFirstItem = response.body().getArticles();
                    removeFirstItem.remove(0);
                    adapter=new ListNewsAdapter(removeFirstItem,getBaseContext());
                    adapter.notifyDataSetChanged();
                    RV_listNews.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<News> call, Throwable t) {

                }
            });
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}