package com.pb.app.fixchat.api.entity;

import android.util.Log;

import androidx.collection.ArrayMap;

import java.util.ArrayList;
import java.util.Map;

public class ValueConverter {

    public static ArrayList<Server> convertValueToServers(String value){
        ArrayList<Server> servers = new ArrayList<>();
        if (!value.isEmpty() && value.length() > 2) {
            String[] x = value.substring(2, value.length()-2).split("\\}, \\{");
            for (int i = 0; i < x.length; i++){
                String[] y = x[i].split(", ");
                Map<String, String> map = new ArrayMap<>();
                for (int n = 0; n < y.length; n++){
                    String[] z = y[n].split("=");
                    if (z.length > 1) {
                        map.put(z[0], z[1]);
                    } else {
                        map.put(z[0], " ");
                    }
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
                    if (map.get("is_added") != null){
                        boolean bool = map.get("is_added").equals("true");
                        server.setHaveUser(bool);
                    }
                    servers.add(server);
                } catch (Exception e) {
                    Log.e("SERVER PARSER", "convertValue: " + e.toString());
                }
            }
        }
        Log.d("SERVER PARSER", "convertValue: " + servers.toString());
        return servers;
    }

    public static ArrayList<User> convertValueToUsers(String value){
        ArrayList<User> users = new ArrayList<>();
        if (value != null && value.length() > 2) {
            String[] x = value.substring(2, value.length()-2).split("\\}, \\{");
            for (int i = 0; i < x.length; i++){
                String[] y = x[i].split(", ");
                Map<String, String> map = new ArrayMap<>();
                for (int n = 0; n < y.length; n++){
                    String[] z = y[n].split("=");
                    if (z.length > 1) {
                        map.put(z[0], z[1]);
                    } else {
                        map.put(z[0], " ");
                    }
                }
                try {
                    User user = new User();
                    user.setId(map.get("id"));
                    user.setName(map.get("name"));
                    user.setEmail(map.get("email"));
                    user.setCompany(map.get("company"));
                    user.setRole(Integer.parseInt(map.get("role").substring(0, map.get("role").length()-2)));
                    users.add(user);
                } catch (Exception e) {
                    Log.e("USER PARSER", "convertValue: " + e.toString());
                }
            }
        }
        Log.d("USER PARSER", "convertValue: " + users.toString());
        return users;
    }
}
