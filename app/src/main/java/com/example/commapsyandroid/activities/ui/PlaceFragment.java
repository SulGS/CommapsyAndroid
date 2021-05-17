package com.example.commapsyandroid.activities.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.commapsyandroid.BuildConfig;
import com.example.commapsyandroid.R;
import com.example.commapsyandroid.entities.Place;
import com.example.commapsyandroid.utils.Utils;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;

public class PlaceFragment extends Fragment {


    private Place place;
    private MapView map;

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
        place = Place.jsonToPlace(Utils.stringToJson(getActivity().getSharedPreferences("localData", Context.MODE_PRIVATE).getString("place","")));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place, container, false);

        ((TextView)view.findViewById(R.id.placeName)).setText(place.getName());
        ((TextView)view.findViewById(R.id.placeDescription)).setText(place.getDescription());

        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap b = Utils.urlToBitmap(place.getPhoto());

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((ImageView)view.findViewById(R.id.placePhoto)).setImageBitmap(b);
                    }
                });
            }
        }).start();

        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

        map = ((MapView)view.findViewById(R.id.placeMap));

        Marker marker = new Marker(map);

        marker.setPosition(new GeoPoint(place.getLatitude(),place.getLongitude()));

        marker.setTextLabelBackgroundColor(
                Color.RED
        );

        marker.setTitle(place.getName());

        map.getOverlays().add(marker);

        IMapController mapController = map.getController();
        mapController.setZoom(17.5);
        GeoPoint startPoint = new GeoPoint(place.getLatitude(), place.getLongitude());
        mapController.setCenter(startPoint);


        return view;
    }
}