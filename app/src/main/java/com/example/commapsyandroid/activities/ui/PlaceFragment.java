package com.example.commapsyandroid.activities.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.commapsyandroid.R;
import com.example.commapsyandroid.entities.Place;
import com.example.commapsyandroid.utils.Utils;

public class PlaceFragment extends Fragment {


    private Place place;

    public PlaceFragment() {
        // Required empty public constructor
    }


    public static PlaceFragment newInstance(String place) {
        PlaceFragment fragment = new PlaceFragment();
        Bundle args = new Bundle();
        args.putString("place", place);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        place = Place.jsonToPlace(Utils.stringToJson(getArguments().getString("place")));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place, container, false);

        ((TextView)view.findViewById(R.id.placeName)).setText(place.getName());
        ((ImageView)view.findViewById(R.id.placePhoto)).setImageBitmap(Utils.urlToBitmap(place.getBytesPhoto()));
        ((TextView)view.findViewById(R.id.placeDescription)).setText(place.getDescription());

        return view;
    }
}