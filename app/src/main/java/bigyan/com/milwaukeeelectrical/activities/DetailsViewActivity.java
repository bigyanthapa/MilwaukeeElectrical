package bigyan.com.milwaukeeelectrical.activities;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import bigyan.com.milwaukeeelectrical.R;
import bigyan.com.milwaukeeelectrical.utils.AlertDialog;

public class DetailsViewActivity extends AppCompatActivity {

    private TextView cityView;
    private TextView statusView;
    private TextView humidityView;
    private TextView pressureView;
    private FloatingActionButton floatingActionButton;

    private AlertDialog alertDialog;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initialize
        initialize();

        bundle = getIntent().getExtras();

        //Extract the data…
        String venName = bundle.getString("VENUE_NAME");

        //Create the text view
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(venName);

        cityView.setText(bundle.getString("City"));
        statusView.setText(bundle.getString("Status"));
        humidityView.setText(bundle.getString("Humidity"));
        pressureView.setText(bundle.getString("Pressure"));

        //Add this city to database if this button is clicked
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog = new AlertDialog();
                alertDialog.displayAddItemAlert(DetailsViewActivity.this, "Alert", "You are about to add this city to your database", bundle.getString("City"));
            }
        });

    }

    //initialize widgets
    public void initialize() {
        cityView = (TextView) findViewById(R.id.displayCityText);
        statusView = (TextView) findViewById(R.id.displayStatusText);
        humidityView = (TextView) findViewById(R.id.displayHumidityText);
        pressureView = (TextView) findViewById(R.id.displayPressureText);
        floatingActionButton = (FloatingActionButton)findViewById(R.id.details_fab);
    }

}
