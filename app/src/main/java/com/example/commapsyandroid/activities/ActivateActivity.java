package com.example.commapsyandroid.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.commapsyandroid.R;
import com.example.commapsyandroid.entities.User;
import com.example.commapsyandroid.utils.Request;
import com.example.commapsyandroid.utils.Utils;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

import javax.json.JsonObject;

public class ActivateActivity extends AppCompatActivity {

    private String mail;
    private TextInputLayout key;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate);
        key = (TextInputLayout) findViewById(R.id.keyLayout);
        loading = (ProgressBar) findViewById(R.id.loading);
        mail = getIntent().getStringExtra("mail");
    }


    public void activate(View v)
    {
        AppCompatActivity AppCom = this;
        loading.setVisibility(View.VISIBLE);
        key.setError(null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,String> parameters = new HashMap<String,String>();
                parameters.put("Mail",mail);
                parameters.put("Key", Utils.hashString(key.getEditText().getText().toString()));
                try {

                    String result = Request.requestData(Request.URLConexion + "/User/validate", parameters);

                    System.out.println(result);

                    boolean correcto = Boolean.parseBoolean(result);

                    System.out.println(correcto);

                    if(correcto)
                    {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"Cuenta activada",Toast.LENGTH_LONG).show();
                                loading.setVisibility(View.INVISIBLE);
                                Utils.restartApp(AppCom);
                            }
                        });


                    }else
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                key.setError("Clave incorrecta");
                                loading.setVisibility(View.INVISIBLE);
                            }
                        });

                        Thread.sleep(3000);


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                key.setError(null);
                            }
                        });
                    }

                }catch (Exception ex)
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Error al realizar la operacion",Toast.LENGTH_LONG).show();
                            loading.setVisibility(View.INVISIBLE);
                        }
                    });
                    ex.printStackTrace();

                }


            }
        }).start();
    }
}