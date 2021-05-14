package com.example.commapsyandroid.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.commapsyandroid.R;
import com.example.commapsyandroid.entities.User;
import com.example.commapsyandroid.utils.Request;
import com.example.commapsyandroid.utils.Utils;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

import javax.json.JsonObject;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout mail;
    private TextInputLayout name;
    private TextInputLayout surname;
    private TextInputLayout pass;
    private TextInputLayout genderL;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        String[] gender = {"Male","Female","Other"};
        ((MaterialAutoCompleteTextView)findViewById(R.id.genderField)).setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,gender));

        mail = (TextInputLayout) findViewById(R.id.mailLayout);
        name = (TextInputLayout) findViewById(R.id.nameLayout);
        surname = (TextInputLayout) findViewById(R.id.surnameLayout);
        pass = (TextInputLayout) findViewById(R.id.passLayout);
        genderL = (TextInputLayout) findViewById(R.id.genderLayout);
        loading = (ProgressBar) findViewById(R.id.loading);

    }


    public void register(View v)
    {
        loading.setVisibility(View.VISIBLE);
        mail.setError(null);
        name.setError(null);
        surname.setError(null);
        pass.setError(null);
        genderL.setError(null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,String> parameters = new HashMap<String,String>();
                parameters.put("Mail",mail.getEditText().getText().toString());
                parameters.put("Name",name.getEditText().getText().toString());
                parameters.put("Surname",surname.getEditText().getText().toString());
                parameters.put("Password", Utils.hashString(pass.getEditText().getText().toString()));
                parameters.put("Profile_Photo","");
                parameters.put("Gender",genderL.getEditText().getText().toString());
                try {

                    String result = Request.requestData(Request.URLConexion + "/User/register", parameters);

                    System.out.println(result);

                    boolean correcto = Boolean.parseBoolean(result);

                    System.out.println(correcto);

                    if(correcto)
                    {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"Registro correcto",Toast.LENGTH_LONG).show();
                                loading.setVisibility(View.INVISIBLE);
                                Intent intent = new Intent(getApplicationContext(),ActivateActivity.class);
                                intent.putExtra("mail",parameters.get("Mail"));
                                startActivity(intent);
                            }
                        });
                    }else
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mail.setError("Error al registrarse");
                                name.setError("Error al registrarse");
                                surname.setError("Error al registrarse");
                                pass.setError("Error al registrarse");
                                genderL.setError("Error al registrarse");
                                loading.setVisibility(View.INVISIBLE);
                            }
                        });

                        Thread.sleep(3000);


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mail.setError(null);
                                name.setError(null);
                                surname.setError(null);
                                pass.setError(null);
                                genderL.setError(null);
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