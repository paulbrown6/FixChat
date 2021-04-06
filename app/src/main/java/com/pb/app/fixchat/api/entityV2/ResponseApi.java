package com.pb.app.fixchat.api.entityV2;
import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public class ResponseApi {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private Object message;

    public Map<String, String> getMessage() {
        return message!=null?parsMessage():null;
    }

    public void setMessage(JSONArray message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String ok) {
        this.status = ok;
    }

    @NotNull
    private Map<String, String> parsMessage(){
        Map<String, String> map = new ArrayMap<>();
        String[] parts = message.toString().substring(1, message.toString().length() - 1).split(", ");
        for(int i = 0; i < parts.length; i++){
            String[] maps = parts[i].split("=");
            if (maps.length > 1) {
                map.put(maps[0], maps[1]);
            } else {
                map.put("message", maps[0]);
            }
        }
        return map;
    }

    @NonNull
    public String toString(){
        return "Response: status = "+ status + " / message = " + getMessage();
    }
}