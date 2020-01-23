package espol.edu.bagtraking.ui.map;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.LinkedList;


import espol.edu.bagtraking.Activity.perfil_usuario_1;
import espol.edu.bagtraking.Modelo.ModelFireBase;
import espol.edu.bagtraking.Modelo.Variables;
import espol.edu.bagtraking.R;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MapViewModel mapViewModel;
    private static final String TAG = MapFragment.class.getSimpleName();
    private GoogleMap mMap;
    DatabaseReference db_reference;
    LinkedList<LatLng> posiciones ;

    private HashMap<String, Marker> mMarkers = new HashMap<>();
    public static double DISTANCIA = 0.0d;
    public static boolean Ready = false;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mapViewModel =
                ViewModelProviders.of(this).get(MapViewModel.class);
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        ((perfil_usuario_1) getActivity()).getSupportActionBar().setTitle("Home Mapa");
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map1);
        if(mapFragment == null){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map1,mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        posiciones = new LinkedList<>();



        /*
        final TextView textView = root.findViewById(R.id.text_home);
        mapViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

         */
        return root;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Authenticate with Firebase when the Google map is loaded
        mMap = googleMap;
        mMap.setMaxZoomPreference(16);
        subscribeToUpdates();
        StartThread();
        HiloNotificacions();
        //Toast.makeText(getActivity(),"La distancia de tu maleta ahora es de .."+String.valueOf(DISTANCIA), Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onStart() {
        Ready = true;
        super.onStart();
    }
    private void subscribeToUpdates() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("datos_gps").child("4334BA");

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                setMarker(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                setMarker(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void setMarker(DataSnapshot dataSnapshot) {
        // When a location update is received, put or update
        // its value in mMarkers, which contains all the markers
        // for locations received, so that we can build the
        // boundaries required to show them all on the map at once
        String key = dataSnapshot.getKey();
        HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
        double lat = Double.parseDouble(value.get("Latitud").toString());
        double lng = Double.parseDouble(value.get("Longitud").toString());
        System.out.println(key);
        if(lat!=0 && lng!=0){
            LatLng location = new LatLng(lat, lng);
            posiciones.add(location);
            if (!mMarkers.containsKey(key)) {
                mMarkers.put(key, mMap.addMarker(new MarkerOptions().title(key).position(location)));
            } else {
                mMarkers.get(key).setPosition(location);
            }
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : mMarkers.values()) {
                builder.include(marker.getPosition());
            }
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 350));
        }

    }
    public void StartThread(){
        Thread thread = new Thread() {
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(3000);
                        while(posiciones.isEmpty()){}
                        DISTANCIA = CalcularDistancia(posiciones.get(posiciones.size()-2).latitude,posiciones.get(posiciones.size()-2).longitude
                        ,posiciones.getLast().latitude, posiciones.getLast().longitude);
                        //toast.show();
                        //Toast.makeText(contex,"La distancia de tu maleta ahora es de .."+String.valueOf(DISTANCIA), Toast.LENGTH_SHORT).show();



                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        };

        thread.start();
    }
    private double CalcularDistancia( double lat1,double long1,double lat2,double long2){
        double constante = Math.PI /180;
        lat2 = lat2 * constante;
        long2 = long2* constante;
        lat1 =lat1* constante;
        long1 =long1* constante;
        double R = 6371; //km

        double deltalat = lat2-lat1;
        double deltalong = long2 -long1;
        double a= Math.pow(Math.sin(deltalat/2),2) + Math.cos(lat1)*Math.cos(lat2)*Math.pow(Math.sin(deltalong/2),2);
        double c = 2*R*Math.atan(Math.sqrt(a))*1000;
        return c;
    }
    public void HiloNotificacions(){
        Thread thread = new Thread() {
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(10000);
                        while(posiciones.isEmpty()&& DISTANCIA !=0){}
                        if(DISTANCIA>=100d){
                            System.out.println("Enviando Notificacion.."+new ModelFireBase("Aplicacion").getCurrentUser().getEmail());
                        }



                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        };

        thread.start();
    }

}