package com.example.commapsyandroid.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.commapsyandroid.BuildConfig;
import com.example.commapsyandroid.R;
import com.example.commapsyandroid.entities.User;
import com.example.commapsyandroid.utils.Request;
import com.example.commapsyandroid.utils.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;

import java.util.HashMap;
import java.util.Map;

import javax.json.JsonObject;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout mail;
    private TextInputLayout pass;
    private MaterialButton button;
    private String stringUser;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        setContentView(R.layout.activity_login);
        mail = (TextInputLayout) findViewById(R.id.mailLayout);
        pass = (TextInputLayout) findViewById(R.id.passLayout);
        loading = (ProgressBar) findViewById(R.id.loading);
        button = (MaterialButton) findViewById(R.id.login);
        SharedPreferences sp = getSharedPreferences("localData",MODE_PRIVATE);

        if(sp.contains("user"))
        {
            User user = User.jsonToUser(Utils.stringToJson(sp.getString("user","")));
            mail.getEditText().setText(user.getMail());
            pass.getEditText().setText(sp.getString("password",""));
            login((findViewById(R.id.login)));
        }


    }

    public void link(View v)
    {
        Intent intent = null;
        switch (v.getId())
        {
            case R.id.registerLink:
                intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.login:
                intent = new Intent(getApplicationContext(),PlatformActivity.class);
                intent.putExtra("user",stringUser);
                startActivity(intent);
                break;
            case R.id.passLink:
                intent = new Intent(getApplicationContext(),ResetPasswordActivity.class);
                startActivity(intent);
                break;
        }
    }



    public void login(View v)
    {
        loading.setVisibility(View.VISIBLE);
        mail.setError(null);
        pass.setError(null);
        button.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,String> parameters = new HashMap<String,String>();
                parameters.put("Mail",mail.getEditText().getText().toString());
                parameters.put("Password", Utils.hashString(pass.getEditText().getText().toString()));
                try {
                    stringUser = Request.requestData(Request.URLConexion + "/User/login", parameters);
                    System.out.println(stringUser);
                    JsonObject json = Utils.stringToJson(stringUser);
                    System.out.println(json);
                    User user = User.jsonToUser(json);
                    System.out.println(user);

                    if(user!=null)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loading.setVisibility(View.INVISIBLE);
                                button.setEnabled(true);
                            }
                        });
                        if(user.isIs_Enable())
                        {
                            if(!user.getMail().equals("0"))
                            {
                                Request.Token = user.get_Key();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"Login correcto",Toast.LENGTH_LONG).show();
                                        SharedPreferences.Editor sp = getSharedPreferences("localData",MODE_PRIVATE).edit();
                                        sp.putString("user",stringUser);
                                        sp.putString("password",pass.getEditText().getText().toString());
                                        sp.commit();
                                        button.setEnabled(true);
                                        link(v);
                                    }
                                });
                            }else
                            {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"Usuario baneado permanentemente",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }else
                        {
                            button.setEnabled(true);
                            Intent intent = new Intent(getApplicationContext(),ActivateActivity.class);
                            intent.putExtra("mail",parameters.get("Mail"));
                            startActivity(intent);
                        }
                    }else
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mail.setError("Creedenciales incorrectas");
                                pass.setError("Creedenciales incorrectas");
                                loading.setVisibility(View.INVISIBLE);
                                button.setEnabled(true);
                            }
                        });

                        Thread.sleep(3000);


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mail.setError(null);
                                pass.setError(null);
                                button.setEnabled(true);
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
                            button.setEnabled(true);
                        }
                    });
                    ex.printStackTrace();

                }


            }
        }).start();
    }
}