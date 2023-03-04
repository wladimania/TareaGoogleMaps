package com.example.tareagooglemaps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Places {
    public String lct_lat;
    public String lct_lng;
    public String icon;
    public String name;
    public String vicinity;
    public String photo_reference;
    public String open;

    public String getLct_lat() {
        return lct_lat;
    }

    public void setLct_lat(String lct_lat) {
        this.lct_lat = lct_lat;
    }

    public String getLct_lng() {
        return lct_lng;
    }

    public void setLct_lng(String lct_lng) {
        this.lct_lng = lct_lng;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getPhoto_reference() {
        return photo_reference;
    }

    public void setPhoto_reference(String photo_reference) {
        this.photo_reference = photo_reference;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public Places(JSONObject a) throws JSONException {
        JSONObject geometry = a.getJSONObject("geometry");
        JSONObject location = geometry.getJSONObject("location");
        lct_lat = location.getString("lat").toString();
        lct_lng = location.getString("lng").toString();
        icon = a.getString("icon").toString();
        name = a.getString("name").toString();
        vicinity = a.getString("vicinity").toString();

        if(!a.isNull("opening_hours")){
            JSONObject horario = a.getJSONObject("opening_hours");
            if(horario.getString("open_now")=="true")
                open = ("Esta Abierto");
            else open = ("Esta Cerrado");
        }else
        {
            open = "No tiene horario";
        }
        System.out.println("Hola aqui el horario = " + open);

        //para sacar las photos
        JSONObject JSONlista = null;
        JSONlista = a;

        if(!JSONlista.isNull("photos")   ){
            JSONArray JSONlistaphoto = JSONlista.getJSONArray("photos");
            JSONObject photreferen = JSONlistaphoto.getJSONObject(0);
            photo_reference = photreferen.getString("photo_reference").toString();
        }else
        {
            photo_reference = "No tiene foto";
        }
        System.out.println("Hola aqui la foto = " + photo_reference);
    }

    public static ArrayList<Places> JsonObjectsBuild(JSONArray datos) throws JSONException {
        ArrayList<Places> places = new ArrayList<>();

        for (int i = 0; i < datos.length() && i<20; i++) {
            places.add(new Places(datos.getJSONObject(i)));
        }
        return places;
    }
}
