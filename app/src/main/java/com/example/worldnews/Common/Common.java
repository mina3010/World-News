package com.example.worldnews.Common;

import com.example.worldnews.Interface.NewsService;
//import com.example.worldnews.Remote.IconBetterIdeaClient;
import com.example.worldnews.RetrofitClient.RetrofitClient;

public class Common {
    private static final String BASE_URL="https://newsapi.org/";
    public static final String API_KEY="544104da0f8642b299b373baa0e2687a";

    public static NewsService getNewsService(){
        return RetrofitClient.getClient(BASE_URL).create(NewsService.class);
    }

    public static String getAPIUrl(String source, String apiKEY){
        StringBuilder apiUrl =new StringBuilder("https://newsapi.org/v2/top-headlines?sources=");
        return apiUrl.append(source).append("&apiKey=").append(apiKEY).toString();
    }
}
