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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commapsyandroid.R;
import com.example.commapsyandroid.activities.PlatformActivity;
import com.example.commapsyandroid.activities.ReportOpinionActivity;
import com.example.commapsyandroid.entities.Opinion;
import com.example.commapsyandroid.entities.Place;
import com.example.commapsyandroid.entities.User;
import com.example.commapsyandroid.utils.Request;
import com.example.commapsyandroid.utils.Utils;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolderComments>{

    ArrayList<Opinion> comments;
    Activity activity;
    FragmentManager fragmentManager;

    public CommentsAdapter(Activity act, ArrayList<Opinion> p,FragmentManager fM)
    {
        activity = act;
        comments = p;
        fragmentManager = fM;
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

        MaterialButton button;
        TextView comment, name;
        RatingBar rating;
        int position;

        public ViewHolderComments(@NonNull View itemView) {
            super(itemView);
            rating = ((RatingBar)itemView.findViewById(R.id.ratingBar));
            comment = ((TextView)itemView.findViewById(R.id.comment));
            name = ((TextView)itemView.findViewById(R.id.name));
            button = ((MaterialButton)itemView.findViewById(R.id.send));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ReportOpinionActivity roa = new ReportOpinionActivity(comments.get(position));
                    roa.show(fragmentManager,"Comments");
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    User user = User.jsonToUser(Utils.stringToJson(PlatformActivity.getActiveUser()));
                    Map<String,String> parameters = new HashMap<String,String>();
                    parameters.put("Mail",comments.get(position).getUser_Mail());

                    try {
                        String response = Request.requestData(Request.URLConexion + "/User/getUser", parameters);

                        User u = User.jsonToUser(Utils.stringToJson(response));

                        if(u!=null)
                        {

                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    name.setText(u.getName());
                                }
                            });
                        }else
                        {
                            if(response.equals("403"))
                            {
                                Utils.restartApp(activity);
                            }
                        }

                    }catch (Exception ex)
                    {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity,"Error al realizar la operacion",Toast.LENGTH_LONG).show();
                            }
                        });
                        ex.printStackTrace();

                    }


                }
            }).start();
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
