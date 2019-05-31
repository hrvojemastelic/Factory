package com.kepler22b.articles.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kepler22b.articles.MainActivity;
import com.kepler22b.articles.R;
import com.kepler22b.articles.model.Article;


import java.util.List;

import io.realm.Realm;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.FaceViewHolder> implements View.OnClickListener{

    Realm realm;
    private LayoutInflater mInflater;
    private List<Article> mMovieInfoList;
    private Context mContext;
    private MainActivity.recyclerViewOnClickListener mListener;

    public RecyclerViewAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    //gets onclick listener from mainactivity
    public void setListener(MainActivity.recyclerViewOnClickListener listener) {
        mListener = listener;
    }

    //gets list from request
    public void setMovieInfoList(List<Article> movieInfoList) {
        mMovieInfoList = movieInfoList;

    }

    @Override
    public FaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FaceViewHolder(mInflater.inflate(R.layout.item_face, parent, false));
    }

    @Override
    public void onBindViewHolder(FaceViewHolder holder, int position) {

        holder.itemView.setTag(position);
        holder.title.setText(mMovieInfoList.get(position).title);
     //   holder.description.setText(mMovieInfoList.get(position).description);
        holder.itemView.setOnClickListener(this);
        Glide.with(mContext)
                .load(mMovieInfoList.get(position).urlToImage)
                .into(holder.mImageView);

    }

    @Override
    public int getItemCount() {

        return mMovieInfoList == null ? 0 : mMovieInfoList.size();
    }

    @Override
    public void onClick(View v) {
        if (mListener != null)
            mListener.onClick((Integer) v.getTag());
    }



    public class FaceViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;
        TextView title,description;

        public FaceViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.iv_face);
            title=(TextView)itemView.findViewById(R.id.recyclertitle);
            //description=(TextView)itemView.findViewById(R.id.recyclerdescription);
        }
    }
}
