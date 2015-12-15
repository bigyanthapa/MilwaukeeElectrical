package bigyan.com.milwaukeeelectrical.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import bigyan.com.milwaukeeelectrical.R;
import bigyan.com.milwaukeeelectrical.sqlite.SQliteHandler;

/**
 * Created by bigyanthapa on 12/14/15.
 */
public class AlertDialog {

    private String title;
    private String message;

    private SQliteHandler database;

    public AlertDialog(){

    }

    public void displayAddItemAlert(final Context context, String title, String message, final String city){

        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon(R.drawable.ic_warning_black);
        // Setting OK Button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                //add to database
                database = new SQliteHandler(context);
                database.addCity(context,city);
            }
        });

        //set negative Button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, "Item Not Added to Database", Toast.LENGTH_SHORT).show();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public void displayNetworkAlert(final Context context, String title, String message){

        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon(R.drawable.ic_warning_black);
        // Setting OK Button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                // Toast.makeText(context, "Item Added to Database", Toast.LENGTH_SHORT).show();
                //add to database

            }
        });


        // Showing Alert Message
        alertDialog.show();
    }

    public void redundantDataAlert(final Context context, String title, String message){

        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon(R.drawable.ic_warning_black);
        // Setting OK Button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(context,"Closed",Toast.LENGTH_SHORT).show();
            }
        });


        // Showing Alert Message
        alertDialog.show();
    }

    public void emptyInputAlert(final Context context, String title, String message){

        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon(R.drawable.ic_warning_black);
        // Setting OK Button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(context,"Closed",Toast.LENGTH_SHORT).show();
            }
        });


        // Showing Alert Message
        alertDialog.show();
    }




}
