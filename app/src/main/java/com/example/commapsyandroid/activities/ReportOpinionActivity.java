package com.example.commapsyandroid.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.commapsyandroid.R;
import com.example.commapsyandroid.entities.Opinion;
import com.example.commapsyandroid.entities.User;
import com.example.commapsyandroid.utils.Request;
import com.example.commapsyandroid.utils.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class ReportOpinionActivity extends AppCompatDialogFragment {

    private TextInputLayout reason;
    private ProgressBar loading;
    private MaterialButton button;

    private Opinion opinion;

    public ReportOpinionActivity(Opinion op)
    {
        opinion = op;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_report_opinion,null);

        reason = (TextInputLayout) view.findViewById(R.id.reasonLayout);
        button = (MaterialButton) view.findViewById(R.id.send);
        loading = (ProgressBar) view.findViewById(R.id.loading);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send(v);
            }
        });

        builder.setView(view).setTitle("Report");



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
                parameters.put("Mail",(User.jsonToUser(Utils.stringToJson(PlatformActivity.getActiveUser()))).getMail());
                parameters.put("OpinionID",opinion.getID()+"");
                parameters.put("Comment",reason.getEditText().getText().toString().replaceAll("\n",".,."));

                try {
                    String response = Request.requestData(Request.URLConexion + "/Report/register", parameters);

                    if(Boolean.parseBoolean(response))
                    {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                button.setEnabled(true);
                                loading.setVisibility(View.INVISIBLE);
                                dismiss();
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
}