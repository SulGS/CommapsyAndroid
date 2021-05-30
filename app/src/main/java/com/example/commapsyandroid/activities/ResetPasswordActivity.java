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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

import javax.json.JsonObject;

public class ResetPasswordActivity extends AppCompatActivity {


    private TextInputLayout mail;
    private TextInputLayout key;
    private TextInputLayout pass;
    private ProgressBar loading;
    private MaterialButton buttonKey;
    private MaterialButton buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        mail = (TextInputLayout) findViewById(R.id.mailLayout);
        pass = (TextInputLayout) findViewById(R.id.passLayout);
        key = (TextInputLayout) findViewById(R.id.keyLayout);
        loading = (ProgressBar) findViewById(R.id.loading);
        buttonKey = (MaterialButton) findViewById(R.id.requestKey);
        buttonSend = (MaterialButton) findViewById(R.id.changePass);
    }


    public void requestKey(View v)
    {
        loading.setVisibility(View.VISIBLE);
        mail.setError(null);
        pass.setError(null);
        key.setError(null);
        buttonKey.setEnabled(false);
        buttonSend.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,String> parameters = new HashMap<String,String>();
                parameters.put("Mail",mail.getEditText().getText().toString());
                try {


                    if(Boolean.parseBoolean(Request.requestData(Request.URLConexion + "/User/requestPasswordChange", parameters)))
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                buttonKey.setEnabled(true);
                                buttonSend.setEnabled(true);
                                loading.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(),"Codigo enviado al correo",Toast.LENGTH_LONG).show();
                            }
                        });
                    }else
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                buttonKey.setEnabled(true);
                                buttonSend.setEnabled(true);
                                mail.setError("No existe el correo");
                                loading.setVisibility(View.INVISIBLE);
                            }
                        });

                        Thread.sleep(3000);


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                buttonKey.setEnabled(true);
                                buttonSend.setEnabled(true);
                                mail.setError(null);
                            }
                        });
                    }

                }catch (Exception ex)
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            buttonKey.setEnabled(true);
                            buttonSend.setEnabled(true);
                            Toast.makeText(getApplicationContext(),"Error al realizar la operacion",Toast.LENGTH_LONG).show();
                            loading.setVisibility(View.INVISIBLE);
                        }
                    });
                    ex.printStackTrace();

                }


            }
        }).start();
    }

    public void changePassword(View v)
    {
        AppCompatActivity AppCom = this;
        loading.setVisibility(View.VISIBLE);
        mail.setError(null);
        pass.setError(null);
        key.setError(null);
        buttonKey.setEnabled(false);
        buttonSend.setEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,String> parameters = new HashMap<String,String>();
                parameters.put("Mail",mail.getEditText().getText().toString());
                parameters.put("Key",Utils.hashString(key.getEditText().getText().toString()));
                parameters.put("Password",Utils.hashString(pass.getEditText().getText().toString()));
                try {

                    String result = Request.requestData(Request.URLConexion + "/User/changePassword", parameters);

                    System.out.println(result);

                    if(Boolean.parseBoolean(result))
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                buttonKey.setEnabled(true);
                                buttonSend.setEnabled(true);
                                loading.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(),"Contraseña cambiada",Toast.LENGTH_LONG).show();
                                Utils.restartApp(AppCom);
                            }
                        });
                    }else
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                buttonKey.setEnabled(true);
                                buttonSend.setEnabled(true);
                                key.setError("Clave incorrecta");
                                loading.setVisibility(View.INVISIBLE);
                            }
                        });

                        Thread.sleep(3000);


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                buttonKey.setEnabled(true);
                                buttonSend.setEnabled(true);
                                key.setError(null);
                            }
                        });
                    }

                }catch (Exception ex)
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            buttonKey.setEnabled(true);
                            buttonSend.setEnabled(true);
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