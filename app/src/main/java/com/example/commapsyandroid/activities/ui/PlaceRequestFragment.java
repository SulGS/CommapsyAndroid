package com.example.commapsyandroid.activities.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.example.commapsyandroid.R;
import com.example.commapsyandroid.activities.PlatformActivity;
import com.example.commapsyandroid.entities.Place;
import com.example.commapsyandroid.entities.User;
import com.example.commapsyandroid.utils.Request;
import com.example.commapsyandroid.utils.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.osmdroid.api.IMapController;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import java.util.HashMap;
import java.util.Map;


public class PlaceRequestFragment extends Fragment {


    private Place place;
    private GeoPoint g;

    private TextInputLayout name;
    private TextInputLayout desc;
    private TextInputLayout category;
    private ProgressBar loading;
    private MapView map;
    private MaterialButton button;

    public PlaceRequestFragment() {
        // Required empty public constructor
    }


    public static PlaceRequestFragment newInstance() {
        PlaceRequestFragment fragment = new PlaceRequestFragment();
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
        View view = inflater.inflate(R.layout.fragment_place_request, container, false);



        name = (TextInputLayout) view.findViewById(R.id.nameLayout);
        desc = (TextInputLayout) view.findViewById(R.id.descLayout);
        category = (TextInputLayout) view.findViewById(R.id.categoryLayout);
        map = (MapView) view.findViewById(R.id.placeMap);
        button = (MaterialButton) view.findViewById(R.id.send);

        loading = (ProgressBar) view.findViewById(R.id.loading);

        name.getEditText().setText(place.getName());
        desc.getEditText().setText(place.getDescription());
        category.getEditText().setText(place.getCategory());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send(v);
            }
        });

        setMarker(new GeoPoint(place.getLatitude(),place.getLongitude()));



        return view;
    }

    public void send(View v)
    {
        loading.setVisibility(View.VISIBLE);
        button.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = User.jsonToUser(Utils.stringToJson(PlatformActivity.getActiveUser()));
                Map<String,String> parameters = new HashMap<String,String>();
                parameters.put("UserMail",user.getMail());
                parameters.put("PlaceID",place.getID()+"");
                parameters.put("Latitude",g.getLatitude()+"");
                parameters.put("Longitude",g.getLongitude()+"");
                parameters.put("Name",name.getEditText().getText().toString());
                parameters.put("Description",desc.getEditText().getText().toString());
                parameters.put("Category",category.getEditText().getText().toString());
                try {
                    String response = Request.requestData(Request.URLConexion + "/PlaceRequest/register", parameters);

                    if(Boolean.parseBoolean(response))
                    {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SharedPreferences.Editor sp = getActivity().getSharedPreferences("localData", Context.MODE_PRIVATE).edit();
                                sp.putString("stringJsonResponse",response);
                                sp.putString("nameParameter",parameters.get("Name"));
                                sp.commit();
                                button.setEnabled(true);
                                loading.setVisibility(View.INVISIBLE);
                                PlatformActivity.getNavigationController().navigate(R.id.serviceFragment);
                            }
                        });
                    }else
                    {
                        if(response.equals("403"))
                        {
                            Utils.restartApp(getActivity());
                        }else {

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loading.setVisibility(View.INVISIBLE);
                                    button.setEnabled(true);
                                }
                            });
                        }
                    }

                }catch (Exception ex)
                {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"Error al realizar la operacion",Toast.LENGTH_LONG).show();
                            button.setEnabled(true);
                            loading.setVisibility(View.INVISIBLE);
                        }
                    });
                    ex.printStackTrace();

                }


            }
        }).start();
    }

    public void setMarker(GeoPoint p)
    {
        Marker marker = new Marker(map);

        g = p;

        marker.setPosition(p);

        marker.setTextLabelBackgroundColor(
                Color.RED
        );

        marker.setTitle(place.getName());

        map.getOverlays().clear();

        map.getOverlays().add(marker);

        IMapController mapController = map.getController();
        mapController.setZoom(17.5);
        mapController.setCenter(p);

        MapEventsReceiver mReceive = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                setMarker(new GeoPoint(p.getLatitude(),p.getLongitude()));
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };


        MapEventsOverlay OverlayEvents = new MapEventsOverlay(getActivity().getBaseContext(), mReceive);
        map.getOverlays().add(OverlayEvents);
    }


}