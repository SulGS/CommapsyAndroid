package com.example.commapsyandroid.utils;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavDeepLinkBuilder;

import com.example.commapsyandroid.R;
import com.example.commapsyandroid.activities.LoginActivity;
import com.example.commapsyandroid.activities.PlatformActivity;
import com.example.commapsyandroid.entities.Place;
import com.example.commapsyandroid.entities.User;

import java.io.StringReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class Utils {

    public static void restartApp(AppCompatActivity act)
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
            return BitmapFactory.decodeStream(new URL("http://192.168.1.192/" + decode).openStream());
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
