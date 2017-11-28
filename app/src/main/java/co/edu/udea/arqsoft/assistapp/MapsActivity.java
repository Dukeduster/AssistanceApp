package co.edu.udea.arqsoft.assistapp;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LatLng pos;
    LatLng myPos;
    private CameraUpdate cu;
    private int MY_PERMISSIONS_REQUEST_GPS;
    private int MY_PERMISSIONS_REQUEST_GPS_COARSE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Double lat = getIntent().getDoubleExtra("lat", 0D);
        Double lon = getIntent().getDoubleExtra("lon", 0D);
        pos=new LatLng(lat,lon);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        updatePosition();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(pos);
        builder.include(myPos);
        LatLngBounds bounds = builder.build();
        int padding = 300; // offset from edges of the map in pixels
        cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(this.pos).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(this.pos));

        mMap.animateCamera(cu);
    }

    public void updatePosition(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_GPS);
        }

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_GPS_COARSE
            );
        }
        if (MY_PERMISSIONS_REQUEST_GPS == PackageManager.PERMISSION_GRANTED || MY_PERMISSIONS_REQUEST_GPS_COARSE
                == PackageManager.PERMISSION_GRANTED) {
            Log.e("position", "Granted");
            LocationManager locationManager = (LocationManager)
                    getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            Location location = locationManager.getLastKnownLocation(locationManager
                    .getBestProvider(criteria, false));

            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            if (location != null) {
                myPos = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.setMyLocationEnabled(true);
                Log.e("inc mypos lat", myPos.latitude + "");
                Log.e("inc mypos long", myPos.longitude + "");

            }
        }

    }
}
