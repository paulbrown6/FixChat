package com.pb.app.fixchat.api.entityV2;
import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pb.app.fixchat.api.CallV2;

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

    private Map<String, String> sMessage;

    private Map<String, String> messageIntance(){
        if(sMessage == null) sMessage = parsMessage();
        return sMessage;
    }

    public Map<String, String> getMessage() {
        return messageIntance();
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
        System.out.println(message.toString());
        String[] isArray = message.toString().split("\\[");
        String[] parts = new String[1];
        if (isArray.length > 1) {
            parts[0] = message.toString().substring(1, message.toString().length() - 1).
                    replace("=[", "![");
        }
        else {
            parts = message.toString().substring(1, message.toString().length() - 1).split(", ");
            if (parts.length <= 1){
                parts = message.toString().split(", ");
            }
        }
        for(int i = 0; i < parts.length; i++){
            String[] maps = new String[1];
            if (isArray.length > 1) {
                maps = parts[i].split("!");
            }
            else {
                maps = parts[i].split("=");
            }
            if (maps.length > 1) {
                map.put(maps[0], maps[1]);
            }
            else {
                map.put(maps[0], " ");
            }
        }
        return map;
    }

    @NonNull
    public String toString(){
        return "Response: status = "+ status + " / message = " + getMessage();
    }
}