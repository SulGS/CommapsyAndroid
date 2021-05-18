package com.example.commapsyandroid.activities.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.commapsyandroid.R;
import com.example.commapsyandroid.entities.Place;
import com.example.commapsyandroid.recyclerview.PlacesAdapter;
import com.example.commapsyandroid.utils.Utils;

import java.io.StringReader;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class ExplorerResultsFragment extends Fragment {

    private RecyclerView list;

    public ExplorerResultsFragment() {
        // Required empty public constructor
    }

    public static ExplorerResultsFragment newInstance(String param1, String param2) {
        ExplorerResultsFragment fragment = new ExplorerResultsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void updateList()
    {
        String placesList = getActivity().getSharedPreferences("localData", Context.MODE_PRIVATE).getString("stringJsonResponse","");


        JsonReader jsonReader = Json.createReader(new StringReader(placesList));
        JsonArray jsonArray = jsonReader.readArray();
        jsonReader.close();

        ArrayList<Place> places = new ArrayList<Place>();

        for (int i = 0;i<jsonArray.size();i++)
        {
            places.add(Place.jsonToPlace(jsonArray.getJsonObject(i)));
        }



        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        PlacesAdapter adaptador = new PlacesAdapter(getActivity(),places);

        list.setAdapter(adaptador);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explorer_results, container, false);

        list = ((RecyclerView)view.findViewById(R.id.placesList));
        updateList();


        return view;
    }
}