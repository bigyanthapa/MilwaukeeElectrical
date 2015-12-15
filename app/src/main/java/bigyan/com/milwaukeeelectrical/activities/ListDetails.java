package bigyan.com.milwaukeeelectrical.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import bigyan.com.milwaukeeelectrical.R;
import bigyan.com.milwaukeeelectrical.model.CityModel;

public class ListDetails extends AppCompatActivity {

    private TextView cityName;
    private TextView cityStatus;
    private TextView cityHumidity;
    private TextView cityPressure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initialize();

        Intent intent = getIntent();
        CityModel cityModel = (CityModel) intent.getSerializableExtra("obj");

        cityName.setText(cityModel.getCityName());
        cityStatus.setText(cityModel.getCityStatus());
        cityHumidity.setText(cityModel.getCityHumidity());
        cityPressure.setText(cityModel.getCityPressure());


    }

    public void initialize(){
        cityName = (TextView)findViewById(R.id.listDisplayCityText);
        cityStatus = (TextView)findViewById(R.id.listDisplayStatusText);
        cityHumidity = (TextView)findViewById(R.id.listDisplayHumidityText);
        cityPressure = (TextView)findViewById(R.id.listDisplayPressureText);

    }

}
