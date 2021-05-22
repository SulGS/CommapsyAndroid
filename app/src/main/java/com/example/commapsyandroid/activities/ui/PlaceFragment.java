package com.example.commapsyandroid.activities.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.commapsyandroid.BuildConfig;
import com.example.commapsyandroid.R;
import com.example.commapsyandroid.activities.PlatformActivity;
import com.example.commapsyandroid.activities.RatingActivity;
import com.example.commapsyandroid.entities.Place;
import com.example.commapsyandroid.entities.User;
import com.example.commapsyandroid.utils.Request;
import com.example.commapsyandroid.utils.Utils;
import com.google.android.material.button.MaterialButton;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;

import java.util.HashMap;
import java.util.Map;

public class PlaceFragment extends Fragment {


    private Place place;
    private MapView map;
    private MaterialButton button;

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

        button = ((MaterialButton)view.findViewById(R.id.comments));

        ((MaterialButton)view.findViewById(R.id.rate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RatingActivity rating = new RatingActivity(place);
                rating.show(getActivity().getSupportFragmentManager(),"Rating");
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(v);
            }
        });



        map = ((MapView)view.findViewById(R.id.placeMap));

        Marker marker = new Marker(map);

        marker.setPosition(new GeoPoint(place.getLatitude(),place.getLongitude()));

        marker.setTextLabelBackgroundColor(
                Color.RED
        );

        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,String> parameters = new HashMap<String,String>();
                parameters.put("Mail",(User.jsonToUser(Utils.stringToJson(PlatformActivity.getActiveUser()))).getMail());
                parameters.put("PlaceID",place.getID()+"");

                String response = Request.requestData(Request.URLConexion + "/Opinion/exists", parameters);

                if(Boolean.parseBoolean(response))
                {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((MaterialButton)view.findViewById(R.id.rate)).setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        }).start();

        marker.setTitle(place.getName());

        map.getOverlays().add(marker);

        IMapController mapController = map.getController();
        mapController.setZoom(17.5);
        GeoPoint startPoint = new GeoPoint(place.getLatitude(), place.getLongitude());
        mapController.setCenter(startPoint);


        return view;
    }

    public void search(View v)
    {
        button.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,String> parameters = new HashMap<String,String>();
                parameters.put("PlaceID",place.getID()+"");
                parameters.put("Page",0+"");
                try {
                    String response = Request.requestData(Request.URLConexion + "/Opinion/returnOpinions", parameters);

                    if(!response.equals(""))
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                button.setEnabled(true);
                            }
                        });

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SharedPreferences.Editor sp = getActivity().getSharedPreferences("localData", Context.MODE_PRIVATE).edit();
                                sp.putString("stringJsonResponse",response);
                                sp.putString("placeParameter",parameters.get("PlaceID"));
                                sp.commit();
                                button.setEnabled(true);
                                System.out.println("fdsfdasdas");
                                PlatformActivity.getNavigationController().navigate(R.id.placeCommentsFragment);
                            }
                        });
                    }else
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                button.setEnabled(true);
                            }
                        });

                        Thread.sleep(3000);


                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                button.setEnabled(true);
                            }
                        });
                    }

                }catch (Exception ex)
                {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"Error al realizar la operacion",Toast.LENGTH_LONG).show();
                            button.setEnabled(true);
                        }
                    });
                    ex.printStackTrace();

                }


            }
        }).start();
    }
}