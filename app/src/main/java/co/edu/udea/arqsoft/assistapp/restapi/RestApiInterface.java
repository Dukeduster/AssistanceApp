package co.edu.udea.arqsoft.assistapp.restapi;

import java.util.List;

import co.edu.udea.arqsoft.assistapp.dtos.Course;
import co.edu.udea.arqsoft.assistapp.dtos.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by AW 13 on 30/10/2017.
 */

public interface RestApiInterface {

    @GET("users")
    Call<List<User>> login(@Query("user") String user, @Query("passw") String password);

    @Headers({"Accept: application/json",})
    @POST("cursos/")
    Call<Course> saveCourse(@Body Course course);
}
