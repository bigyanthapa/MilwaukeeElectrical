package bigyan.com.milwaukeeelectrical.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import bigyan.com.milwaukeeelectrical.R;
import bigyan.com.milwaukeeelectrical.utils.AlertDialog;

public class AddItemActivity extends AppCompatActivity {

    private Button addCityButotn;
    private EditText inputCity;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addCityButotn = (Button)findViewById(R.id.buttonAddFavoriteCity);
        inputCity = (EditText)findViewById(R.id.inputFavoriteCity);



        //Add this City to Database
        addCityButotn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(inputCity.getText().toString().trim().length() == 0){
                    Snackbar.make(view, "City cannot be Empty", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    inputCity.requestFocus();
                }else{
                    String city = inputCity.getText().toString();
                    alertDialog = new AlertDialog();
                    alertDialog.displayAddItemAlert(AddItemActivity.this,"Alert","You are about to add this city to your database",city);

                }

            }
        });


    }

}
