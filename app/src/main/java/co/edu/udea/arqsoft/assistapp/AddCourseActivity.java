package co.edu.udea.arqsoft.assistapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import co.edu.udea.arqsoft.assistapp.dtos.Course;
import co.edu.udea.arqsoft.assistapp.dtos.User;
import co.edu.udea.arqsoft.assistapp.restapi.RestClientImpl;
import co.edu.udea.arqsoft.assistapp.utils.DatePickerFragment;
import co.edu.udea.arqsoft.assistapp.utils.NetworkUtilities;
import io.fabric.sdk.android.services.common.SafeToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Actividad para Ingresar un Curso
 */
public class AddCourseActivity extends AppCompatActivity {


    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */


    private View mProgressView;
    private View mLoginFormView;
    private EditText mDateFinish;
    private AutoCompleteTextView mDescription;
    private AutoCompleteTextView mNameCourse;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_course);
        setSupportActionBar(toolbar);
        setupActionBar();
        mDateFinish = (EditText) findViewById(R.id.date_expire);
        mDateFinish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        mLoginFormView = findViewById(R.id.course_form);
        mProgressView = findViewById(R.id.course_progress);
        mNameCourse = (AutoCompleteTextView) findViewById(R.id.name);
        mDescription = (AutoCompleteTextView) findViewById(R.id.description);

    }

    /**
     * Muestra un Dialog para que el usuario ingrese Una fecha
     */
    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because january is zero
                final String selectedDate = year + "-" + String.format("%02d", (month+1)) + "-" + String.format("%02d", day);
                mDateFinish.setText(selectedDate);
            }
        });
        newFragment.show(this.getFragmentManager(), "datePicker");
    }




    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.curso, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e("menu", "before case");
        Log.e("menu", ""+item.getItemId());
        switch (item.getItemId()) {

            case R.id.save_curso:
                Log.e("menu", "after case");
                saveCourse();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * Guarda un Curso
     */

    private void saveCourse() {
        User user = getUserFromPrefs();
        if(NetworkUtilities.isConnected(this)){
            showProgress(true);
            Course curso= new Course();
            curso.setDescripcion(this.mDescription.getText().toString());
            curso.setFechaExpiracion(this.mDateFinish.getText().toString()+"T00:00:00");
            curso.setName(this.mNameCourse.getText().toString());
            curso.setOwner(user.getId());
            curso.setHabilitado(Boolean.TRUE);
            Call<Course> call = RestClientImpl.getClientLogin().saveCourse(curso);
            call.enqueue(new Callback<Course>() {
                @Override
                public void onResponse(Call<Course> call, Response<Course> response) {
                    Course curso = response.body();
                    if(response.code()==201){
                        Toast toast = SafeToast.makeText(getApplicationContext(),"Curso " + curso.getName() + "Guardado.", SafeToast.LENGTH_SHORT );
                        toast.show();
                        saveSuccess();
                    }else{
                        Snackbar snackbar = Snackbar
                                .make(mLoginFormView, "Ha ocurrido un error" , Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }

                @Override
                public void onFailure(Call<Course> call, Throwable t) {
                    Snackbar snackbar = Snackbar
                            .make(mLoginFormView, "Ha ocurrido un error" , Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            });
        }else{
            Snackbar snackbar = Snackbar
                    .make(mLoginFormView, "Sin Conexion a internet" , Snackbar.LENGTH_LONG);
            snackbar.show();
        }

    }

    /**
     * Obtiene la informacion guardada del Usuario
     * @return
     */

    private User getUserFromPrefs(){
        SharedPreferences prefs =
                getSharedPreferences("AssistPrefs", Context.MODE_PRIVATE);
        User user = new User();
        user.setCedula(prefs.getString("cedula",""));
        user.setId(prefs.getInt("id",0));
        user.setRol(prefs.getString("rol",""));
        user.setName(prefs.getString("name",""));
        user.setLastname(prefs.getString("lastname",""));
        user.setPassw(prefs.getString("password",""));
        user.setUsername(prefs.getString("username",""));
        return user;

    }


    public void saveSuccess(){
        Intent in = new Intent("LOADCOURSES");
        sendBroadcast(in);
        this.finish();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

    }

}

