package bigyan.com.milwaukeeelectrical;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import bigyan.com.milwaukeeelectrical.model.CityModel;

/**
 * Created by bigyanthapa on 12/15/15.
 */
public class ListArrayAdapter extends ArrayAdapter {

    private ArrayList<CityModel> cityModelList = new ArrayList<>();
    private Context context;

    public ListArrayAdapter(Context context, ArrayList<CityModel> cityList) {
        super(context, 0, cityList);
        this.context = context;
        this.cityModelList = cityList;
    }


    public String getCityName(int position){
        // Get the data item for this position
        CityModel cityModel = (CityModel)getItem(position);
        return cityModel.getCityName();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        CityModel cityModel = (CityModel)getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        // Lookup view for data population
        TextView cityText = (TextView) convertView.findViewById(R.id.cityNameTextView);
        TextView statusText = (TextView) convertView.findViewById(R.id.statusTextView);
        // Populate the data into the template view using the data object

        cityText.setText(cityModel.getCityName());
        statusText.setText(cityModel.getCityStatus());

        // Return the completed view to render on screen
        return convertView;
    }


}
