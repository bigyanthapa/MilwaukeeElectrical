package bigyan.com.milwaukeeelectrical.service;


import bigyan.com.milwaukeeelectrical.model.Model;
import retrofit.Call;
import retrofit.http.GET;

public interface WeatherService {

    @GET("/data/2.5/weather")
    Call<Model> getWheatherReport();

}
