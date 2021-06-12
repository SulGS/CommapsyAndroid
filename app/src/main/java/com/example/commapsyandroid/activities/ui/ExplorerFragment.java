package com.example.commapsyandroid.activities.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.commapsyandroid.R;
import com.example.commapsyandroid.activities.ActivateActivity;
import com.example.commapsyandroid.activities.PlatformActivity;
import com.example.commapsyandroid.entities.User;
import com.example.commapsyandroid.utils.Request;
import com.example.commapsyandroid.utils.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

import javax.json.JsonObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExplorerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExplorerFragment extends Fragment {

    private TextInputLayout filter;
    private MaterialButton button;
    private ProgressBar loading;

    public ExplorerFragment() {

    }

    public static ExplorerFragment newInstance() {
        ExplorerFragment fragment = new ExplorerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void search(View v)
    {
        loading.setVisibility(View.VISIBLE);
        filter.setError(null);
        button.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,String> parameters = new HashMap<String,String>();
                parameters.put("Name",filter.getEditText().getText().toString());
                parameters.put("Page",0+"");
                try {
                    String response = Request.requestData(Request.URLConexion + "/Place/returnPlacesByName", parameters);

                    if(!response.equals(""))
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

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SharedPreferences.Editor sp = getActivity().getSharedPreferences("localData", Context.MODE_PRIVATE).edit();
                                    sp.putString("stringJsonResponse", response);
                                    sp.putString("nameParameter", parameters.get("Name"));
                                    sp.commit();
                                    button.setEnabled(true);
                                    PlatformActivity.getNavigationController().navigate(R.id.explorerResultsFragment);
                                }
                            });
                        }
                    }else
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                filter.setError("Introduzca al menos 3 letras");
                                loading.setVisibility(View.INVISIBLE);
                                button.setEnabled(true);
                            }
                        });

                        Thread.sleep(3000);


                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                filter.setError(null);
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
                            loading.setVisibility(View.INVISIBLE);
                            button.setEnabled(true);
                        }
                    });
                    ex.printStackTrace();

                }


            }
        }).start();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explorer, container, false);

        filter = ((TextInputLayout)view.findViewById(R.id.searchLayout));
        button = ((MaterialButton)view.findViewById(R.id.searchButton));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(v);
            }
        });
        loading = ((ProgressBar)view.findViewById(R.id.loading));

        return view;
    }
}