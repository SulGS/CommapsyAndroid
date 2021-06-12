package com.example.commapsyandroid.utils;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavDeepLinkBuilder;

import com.example.commapsyandroid.R;
import com.example.commapsyandroid.activities.LoginActivity;
import com.example.commapsyandroid.activities.PlatformActivity;
import com.example.commapsyandroid.entities.Place;
import com.example.commapsyandroid.entities.User;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class Utils {

    private static boolean SERVICE_STATUS = false;
    private static LocationManager locationManager;
    private static LocationListener ll;


    public String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }


    public static void pickImage(Activity aca,boolean isPlace)
    {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("outputX", 256);
        intent.putExtra("outputY", 256);
        if(isPlace)
        {
            intent.putExtra("aspectX", 16);
            intent.putExtra("aspectY", 9);
        }else
        {
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
        }
        intent.putExtra("return-data", true);
        aca.startActivityForResult(intent, 1);
    }

    public static void restartApp(Activity act)
    {
        Intent intent = new Intent(act, LoginActivity.class);
        intent.putExtra("ValorExtra", true);
        act.startActivity(intent);
        act.finish();
    }

    public static void createNotificationChannel(Activity activity,String name) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(name, name, importance);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = activity.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void configService(Activity aca)
    {
        locationManager = (LocationManager) aca.getSystemService(Context.LOCATION_SERVICE);
        ll = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                findShortestPlace(location,aca);
            }
        };
    }

    public static boolean getStatus()
    {
        return SERVICE_STATUS;
    }

    public static boolean changeStatus(Activity aca)
    {
        SERVICE_STATUS = !SERVICE_STATUS;

        if(SERVICE_STATUS)
        {
            if (ActivityCompat.checkSelfPermission(aca, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(aca, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestGPSPermissions(aca);
                if (ActivityCompat.checkSelfPermission(aca, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(aca, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    SERVICE_STATUS = !SERVICE_STATUS;
                    return SERVICE_STATUS;
                }
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 120000, 0, ll);
        }else
        {
            locationManager.removeUpdates(ll);
        }

        return SERVICE_STATUS;
    }

    private static void requestGPSPermissions(Activity aca)
    {
        ActivityCompat.requestPermissions(aca,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1);
    }

    public static void findShortestPlace(Location locationGPS,Activity aca) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,String> parameters = new HashMap<String,String>();
                parameters.put("Latitude",locationGPS.getLatitude()+"");
                parameters.put("Longitude",locationGPS.getLongitude()+"");
                try {
                    Log.d("Result","a");
                    String result = Request.requestData(Request.URLConexion + "/Place/returnShortestPlace", parameters);
                    Log.d("Result","b");
                    Place place = Place.jsonToPlace(Utils.stringToJson(result));

                    Log.d("Result",result);


                    if(place!=null)
                    {
                        aca.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.showNotification(result,aca);
                            }
                        });
                    }else
                    {
                        if(result.equals("403"))
                        {
                            Utils.restartApp(aca);
                        }else {
                            aca.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                }
                            });
                        }
                    }

                }catch (Exception ex)
                {
                    aca.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(aca.getApplicationContext(),"Error al realizar la operacion",Toast.LENGTH_LONG).show();
                        }
                    });
                    ex.printStackTrace();

                }


            }
        }).start();

    }

    public static void showNotification(String stringPlace,Activity activity)
    {
        String user = null;
        if(activity instanceof PlatformActivity)
        {
            user = ((PlatformActivity)activity).getActiveUser();
        }
        System.out.println(user);
        Place place = Place.jsonToPlace(Utils.stringToJson(stringPlace));
        SharedPreferences sp = activity.getSharedPreferences("localData", Context.MODE_PRIVATE);

        SharedPreferences.Editor edit = sp.edit();
        edit.putString("place",stringPlace);
        edit.putString("user",user);
        edit.commit();

        PendingIntent pi = new NavDeepLinkBuilder(activity.getApplication().getApplicationContext()).setComponentName(PlatformActivity.class).
        setGraph(R.navigation.mobile_navigation).setDestination(R.id.placeFragment).createPendingIntent();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity, "ShortestPlace")
                .setSmallIcon(R.drawable.commapsy)
                .setContentTitle(place.getName())
                .setContentText(place.getDescription())
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(place.getDescription()))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT).setContentIntent(pi).setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(activity);

        notificationManager.notify(new Random().nextInt(1000000),builder.build());
    }

    public static JsonObject stringToJson(String jsonToParse)
    {
        try {
            System.out.println(jsonToParse);
            JsonReader jReader = Json.createReader(new StringReader(jsonToParse));

            JsonObject jsonValues = jReader.readObject();

            jReader.close();

            return jsonValues;
        }catch(Exception x)
        {
            x.printStackTrace();
            return null;
        }
    }

    public static Bitmap urlToBitmap(String decode)
    {
        try {
            return BitmapFactory.decodeStream(new URL("http://commapsy.us.to:8082/" + decode).openStream());
        }catch(Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public static String hashString(String text)
    {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        byte[] encodedhash = digest.digest(
                text.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
        for (int i = 0; i < encodedhash.length; i++) {
            String hex = Integer.toHexString(0xff & encodedhash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
