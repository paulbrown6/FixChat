package com.pb.app.fixchat.api.entityV2;

import android.util.Log;

import androidx.collection.ArrayMap;

import java.util.ArrayList;
import java.util.Map;

public class ValueConverter {

    public static ArrayList<Server> convertValueToServers(String value){
        ArrayList<Server> servers = new ArrayList<>();
        if (value != null && value.length() > 2 && value.contains("\\[")) {
            String[] x = value.substring(1, value.length()-1).split(", ");
            for (int i = 0; i < x.length; i++){
                String[] y = x[i].substring(1, x[i].length()-1).split(", ");
                Map<String, String> map = new ArrayMap<>();
                for (int n = 0; n < y.length; n++){
                    String[] z = y[n].split("=");
                    map.put(z[0], z[1]);
                }
                try {
                    Server server = new Server();
                    server.setId(map.get("id"));
                    server.setName(map.get("name"));
                    server.setHv(map.get("hv"));
                    server.setState(map.get("state"));
                    server.setNetwork(map.get("network"));
                    server.setCompany(map.get("company"));
                    server.setDescription(map.get("description"));
                    server.setIp(map.get("ip"));
                    server.setOut_addr(map.get("out_addr"));
                    servers.add(server);
                } catch (Exception e) {
                    Log.e("SERVER PARSER", "convertValue: " + e.toString());
                }
            }
        }
        return servers;
    }

    public static ArrayList<User> convertValueToUsers(String value){
        ArrayList<User> users = new ArrayList<>();
        if (value != null && value.length() > 2 && value.contains("\\[")) {
            String[] x = value.substring(1, value.length()-1).split(", ");
            for (int i = 0; i < x.length; i++){
                String[] y = x[i].substring(1, x[i].length()-1).split(", ");
                Map<String, String> map = new ArrayMap<>();
                for (int n = 0; n < y.length; n++){
                    String[] z = y[n].split("=");
                    map.put(z[0], z[1]);
                }
                try {
                    User user = new User();
                    user.setId(map.get("id"));
                    user.setName(map.get("name"));
                    user.setEmail(map.get("email"));
                    user.setCompany(map.get("company"));
                    user.setRole(Integer.parseInt(map.get("role")));
                    users.add(user);
                } catch (Exception e) {
                    Log.e("USER PARSER", "convertValue: " + e.toString());
                }
            }
        }
        return users;
    }
}
