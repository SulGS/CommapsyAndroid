package com.example.commapsyandroid.activities.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.commapsyandroid.R;
import com.example.commapsyandroid.activities.PlatformActivity;
import com.example.commapsyandroid.entities.Opinion;
import com.example.commapsyandroid.recyclerview.CommentsAdapter;
import com.example.commapsyandroid.utils.Request;
import com.google.android.material.button.MaterialButton;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;

public class PlaceCommentsFragment extends Fragment {

    private RecyclerView list;
    private ArrayList<Opinion> comments;
    private int page;
    private MaterialButton button;
    private String placeParameter;

    public PlaceCommentsFragment() {
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


        for (int i = 0;i<jsonArray.size();i++)
        {
            comments.add(Opinion.jsonToOpinion(jsonArray.getJsonObject(i)));
        }



        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        CommentsAdapter adaptador = new CommentsAdapter(getActivity(),comments);

        list.setAdapter(adaptador);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        page = 1;
        comments = new ArrayList<Opinion>();
        placeParameter = getActivity().getSharedPreferences("localData", Context.MODE_PRIVATE).getString("placeParameter","");
        View view = inflater.inflate(R.layout.fragment_explorer_results, container, false);

        button = ((MaterialButton)view.findViewById(R.id.more));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestMore(v);
            }
        });

        list = ((RecyclerView)view.findViewById(R.id.placesList));
        updateList();


        return view;
    }


    public void requestMore(View view)
    {
        button.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,String> parameters = new HashMap<String,String>();
                parameters.put("PlaceID", placeParameter);
                parameters.put("Page",page+"");
                page++;
                try {
                    String response = Request.requestData(Request.URLConexion + "/Opinion/returnOpinions", parameters);

                    if(!response.equals(""))
                    {


                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SharedPreferences.Editor sp = getActivity().getSharedPreferences("localData", Context.MODE_PRIVATE).edit();
                                sp.putString("stringJsonResponse",response);
                                sp.commit();
                                button.setEnabled(true);
                                updateList();
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