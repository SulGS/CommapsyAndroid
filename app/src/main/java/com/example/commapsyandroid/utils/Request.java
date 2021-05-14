package com.example.commapsyandroid.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import javax.json.JsonObject;

public class Request {

    public final static String URLConexion = "http://192.168.1.192:8080";

    public static String requestData(String urlString, Map<String,String> parameters)
    {
        BufferedReader reader;
        String line;
        StringBuffer responseContent = new StringBuffer();


        try {
            URL url = new URL(urlString);

            StringBuilder postData = new StringBuilder();
            postData.append("{");
            int i = 0;
            for (Map.Entry<String,String> param : parameters.entrySet()) {
                postData.append("\"" + param.getKey() + "\"");
                postData.append(':');
                postData.append("\"" + String.valueOf(param.getValue()) + "\"");
                i++;
                if(parameters.size()!=i)
                {
                    postData.append(",");
                }
            }
            postData.append("}");
            System.out.println(postData.toString());
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            connection.setDoOutput(true);
            connection.getOutputStream().write(postDataBytes);

            int status = connection.getResponseCode();

            System.out.println(status);

            InputStreamReader in = new InputStreamReader(connection.getInputStream(), "UTF-8");


            String result = new String();
            for (int c; (c = in.read()) >= 0;)
                result += (char)c;


            in.close();
            return result;



        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
