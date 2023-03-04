package com.example.tareagooglemaps;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class Adapterplaces implements GoogleMap.InfoWindowAdapter {

    private static final String TAG = "Adaptadorplaces";
    private LayoutInflater inflater;
    private Places places;

    public Adapterplaces(LayoutInflater inflater){
        this.inflater = inflater;

    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }


    @Override
    public View getInfoWindow( Marker marker) {
        View v = inflater.inflate(R.layout.ly_info_places, null);
        places = (Places) marker.getTag();
        ImageView image = (ImageView)v.findViewById(R.id.imgicon);
        ((TextView)v.findViewById(R.id.Nombre)).setText(places.name);
        ((TextView)v.findViewById(R.id.Direccion)).setText(places.vicinity);
        ((TextView)v.findViewById(R.id.lblweb)).setText(places.open);
        Glide.with(v).load(places.icon).into(image);
        ImageView image2 = (ImageView)v.findViewById(R.id.Urlimg);
        Glide.with(v).load( "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=" + places.photo_reference + "&key=AIzaSyB5MkIB5lNnQH1kC1tZ3ATeEsv7z66moKs").into(image2);
        return v;
    }
}
