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

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout mail;
    private TextInputLayout pass;
    private String stringUser;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mail = (TextInputLayout) findViewById(R.id.mailLayout);
        pass = (TextInputLayout) findViewById(R.id.passLayout);
        loading = (ProgressBar) findViewById(R.id.loading);
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
                            }
                        });
                        if(user.isIs_Enable())
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),"Login correcto",Toast.LENGTH_LONG).show();
                                    link(v);
                                }
                            });
                        }else
                        {
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
                            }
                        });

                        Thread.sleep(3000);


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mail.setError(null);
                                pass.setError(null);
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