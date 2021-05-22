package com.example.commapsyandroid.recyclerview;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commapsyandroid.R;
import com.example.commapsyandroid.activities.PlatformActivity;
import com.example.commapsyandroid.entities.Opinion;
import com.example.commapsyandroid.entities.Place;
import com.example.commapsyandroid.utils.Utils;

import java.util.ArrayList;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolderComments>{

    ArrayList<Opinion> comments;
    Activity activity;

    public CommentsAdapter(Activity act, ArrayList<Opinion> p)
    {
        activity = act;
        comments = p;
    }


    @NonNull
    @Override
    public ViewHolderComments onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_adapter,parent,false);

        return new ViewHolderComments(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderComments holder, int position) {

        holder.comment.setText(comments.get(position).getComment());

        float rate = (float)comments.get(position).getRating();

        System.out.println(rate);

        holder.rating.setRating(rate/2);


        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolderComments extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView comment;
        RatingBar rating;
        int position;

        public ViewHolderComments(@NonNull View itemView) {
            super(itemView);
            rating = ((RatingBar)itemView.findViewById(R.id.ratingBar));
            comment = ((TextView)itemView.findViewById(R.id.comment));
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {

            return false;
        }
    }
}
