package com.example.commapsyandroid.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.commapsyandroid.R;
import com.example.commapsyandroid.entities.Place;
import com.example.commapsyandroid.entities.User;
import com.example.commapsyandroid.utils.Request;
import com.example.commapsyandroid.utils.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.osmdroid.views.MapView;

import java.util.HashMap;
import java.util.Map;

public class RatingActivity extends AppCompatDialogFragment {

    private Place place;

    private RatingBar rating;
    private TextInputLayout comment;
    private ProgressBar loading;
    private MaterialButton button;


    public RatingActivity (Place place)
    {
        this.place = place;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_rating,null);

        rating = (RatingBar) view.findViewById(R.id.ratingBar);
        comment = (TextInputLayout) view.findViewById(R.id.commentLayout);
        button = (MaterialButton) view.findViewById(R.id.send);
        loading = (ProgressBar) view.findViewById(R.id.loading);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send(v);
            }
        });

        builder.setView(view).setTitle("Rating");



        return builder.create();
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
                parameters.put("UserMail",(User.jsonToUser(Utils.stringToJson(PlatformActivity.getActiveUser()))).getMail());
                parameters.put("PlaceID",place.getID()+"");
                parameters.put("Rating",rating.getRating()+"");
                parameters.put("Comment",comment.getEditText().getText().toString());

                try {
                    String response = Request.requestData(Request.URLConexion + "/Opinion/register", parameters);

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
                                dismiss();
                            }
                        });
                    }else
                    {


                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loading.setVisibility(View.INVISIBLE);
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
                            loading.setVisibility(View.INVISIBLE);
                        }
                    });
                    ex.printStackTrace();

                }


            }
        }).start();
    }


}