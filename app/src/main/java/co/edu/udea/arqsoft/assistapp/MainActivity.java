package co.edu.udea.arqsoft.assistapp;

import android.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import co.edu.udea.arqsoft.assistapp.adapters.CourseAdapter;
import co.edu.udea.arqsoft.assistapp.adapters.SessionAdapter;
import co.edu.udea.arqsoft.assistapp.dtos.Asistencia;
import co.edu.udea.arqsoft.assistapp.dtos.Course;
import co.edu.udea.arqsoft.assistapp.dtos.Session;
import co.edu.udea.arqsoft.assistapp.dtos.User;
import co.edu.udea.arqsoft.assistapp.restapi.RestClientImpl;
import co.edu.udea.arqsoft.assistapp.utils.NetworkUtilities;
import io.fabric.sdk.android.services.common.SafeToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    RecyclerView recyclerView;
    private String currentView = "COURSES";
    private int idCourse = 0;
    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeMain";
    private int MY_PERMISSIONS_REQUEST_GPS;
    private int MY_PERMISSIONS_REQUEST_GPS_COARSE;
    private boolean permissionsGranted=false;
    LatLng myPos;

    BroadcastReceiver rec = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("LOADSESSIONS".equals(intent.getAction())) {
                int course = intent.getIntExtra("course", 0);
                idCourse = course;
                loadSessions(course);
                currentView = "SESSIONS";
                getSupportActionBar().setTitle("Sesiones");
            } else if ("LOADCOURSES".equals(intent.getAction())) {
                loadCourses();
                currentView = "SESSIONS";
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("SESSIONS".equals(currentView)) {
                    Intent i = new Intent(MainActivity.this, AddSessionActivity.class);
                    i.putExtra("idCourse", idCourse);
                    startActivity(i);
                } else {
                    Intent i = new Intent(MainActivity.this, AddCourseActivity.class);
                    startActivity(i);
                }

            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.scrollView);
        IntentFilter filterS = new IntentFilter("LOADSESSIONS");
        IntentFilter filterC = new IntentFilter("LOADCOURSES");
        this.registerReceiver(rec, filterS);
        this.registerReceiver(rec, filterC);
        loadCourses();
        askPermissions();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (currentView.equals("SESSIONS")) {
            getSupportActionBar().setTitle("Mis Cursos");
            currentView = "COURSES";
            loadCourses();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(this, BarcodeCaptureActivity.class);
            intent.putExtra(BarcodeCaptureActivity.AutoFocus, false);
            intent.putExtra(BarcodeCaptureActivity.UseFlash, false);
            startActivityForResult(intent, RC_BARCODE_CAPTURE);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void loadCourses() {
        User user = getUserFromPrefs();
        Log.e("client route", user.getName());
        Call<List<Course>> call = RestClientImpl.getClientLogin()
                .getCourses(user.getId());
        call.enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                Log.e("code", "" + response.code());
                List<Course> courses = response.body();
                if (courses != null) {
                    Log.e("numero de cursos", "" + courses.size());
                    showCourses(courses);
                    TextView txtView = (TextView) findViewById(R.id.no_courses);
                    txtView.setVisibility(View.INVISIBLE);
                } else {
                    Log.e("no hay nada", "" + response.code());
                    TextView txtView = (TextView) findViewById(R.id.no_courses);
                    txtView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                Log.e("Error Loading Routes", t.getMessage());
                if (t.getMessage().startsWith("java.lang.IllegalStateException")) {
                    Log.e("Entro one route", t.getMessage());
                }
            }
        });
    }

    private void loadSessions(int idCourse) {
        User user = getUserFromPrefs();
        Log.e("client session", user.getName());
        Call<List<Session>> call = RestClientImpl.getClientLogin()
                .getSessions(idCourse);
        call.enqueue(new Callback<List<Session>>() {
            @Override
            public void onResponse(Call<List<Session>> call, Response<List<Session>> response) {
                Log.e("code", "" + response.code());
                List<Session> sessions = response.body();
                if (sessions != null) {
                    Log.e("numero de cursos", "" + sessions.size());
                    showSessions(sessions);
                    TextView txtView = (TextView) findViewById(R.id.no_courses);
                    txtView.setVisibility(View.INVISIBLE);
                } else {
                    Log.e("no hay nada", "" + response.code());
                    sessions = new LinkedList<>();
                    showSessions(sessions);
                    TextView txtView = (TextView) findViewById(R.id.no_courses);
                    txtView.setText("No hay sessiones");
                    txtView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Session>> call, Throwable t) {
                Log.e("Error Loading Routes", t.getMessage());
                if (t.getMessage().startsWith("java.lang.IllegalStateException")) {
                    Log.e("Entro one route", t.getMessage());
                }
            }
        });

    }

    private void showCourses(List<Course> courses) {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager lManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lManager);
        RecyclerView.Adapter adapter = new CourseAdapter(courses, this);
        recyclerView.setAdapter(adapter);
        recyclerView.smoothScrollToPosition(adapter.getItemCount());
        recyclerView.smoothScrollToPosition(0);
    }

    private void showSessions(List<Session> sessions) {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager lManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lManager);
        RecyclerView.Adapter adapter = new SessionAdapter(sessions, this);
        recyclerView.setAdapter(adapter);
        recyclerView.smoothScrollToPosition(adapter.getItemCount());
        recyclerView.smoothScrollToPosition(0);
    }


    private User getUserFromPrefs() {
        SharedPreferences prefs =
                getSharedPreferences("AssistPrefs", Context.MODE_PRIVATE);
        User user = new User();
        user.setCedula(prefs.getString("cedula", ""));
        user.setId(prefs.getInt("id", 0));
        user.setRol(prefs.getString("rol", ""));
        user.setName(prefs.getString("name", ""));
        user.setLastname(prefs.getString("lastname", ""));
        user.setPassw(prefs.getString("password", ""));
        user.setUsername(prefs.getString("username", ""));
        return user;

    }


    @Override
    public void onDestroy() {
        unregisterReceiver(rec);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Log.e("Barcode read: ", barcode.displayValue);
                    saveAssist(barcode.displayValue);
                } else {
                    Log.e("Error Barcode", "No barcode captured intent data is null");
                }
            } else {

                Log.e("Error barcode", "" + resultCode);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void saveAssist(String barcodeParam)  {
        User user = getUserFromPrefs();
        Log.e("account user", user.toString());
        Log.e("network", String.valueOf(NetworkUtilities.isConnected(this)));
        String barcode = barcodeParam.trim();
        final Asistencia assist = new Asistencia();
        assist.setSesion(barcode);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String day = dateFormat.format(new Date());
        dateFormat = new SimpleDateFormat("HH:mm:ss");
        String hour =dateFormat.format(new Date());
        assist.setFechaAsistencia(day+"T"+hour);
        assist.setEstudiante(user.getId());
        updatePosition();

        Double lat = BigDecimal.valueOf(myPos.latitude)
                .setScale(6, RoundingMode.HALF_UP)
                .doubleValue();
        Double lon = BigDecimal.valueOf(myPos.longitude)
                .setScale(6, RoundingMode.HALF_UP)
                .doubleValue();
        if (myPos != null) {
            assist.setLatitud(lat);
            assist.setLongitud(lon);
        } else {
            assist.setLatitud(0D);
            assist.setLongitud(0D);
        }

        Log.e("id", ""+assist.getId());
        Log.e("FechaReporte", ""+assist.getFechaAsistencia());
        Log.e("session", ""+assist.getSesion());
        Log.e("est", ""+assist.getEstudiante());
        Log.e("lat", ""+assist.getLatitud());
        Log.e("lon", ""+assist.getLongitud());
        if(NetworkUtilities.isConnected(this)){

            Call<Asistencia> call = RestClientImpl.getClientLogin().saveAssist(assist);
            call.enqueue(new Callback<Asistencia>() {
                @Override
                public void onResponse(Call<Asistencia> call, Response<Asistencia> response) {
                    Asistencia assistR = response.body();
                    if(response.code()==201){
                        Toast toast = SafeToast.makeText(getApplicationContext(),"Asistencia Reportada " , SafeToast.LENGTH_SHORT );
                        toast.show();
                    }else{
                        Log.e("Response code", ""+response.code());
                        Log.e("Response code", ""+response.body());
                        Toast toast = SafeToast.makeText(getApplicationContext(),"Ha ocurrido un Error  " , SafeToast.LENGTH_SHORT );
                        toast.show();
                    }
                }

                @Override
                public void onFailure(Call<Asistencia> call, Throwable t) {
                    Log.e("Error Duke", t.getMessage());
                    Toast toast = SafeToast.makeText(getApplicationContext(),"Ha ocurrido un Error  " , SafeToast.LENGTH_SHORT );
                    toast.show();
                    // assertTrue(false);
                }
            });
        }else{
            Toast toast = SafeToast.makeText(getApplicationContext(),"Asistencia Reportada " , SafeToast.LENGTH_SHORT );
            toast.show();
        }

    }


    public void askPermissions(){
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
            this.permissionsGranted=true;
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
                Log.e("inc mypos lat", myPos.latitude + "");
                Log.e("inc mypos long", myPos.longitude + "");

            }
        }

    }
}


