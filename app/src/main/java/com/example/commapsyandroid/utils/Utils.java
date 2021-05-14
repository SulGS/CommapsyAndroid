package com.example.commapsyandroid.utils;

import android.app.Activity;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.commapsyandroid.activities.LoginActivity;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
