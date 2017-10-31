package co.edu.udea.arqsoft.assistapp.restapi;

import co.edu.udea.arqsoft.assistapp.BuildConfig;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by AW 13 on 30/10/2017.
 */

public class RestClientImpl  {
    private static RestApiInterface restApiInterface;

    public static RestApiInterface getClientLogin() {

        String API_URL = BuildConfig.IPAPP;
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(API_URL).addConverterFactory(GsonConverterFactory.create());
        //String authToken = Credentials.basic(user, password);
        //AuthenticationInterceptor authenticationInterceptor = new AuthenticationInterceptor(authToken);
        //if (!httpClient.interceptors().contains(authenticationInterceptor)) {
            //httpClient.addInterceptor(authenticationInterceptor);
        //}
        Retrofit retrofit = builder.client(httpClient.build()).build();
        restApiInterface = retrofit.create(RestApiInterface.class);
        return restApiInterface;
    }


}
