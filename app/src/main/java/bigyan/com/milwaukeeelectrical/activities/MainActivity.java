package bigyan.com.milwaukeeelectrical.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bigyan.com.milwaukeeelectrical.CustomListAdapter;
import bigyan.com.milwaukeeelectrical.R;
import bigyan.com.milwaukeeelectrical.model.CityModel;
import bigyan.com.milwaukeeelectrical.model.Model;
import bigyan.com.milwaukeeelectrical.service.WeatherService;
import bigyan.com.milwaukeeelectrical.sqlite.SQliteHandler;
import bigyan.com.milwaukeeelectrical.utils.AlertDialog;
import butterknife.InjectView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {

    private static final String API_URL = "http://api.openweathermap.org";
    private static final String API_KEY = "cb597564e37027c14776fe5d91fec890";

    @InjectView(R.id.add_item_fab)
    FloatingActionButton addItemFab;

    private SQliteHandler database;

    //initializ listview
    private ListView displayView;
    private CustomListAdapter listAdapter;
    private List<CityModel> cityModelList;

    private CityModel cityModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database = new SQliteHandler(MainActivity.this);
        cityModelList = new ArrayList<>();

        displayView = (ListView)findViewById(R.id.displayAllListView);

        listAdapter = new CustomListAdapter(cityModelList, this);
        displayView.setAdapter(listAdapter);

        //Check for database
        if(isNetworkAvailable()){
            //get all cities from database and display
            try{
                List<String> cityList = database.getAllCities();
                for(String s: cityList){
                    //get Weather Data
                    displayAll(s);
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
        else {
            AlertDialog alertDialog = new AlertDialog();
            alertDialog.displayNetworkAlert(MainActivity.this, "Network Alert", "Internet is not available. Please check your connection");
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_item_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(MainActivity.this, AddItemActivity.class);
                startActivity(mIntent);
            }
        });


        /**
         * Add ActionListener to listOnClick
         * */
        displayView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent mIntent = new Intent(view.getContext(), ListDetails.class);
                mIntent.putExtra("obj", cityModelList.get(position));
                startActivityForResult(mIntent, 0);
            }
        });

        /**
         *Set on Item long click Listener
         */
        displayView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {


                return true;
            }
        });

    }

    /**
     * Check the Network Status here
     * */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    /**
    * Populate ListView by retrieving data for all cities in the database
    * */
    public void displayAll(final String s){

        OkHttpClient client = new OkHttpClient();

        client.interceptors().add(new Interceptor() {
            @Override
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.httpUrl()
                        .newBuilder()
                        .addQueryParameter("q", s)
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

                    cityModel.setCityName(city);
                    cityModel.setCityStatus(status);
                    cityModel.setCityHumidity(humidity);
                    cityModel.setCityPressure(pressure);

                    cityModelList.add(cityModel);
                    listAdapter.notifyDataSetChanged();


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Intent i = new Intent(MainActivity.this, SearchWeatherActivity.class);
            startActivity(i);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
