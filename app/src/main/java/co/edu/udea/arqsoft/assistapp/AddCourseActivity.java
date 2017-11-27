package co.edu.udea.arqsoft.assistapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import co.edu.udea.arqsoft.assistapp.dtos.Course;
import co.edu.udea.arqsoft.assistapp.dtos.User;
import co.edu.udea.arqsoft.assistapp.restapi.RestClientImpl;
import co.edu.udea.arqsoft.assistapp.utils.DatePickerFragment;
import co.edu.udea.arqsoft.assistapp.utils.NetworkUtilities;
import co.edu.udea.arqsoft.assistapp.utils.Utils;
import io.fabric.sdk.android.services.common.SafeToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via email/password.
 */
public class AddCourseActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
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

        // Set up the login form.
       // mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
       // populateAutoComplete();
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

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because january is zero
                final String selectedDate = year + "-" + (month+1) + "-" + day;
                mDateFinish.setText(selectedDate);
            }
        });
        newFragment.show(this.getFragmentManager(), "datePicker");
    }


    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //populateAutoComplete();
            }
        }
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


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
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

    private void saveCourse() {
        User user = getUserFromPrefs();
        Log.e("account user", user.toString());
        Log.e("network", String.valueOf(NetworkUtilities.isConnected(this)));

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
                        Snackbar snackbar = Snackbar
                                .make(mLoginFormView, "Curso " + curso.getName() + " Guardado.", Snackbar.LENGTH_LONG);
                        //snackbar.show();
                        toast.show();
                        saveSuccess();
                    }else{
                        Log.e("Response code", ""+response.code());
                        Log.e("Response code", ""+response.body());
                        Snackbar snackbar = Snackbar
                                .make(mLoginFormView, "Ha ocurrido un error" , Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }

                @Override
                public void onFailure(Call<Course> call, Throwable t) {
                    Log.e("Error Duke", t.getMessage());
                    Snackbar snackbar = Snackbar
                            .make(mLoginFormView, "Ha ocurrido un error" , Snackbar.LENGTH_LONG);
                    snackbar.show();
                    // assertTrue(false);
                }
            });
        }else{
            Snackbar snackbar = Snackbar
                    .make(mLoginFormView, "Sin Conexion a internet" , Snackbar.LENGTH_LONG);
            snackbar.show();
        }

    }

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
        this.finish();
    }

}

