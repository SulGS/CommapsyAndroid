package com.example.commapsyandroid.activities.ui;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiceFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Switch serviceSwitch;

    public ServiceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ServiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ServiceFragment newInstance(String param1, String param2) {
        ServiceFragment fragment = new ServiceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_service, container, false);

        serviceSwitch = (Switch) view.findViewById(R.id.switchService);

        createNotificationChannel();

        serviceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                findShortestPlace();
                serviceSwitch.setChecked(false);
            }
        });

        return view;
    }

    public void requestGPSPermissions()
    {
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1);
    }

    public void findShortestPlace() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestGPSPermissions();
            return;
        }
        Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,String> parameters = new HashMap<String,String>();
                parameters.put("Latitude",locationGPS.getLatitude()+"");
                parameters.put("Longitude",locationGPS.getLongitude()+"");
                try {
                    Place place = Place.jsonToPlace(Utils.stringToJson(Request.requestData(Request.URLConexion + "/Place/returnShortestPlace", parameters)));

                    if(place!=null)
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), "ShortestPlace")
                                        .setSmallIcon(R.drawable.commapsy)
                                        .setContentTitle(place.getName())
                                        .setContentText(place.getDescription())
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText(place.getDescription()))
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());

                                notificationManager.notify(new Random().nextInt(1000000),builder.build());
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

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ShortestPlace";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("ShortestPlace", name, importance);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}