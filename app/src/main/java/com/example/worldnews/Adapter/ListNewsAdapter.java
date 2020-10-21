package com.example.worldnews.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.worldnews.ui.DetailsArticles;
import com.example.worldnews.Model.Article;
import com.example.worldnews.R;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ListNewsAdapter extends RecyclerView.Adapter<ListNewsAdapter.ViewHolder> {

    private List<Article>articleList=new ArrayList<>();
    private Context context;

    public ListNewsAdapter(List<Article> articleList, Context context) {
        this.articleList = articleList;
        this.context = context;
    }

    @Override
    public ListNewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_layout, parent, false);

        return new ListNewsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListNewsAdapter.ViewHolder holder, int position) {


        if(articleList.get(position).getUrlToImage()!=null) {
            if(!articleList.get(position).getUrlToImage().isEmpty()) {
                Picasso.get().load(articleList.get(position).getUrlToImage()).into(holder.article_image);
            }else {
            }
        }else {
        }

        if (articleList.get(position).getTitle().length()>65){
            holder.article_title.setText(articleList.get(position).getTitle().substring(0,65)+"...");
        }else {
            holder.article_title.setText(articleList.get(position).getTitle());
        }
//        Date date =null;
//        try {
//            if(!articleList.get(position).getPublishedAt().isEmpty()){
//                date= ISO8601Parse.parse(articleList.get(position).getPublishedAt());
//            }else {
//                Toast.makeText(context, "error in web site", Toast.LENGTH_SHORT).show();
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        holder.article_time.setReferenceTime(date.getTime());
        holder.article_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, DetailsArticles.class);
                intent.putExtra("webURL",articleList.get(position).getUrl());
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView article_title;
        RelativeTimeTextView article_time;
        CircleImageView article_image;
        CardView article_card;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            article_title = itemView.findViewById(R.id.article_title);
            article_image = (CircleImageView) itemView.findViewById(R.id.article_image);
            article_time = (RelativeTimeTextView) itemView.findViewById(R.id.article_time);
            article_card = (CardView) itemView.findViewById(R.id.article_card);
        }
    }
}
