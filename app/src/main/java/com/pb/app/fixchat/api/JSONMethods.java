package com.pb.app.fixchat.api;

import android.util.ArrayMap;
import android.util.Base64;
import android.util.Log;

import com.pb.app.fixchat.api.entityV2.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;

public class JSONMethods {

    private static JSONMethods instance;

    public static JSONMethods getInstance(){
        return instance == null? instance = new JSONMethods(): instance;
    }

    public static JSONObject createRequest(String body){
        String[] parts = body.split("\\|");
        JSONObject jsonObject = new JSONObject();
        try {
            for (int i = 0; i < parts.length; i++){
                String[] object = parts[i].split("\\.");
                jsonObject.put(object[0], object[1]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public static JSONObject createRequestArray(String body, String nameArray, ArrayList<String> array){
        String[] parts = body.split("\\|");
        JSONObject jsonObject = new JSONObject();
        try {
            for (int i = 0; i < parts.length; i++){
                String[] object = parts[i].split("\\.");
                jsonObject.put(object[0], object[1]);
            }
            jsonObject.put(nameArray, array);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public static User parsTokenToUser(String token){
        User user = new User();
        String[] parts = token.split("[.]");
        Map<String, String> map = new ArrayMap<>();
        try {
            String f = getJson(parts[1]);
            Log.d("TOKEN_DECODED", "Body: " + f);
            String s = f.split("\\{")[2].split("\\}")[0];
            String[] p = s.split(",");
            for(int i = 0; i < p.length; i++){
                String[] maps = p[i].split(":");
                map.put(maps[0].substring(1, maps[0].length()-1), maps[1].length() > 2 ?
                        maps[1].substring(1, maps[1].length()-1): maps[1]);
            }
            user.setId(map.get("id"));
            user.setName(map.get("name"));
            user.setEmail(map.get("email"));
            user.setCompany(map.get("company"));
            user.setRole(Integer.parseInt(map.get("role")));
        } catch (Exception e) {
            Log.e("TOKEN_DECODED", e.toString());
        }
        return user;
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException {
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }
}

