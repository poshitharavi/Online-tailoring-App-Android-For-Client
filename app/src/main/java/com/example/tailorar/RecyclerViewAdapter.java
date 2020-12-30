package com.example.tailorar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Style> mData;

    public RecyclerViewAdapter(Context mContext, List<Style> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        view = layoutInflater.inflate(R.layout.cardview_style, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.styleTitle.setText(mData.get(position).getStyleName());
        holder.styleImg.setImageResource(mData.get(position).getImageResource());

        //click event
        holder.styleCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext,StyleDetailActivity.class);

                //adddetails
                intent.putExtra("styleId", mData.get(position).getStyleId());
                intent.putExtra("styleName", mData.get(position).getStyleName());
                intent.putExtra("styleDescription",mData.get(position).getDescription());
                intent.putExtra("styleImg",mData.get(position).getImageResource());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        TextView styleTitle;
        ImageView styleImg;
        CardView styleCardView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            styleImg = itemView.findViewById(R.id.styleCardImg);
            styleTitle = itemView.findViewById(R.id.styleNameCardTxt);
            styleCardView = itemView.findViewById(R.id.styleCard);
        }
    }

}
