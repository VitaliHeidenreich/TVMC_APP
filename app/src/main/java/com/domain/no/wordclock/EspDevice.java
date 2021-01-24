package com.domain.no.wordclock;

public class EspDevice {

    EspDevice(){
        // nop
    }

    EspDevice(String name, String adresse){
        Name = name;
        Address = adresse;
    }

    private String Name;
    private String Address;
    //Dif connection address
    private static String ConName = null;
    private static String ConAddress = null;
    private static final String defDev = "MyTV_";

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAdresse() {
        return Address;
    }

    public void setAdresse(String adresse) {
        Address = adresse;
    }

    public String getConAddress(){
        return ConAddress;
    }

    public void setConAddress(String conAddress) {
        ConAddress = conAddress;
    }

    public String getConName() {
        return ConName;
    }

    public void setConName(String conName){
        ConName = conName;
    }

    public static final String getDefDev(){ return defDev; }
}
