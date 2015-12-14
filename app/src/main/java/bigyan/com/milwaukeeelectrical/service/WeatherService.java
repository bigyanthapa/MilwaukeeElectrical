package bigyan.com.milwaukeeelectrical.service;


import bigyan.com.milwaukeeelectrical.model.Model;
import retrofit.Call;
import retrofit.http.GET;

public interface WeatherService {

    @GET("/data/2.5/weather?q=Chicago,us&appid=2de143494c0b295cca9337e1e96b00e0")
    Call<Model> getWheatherReport();

}
