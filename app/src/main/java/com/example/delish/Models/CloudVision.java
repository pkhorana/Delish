package com.example.delish.Models;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.*;
import java.io.*;
import java.util.Scanner;



public class CloudVision {

    private static final String TARGET_URL = "https://vision.googleapis.com/v1/images:annotate?";
    private static final String API_KEY = "key=AIzaSyDvAJuEtHZG3ZOwV1PzbrLyx3Hzt-OG1xU";


    public static String webDetectionResponse(String imageString) throws Exception {
        URL serverUrl = new URL(TARGET_URL + API_KEY );

        URLConnection urlConnection = serverUrl.openConnection();

        HttpURLConnection httpConnection = (HttpURLConnection)urlConnection;
        httpConnection.setRequestMethod("POST");
        httpConnection.setRequestProperty("Content-Type", "application/json");
        //httpConnection.setRequestProperty("Authorization", "Bearer " + token);
        httpConnection.setDoOutput(true);

        BufferedWriter httpRequestBodyWriter = new BufferedWriter(new
                OutputStreamWriter(httpConnection.getOutputStream()));
        String str = "{\"requests\":  [{ \"features\":  [ {\"type\": \"WEB_DETECTION\""
                +"}], \"image\": { \"content\": "
                + "\"" + imageString + "\"" + "}}]}";
        httpRequestBodyWriter.write
                (str);
        httpRequestBodyWriter.close();


        // httpRequestBodyWriter.write
        //     ("{\"requests\":  [{ \"features\":  [ {\"type\": \"WEB_DETECTION\""
        //     +"}], \"image\": {\"source\": { \"gcsImageUri\":"
        //     +" \"gs://testbucket67/potato.jpeg\"}}}]}");
        // httpRequestBodyWriter.close();

        String response = httpConnection.getResponseMessage();
        if (httpConnection.getInputStream() == null) {
            System.out.println("No stream");
            return null;
        }

        Scanner httpResponseScanner = new Scanner (httpConnection.getInputStream());
        String resp = "";
        while (httpResponseScanner.hasNext()) {
            String line = httpResponseScanner.nextLine();
            resp += line;
            System.out.println(line);  //  alternatively, print the line of response
        }
        httpResponseScanner.close();

        JSONObject json = new JSONObject(resp);
        JSONArray arr = json.getJSONArray("responses");
        JSONObject obj = arr.getJSONObject(0);
        JSONObject obj2 = obj.getJSONObject("webDetection");
        JSONArray arr2 = obj2.getJSONArray("webEntities");

        JSONObject first = ((JSONArray) arr2).getJSONObject(0);
        String tag = first.getString("description").toLowerCase();
        RecipeAlgo.setItem(tag);
        return tag;
    }

}
