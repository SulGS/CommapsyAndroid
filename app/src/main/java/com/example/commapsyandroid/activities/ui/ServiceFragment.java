package com.example.commapsyandroid.activities.ui;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.commapsyandroid.R;
import com.example.commapsyandroid.activities.ActivateActivity;
import com.example.commapsyandroid.entities.Place;
import com.example.commapsyandroid.entities.User;
import com.example.commapsyandroid.utils.Request;
import com.example.commapsyandroid.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.json.JsonObject;

public class ServiceFragment extends Fragment {


    private Switch serviceSwitch;
    private LocationManager locationManager;
    private LocationListener ll;

    public ServiceFragment() {
        // Required empty public constructor
    }

    public static ServiceFragment newInstance(String param1, String param2) {
        ServiceFragment fragment = new ServiceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        ll = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                findShortestPlace(location);
            }
        };

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_service, container, false);

        serviceSwitch = (Switch) view.findViewById(R.id.switchService);

        Utils.createNotificationChannel(getActivity(),"ShortestPlace");

        serviceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        requestGPSPermissions();
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            serviceSwitch.setChecked(false);
                            return;
                        }
                    }
                    serviceSwitch.setText("Activado");
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 120000, 0, ll);
                }else
                {
                    serviceSwitch.setText("Desactivado");
                    locationManager.removeUpdates(ll);
                }



            }
        });

        return view;
    }

    public void requestGPSPermissions()
    {
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1);
    }

    public void findShortestPlace(Location locationGPS) {



        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,String> parameters = new HashMap<String,String>();
                parameters.put("Latitude",locationGPS.getLatitude()+"");
                parameters.put("Longitude",locationGPS.getLongitude()+"");
                try {
                    Log.d("Result","a");
                    String result = Request.requestData(Request.URLConexion + "/Place/returnShortestPlace", parameters);
                    Log.d("Result","b");
                    Place place = Place.jsonToPlace(Utils.stringToJson(result));

                    Log.d("Result",result);


                    if(place!=null)
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.showNotification(result,getActivity());
                            }
                        });
                    }else
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    }

                }catch (Exception ex)
                {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(),"Error al realizar la operacion",Toast.LENGTH_LONG).show();
                        }
                    });
                    ex.printStackTrace();

                }


            }
        }).start();

    }


}