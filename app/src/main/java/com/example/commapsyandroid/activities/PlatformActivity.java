package com.example.commapsyandroid.activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.commapsyandroid.R;
import com.example.commapsyandroid.activities.ui.ExplorerFragment;
import com.example.commapsyandroid.activities.ui.ServiceFragment;
import com.example.commapsyandroid.entities.User;
import com.example.commapsyandroid.utils.Request;
import com.example.commapsyandroid.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.json.JsonObject;

public class PlatformActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private User user;
    private static String userJson;
    private static NavController nc;

    private static String name;


    public static String getActiveUser()
    {
        return userJson;
    }


    public static void setSendName(String n)
    {
        name = n;
    }

    public static NavController getNavigationController()
    {
        return nc;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(getIntent().hasExtra("user"))
        {
            userJson = getIntent().getExtras().getString("user");
        }else
        {
            userJson = getSharedPreferences("localData",MODE_PRIVATE).getString("user","");
        }


        user = User.jsonToUser(Utils.stringToJson(userJson));
        System.out.println(userJson);
        setContentView(R.layout.activity_platform);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactFormActivity cfa = new ContactFormActivity();
                cfa.show(getSupportFragmentManager(),"Contact Form");
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ((TextView)navigationView.getHeaderView(0).findViewById(R.id.email)).setText(user.getMail());
        ((TextView)navigationView.getHeaderView(0).findViewById(R.id.nameSurname)).setText(user.getName() + " " + user.getSurname());
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap b = Utils.urlToBitmap(user.getProfile_Photo());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((ImageView)findViewById(R.id.userLogo)).setImageBitmap(b);
                    }
                });
            }
        }).start();


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.servicio, R.id.explorador, R.id.miperfil,R.id.cerrarsesion)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        nc = navController;

        AppCompatActivity aca = this;

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.servicio:
                        navController.navigate(R.id.serviceFragment);
                        drawer.closeDrawers();
                        break;
                    case R.id.explorador:
                        navController.navigate(R.id.explorerFragment);
                        drawer.closeDrawers();
                        break;
                    case R.id.cerrarsesion:

                        SharedPreferences.Editor sp = getSharedPreferences("localData",MODE_PRIVATE).edit();

                        sp.remove("user");
                        sp.remove("password");
                        sp.commit();


                        Utils.restartApp(aca);
                        break;
                }


                return true;
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.platform, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}