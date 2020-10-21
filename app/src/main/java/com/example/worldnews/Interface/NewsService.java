package com.example.worldnews.Interface;

import com.example.worldnews.Common.Common;
import com.example.worldnews.Model.News;
import com.example.worldnews.Model.WebSite;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface NewsService {
    @GET("v2/sources?language=en&apiKey="+ Common.API_KEY)
    Call<WebSite> getSources();

    @GET
    Call<News> getNewsArticles(@Url String url);
}
