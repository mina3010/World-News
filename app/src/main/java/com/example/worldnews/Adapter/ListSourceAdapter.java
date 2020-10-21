package com.example.worldnews.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.worldnews.ui.ListNews;
import com.example.worldnews.Model.WebSite;
import com.example.worldnews.R;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ListSourceAdapter extends RecyclerView.Adapter<ListSourceAdapter.ViewHolder>  {
    private Context context;
    private WebSite webSite;

    public ListSourceAdapter(Context context, WebSite webSite) {
        this.context = context;
        this.webSite = webSite;
    }

    @Override
    public ListSourceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.source_layout, parent, false);

        return new ListSourceAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListSourceAdapter.ViewHolder holder, int position) {

        holder.sourceName.setText(webSite.getSources().get(position).getName());
        holder.sourceCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ListNews.class);
                intent.putExtra("source",webSite.getSources().get(position).getId());
                v.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return webSite.getSources().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sourceName;
        CircleImageView sourceImage;
        CardView sourceCard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sourceName = itemView.findViewById(R.id.source_name);
            sourceImage = (CircleImageView) itemView.findViewById(R.id.source_image);
            sourceCard = (CardView) itemView.findViewById(R.id.source_card);

        }
    }
}
