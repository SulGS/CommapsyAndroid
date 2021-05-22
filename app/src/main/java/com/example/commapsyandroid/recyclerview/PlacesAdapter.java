package com.example.commapsyandroid.recyclerview;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commapsyandroid.R;
import com.example.commapsyandroid.activities.PlatformActivity;
import com.example.commapsyandroid.entities.Place;
import com.example.commapsyandroid.utils.Utils;

import java.util.ArrayList;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolderPlaces>{

    ArrayList<Place> places;
    Activity activity;

    public PlacesAdapter(Activity act, ArrayList<Place> p)
    {
        activity = act;
        places = p;
    }

    
    @NonNull
    @Override
    public ViewHolderPlaces onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_adapter,parent,false);

        return new ViewHolderPlaces(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPlaces holder, int position) {

        holder.placeName.setText(places.get(position).getName());
        holder.placeCategory.setText(places.get(position).getCategory());

        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = Utils.urlToBitmap(places.get(position).getPhoto());

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        holder.placeImage.setImageBitmap(bitmap);
                    }
                });
            }
        }).start();


        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public class ViewHolderPlaces extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        ImageView placeImage;
        TextView placeName, placeCategory;
        int position;

        public ViewHolderPlaces(@NonNull View itemView) {
            super(itemView);
            placeImage = ((ImageView)itemView.findViewById(R.id.placeImage));
            placeName = ((TextView)itemView.findViewById(R.id.placeName));
            placeCategory = ((TextView)itemView.findViewById(R.id.placeCategory));
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            SharedPreferences.Editor sp = activity.getSharedPreferences("localData", Context.MODE_PRIVATE).edit();
            sp.putString("place",places.get(position).toJsonString());
            sp.commit();
            PlatformActivity.getNavigationController().popBackStack();
            PlatformActivity.getNavigationController().navigate(R.id.placeFragment);
        }

        @Override
        public boolean onLongClick(View v) {
            SharedPreferences.Editor sp = activity.getSharedPreferences("localData", Context.MODE_PRIVATE).edit();
            sp.putString("place",places.get(position).toJsonString());
            sp.commit();
            PlatformActivity.getNavigationController().popBackStack();
            PlatformActivity.getNavigationController().navigate(R.id.placeRequestFragment);
            return false;
        }
    }
}
