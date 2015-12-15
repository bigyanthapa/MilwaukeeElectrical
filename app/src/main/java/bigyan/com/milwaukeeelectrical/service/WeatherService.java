package bigyan.com.milwaukeeelectrical.service;


import bigyan.com.milwaukeeelectrical.model.Model;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface WeatherService {

    @GET("/data/2.5/weather")
    Call<Model> getWheatherReport();

    // RxJava version
    @GET("/users/{id}/name")
    Observable<String> userName(@Path("userId") String userId);

}
