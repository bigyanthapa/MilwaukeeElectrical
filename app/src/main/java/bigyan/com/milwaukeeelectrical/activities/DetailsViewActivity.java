package bigyan.com.milwaukeeelectrical.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import bigyan.com.milwaukeeelectrical.R;

public class DetailsViewActivity extends AppCompatActivity {

    private TextView cityView;
    private TextView statusView;
    private TextView humidityView;
    private TextView pressureView;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initialize
        initialize();

        Bundle bundle = getIntent().getExtras();

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
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(DetailsViewActivity.this);

                // Setting Dialog Title
                alertDialog.setTitle("Warning");

                // Setting Dialog Message
                alertDialog.setMessage("Do you Want to Add this City to Database");

                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.ic_warning_black);

                // Setting OK Button
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed
                        Toast.makeText(DetailsViewActivity.this, "Item Added to Database", Toast.LENGTH_SHORT).show();

                    }
                });

                //set negative Button
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(DetailsViewActivity.this, "Item Not Added to Database",Toast.LENGTH_SHORT).show();
                    }
                });

                // Showing Alert Message
                alertDialog.show();
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
