package bigyan.com.milwaukeeelectrical.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;

import bigyan.com.milwaukeeelectrical.R;
import bigyan.com.milwaukeeelectrical.model.Model;
import bigyan.com.milwaukeeelectrical.service.WeatherService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class SearchWeatherActivity extends AppCompatActivity {

    private static final String API_URL = "http://api.openweathermap.org";
    private static final String API_KEY = "cb597564e37027c14776fe5d91fec890";

    private Button okButton;
    private EditText inputText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_weather);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initialize widgets
        initializeWidgets();



        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //Make sure city is not empty
                if(inputText.getText().toString().trim().length() == 0){
                    Snackbar.make(view, "City cannot be Empty", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    inputText.requestFocus();
                }else{
                    inputText.setText("");
                    final String input = inputText.getText().toString();

                    OkHttpClient client = new OkHttpClient();

                    client.interceptors().add(new Interceptor() {
                        @Override
                        public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            HttpUrl url = request.httpUrl()
                                    .newBuilder()
                                    .addQueryParameter("q", input)
                                    .addQueryParameter("appid", API_KEY)
                                    .build();
                            request = request.newBuilder().url(url).build();
                            return chain.proceed(request);
                        }
                    });

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(API_URL)
                            .client(client)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    WeatherService service = retrofit.create(WeatherService.class);

                    Call<Model> call = service.getWheatherReport();


                    call.enqueue(new Callback<Model>() {
                        @Override
                        public void onResponse(Response<Model> response, Retrofit retrofit) {

                            Model result = response.body();

                            try {


                                String city = response.body().getName();
                                String status = response.body().getWeather().get(0).getDescription();
                                String humidity = response.body().getMain().getHumidity().toString();
                                String pressure = response.body().getMain().getPressure().toString();

                                //Pass Data to Details view Activity
                                Intent i = new Intent(SearchWeatherActivity.this, DetailsViewActivity.class);
                                Bundle bundle = new Bundle();
                                //Add your data from getFactualResults method to bundle
                                bundle.putString("City", city);
                                bundle.putString("Status", status);
                                bundle.putString("Humidity", humidity);
                                bundle.putString("Pressure", pressure);
                                //Add the bundle to the intent
                                i.putExtras(bundle);
                                startActivity(i);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Throwable t) {

                        }
                    });

                }

            }
        });
    }

    //Initialize widgets here
    public void initializeWidgets(){
        okButton = (Button)findViewById(R.id.buttonOk);
        inputText = (EditText)findViewById(R.id.searchInput);
        inputText.setClickable(true);

    }


}
