package com.pb.app.fixchat.api.entityV2;
import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Server {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("hv")
    @Expose
    private String hv;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("network")
    @Expose
    private String network;
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("ip")
    @Expose
    private String ip;
    @SerializedName("out_addr")
    @Expose
    private String out_addr;

    public static String START_POWER = "start_power";
    public static String STOP_POWER = "stop_power";
    public static String STOP_POWER_FORCE = "stop_power_force";
    public static String START_NETWORK = "start_network";
    public static String STOP_NETWORK = "stop_network";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHv() {
        return hv;
    }

    public void setHv(String hv) {
        this.hv = hv;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getOut_addr() {
        return out_addr;
    }

    public void setOut_addr(String out_addr) {
        this.out_addr = out_addr;
    }

    @NonNull
    public String toString(){
        return "ID = "+ id + " / name = " + name + "  / state = " + state +
                "  / Network = " + network + "company = "+ company +
                " / description = " + description + "ip = "+ ip +
                "  / out_addr = " + out_addr;
    }
}