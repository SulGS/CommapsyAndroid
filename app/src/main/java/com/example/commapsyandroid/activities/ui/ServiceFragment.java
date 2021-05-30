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
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_service, container, false);

        serviceSwitch = (Switch) view.findViewById(R.id.switchService);

        if(Utils.getStatus())
        {
            serviceSwitch.setText("Activado");
            serviceSwitch.setChecked(true);
        }else
        {
            serviceSwitch.setText("Desactivado");
            serviceSwitch.setChecked(false);
        }



        Utils.createNotificationChannel(getActivity(),"ShortestPlace");

        Utils.configService(getActivity());

        serviceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(Utils.changeStatus(getActivity()))
                {
                    serviceSwitch.setText("Activado");
                    serviceSwitch.setChecked(true);
                }else
                {
                    serviceSwitch.setText("Desactivado");
                    serviceSwitch.setChecked(false);
                }



            }
        });

        return view;
    }






}