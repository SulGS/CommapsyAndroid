package com.example.commapsyandroid.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.commapsyandroid.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout mail;
    private TextInputLayout pass;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mail = (TextInputLayout) findViewById(R.id.mailLayout);
        pass = (TextInputLayout) findViewById(R.id.passLayout);
        loading = (ProgressBar) findViewById(R.id.loading);
    }



    public void login(View v)
    {
        loading.setVisibility(View.VISIBLE);
        mail.setError(null);
        pass.setError(null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,String> parameters = new HashMap<String,String>();
                parameters.put("Mail",mail.getEditText().getText().toString());
                parameters.put("Password",Utils.hashString(pass.getEditText().getText().toString()));
                try {
                    boolean estadoOperacion = Boolean.parseBoolean(Request.requestData(Request.URLConexion + "/User/login", parameters));

                    if(estadoOperacion)
                    {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"Login correcto",Toast.LENGTH_LONG).show();
                            }
                        });
                    }else
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mail.setError("Creedenciales incorrectas");
                                pass.setError("Creedenciales incorrectas");
                            }
                        });
                    }

                }catch (Exception ex)
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Error al realizar la operacion",Toast.LENGTH_LONG).show();
                        }
                    });

                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }).start();
    }
}