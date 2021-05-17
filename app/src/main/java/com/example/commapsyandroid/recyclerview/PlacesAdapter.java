package com.example.commapsyandroid.recyclerview;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commapsyandroid.entities.Place;

import java.util.ArrayList;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolderPlaces>{

    ArrayList<Place> places;



    @NonNull
    @Override
    public ViewHolderPlaces onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPlaces holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolderPlaces extends RecyclerView.ViewHolder {

        public ViewHolderPlaces(@NonNull View itemView) {
            super(itemView);



        }
    }
}
