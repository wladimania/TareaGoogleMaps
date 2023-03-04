package com.example.tareagooglemaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    GoogleMap mapa;
    LatLng punto;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        queue= Volley.newRequestQueue(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mapa = googleMap;
        mapa.getUiSettings().setZoomControlsEnabled(true);
        mapa.setOnMapClickListener(this);
        CameraUpdate camUpd1 =
                CameraUpdateFactory
                        .newLatLngZoom(new LatLng(-1.0801982016846716, -79.50113233338), 15);

        mapa.moveCamera(camUpd1);
        mapa.setInfoWindowAdapter(new Adapterplaces(LayoutInflater.from(getApplicationContext())));
        mapa.setOnMapClickListener(this);

    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        this.mapa.clear();
        //punto = new LatLng(latLng.latitude, latLng.longitude);
        mapa.addMarker(new MarkerOptions().position(latLng)
                .title("Hola estas"));
        this.drawRectangle(latLng,500);
        this.AddMarker(latLng);

    }
    public void AddMarker(LatLng latLng) {
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?fields=name&location="+latLng.latitude+","+latLng.longitude+"&radius=1500&type=bar&key=AIzaSyB5MkIB5lNnQH1kC1tZ3ATeEsv7z66moKs";
        System.out.println(latLng.latitude+","+latLng.longitude);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject JSONlista = null;

                            JSONlista = new JSONObject(response);
                            JSONArray JSONlistaplaces = JSONlista.getJSONArray("results");


                            ArrayList<Places> lstplace = Places.JsonObjectsBuild(JSONlistaplaces);

                            for(int i=0;i<lstplace.size();i++){
                                //System.out.println(lstplace.get(i).location_lat+","+lstplace.get(i).location_lng);
                                LatLng punto = new LatLng(Double.valueOf(lstplace.get(i).lct_lat),Double.valueOf(lstplace.get(i).lct_lng));
                                mapa.addMarker(new MarkerOptions().position(punto).title("Marker suplentes!! #" + i)).setTag(lstplace.get(i));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR EN PARSEO");
                    }
                }
        );
        queue.add(stringRequest);
    }
    private void drawRectangle(LatLng center, double radius) {
        // Crear un objeto LatLngBounds.Builder
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        // Calcular las ubicaciones de los cuatro vértices
        LatLng northeast = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 45);
        LatLng northwest = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 315);
        LatLng southeast = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 135);
        LatLng southwest = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 225);

        // Crear un objeto PolygonOptions para el rectángulo
        PolygonOptions rectOptions = new PolygonOptions()
                .add(southeast) // Añadir el vértice inferior-izquierdo
                .add(northeast) // Añadir el vértice superior-izquierdo
                .add(northwest) // Añadir el vértice superior-derecho
                .add(southwest) // Añadir el vértice inferior-derecho
                .strokeColor(Color.GREEN) // Establecer el color del borde del rectángulo
                .fillColor(Color.TRANSPARENT); // Establecer el color de relleno del rectángulo (transparente)

        // Añadir el polígono al mapa
        mapa.addPolygon(rectOptions);

    }


}